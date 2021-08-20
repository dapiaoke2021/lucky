package com.jxx.lucky.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.cola.exception.BizException;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.jxx.lucky.component.LuckyEventSource;
import com.jxx.lucky.config.IssueGameConfig;
import com.jxx.lucky.config.IssueGameProperty;
import com.jxx.lucky.domain.*;
import com.jxx.lucky.domain.nn.IssueNN;
import com.jxx.lucky.dos.BankerRecordDO;
import com.jxx.lucky.dos.BetRecordDO;
import com.jxx.lucky.dos.IssueDO;
import com.jxx.lucky.mapper.BankerRecordMapper;
import com.jxx.lucky.mapper.BetMapper;
import com.jxx.lucky.mapper.IssueMapper;
import com.jxx.lucky.param.BetParam;
import com.jxx.lucky.service.impl.IssueServiceImpl;
import com.jxx.user.service.IUserServiceApi;
import com.jxx.user.vo.UserVO;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@ExtendWith(SpringExtension.class)
public class IssuePointServiceImplTest {

    @InjectMocks
    IssueServiceImpl issueService;

    @Mock
    BetMapper betMapper;

    @Mock
    IssueMapper issueMapper;

    @Mock
    BankerRecordMapper bankerRecordMapper;

    @Mock
    RobotService robotService;

    @Mock
    IUserServiceApi userServiceApi;

    @Mock
    LuckyEventSource luckyEventSource;

    @Captor
    ArgumentCaptor<BankerRecordDO> bankRecordCaptor;

    @Captor
    ArgumentCaptor<BetRecordDO> betRecordDOArgumentCaptor;

    String issueNo;

    @BeforeAll
    public static void init() {
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), IssueDO.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), BetRecordDO.class);
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), BankerRecordDO.class);
    }


    @BeforeEach
    public  void IssueServiceImplTest() {
        ReflectionTestUtils.setField(issueService, "bankerMinMoney", 1000);
        ReflectionTestUtils.setField(issueService, "userService", userServiceApi);
        issueNo = DateUtil.format(DateUtil.date(), "MMddHHmm");

        IssueGameProperty issueGameProperty = new IssueGameProperty();
        issueGameProperty.setGameConfig(
                Arrays.asList(
                        new GameConfig(BankerTypeEnum.NN, "com.jxx.lucky.domain.NNGame"),
                        new GameConfig(BankerTypeEnum.SN, "com.jxx.lucky.domain.PointGame")
                )
        );
        ReflectionTestUtils.setField(
                issueService,
                "gameConfig",
                issueGameProperty
        );
        ReflectionTestUtils.setField(
                issueService,
                "luckyEventSource",
                luckyEventSource
        );
        Mockito.when(luckyEventSource.luckyOutput()).thenReturn(new MessageChannel() {
            @Override
            public boolean send(Message<?> message, long l) {
                return false;
            }
        });
        IssueNN currentIssue =(IssueNN) ReflectionTestUtils.getField(issueService, "currentIssue");
        currentIssue.buildIssue(Arrays.asList(
                new GameConfig(BankerTypeEnum.NN, "com.jxx.lucky.domain.NNGame"),
                new GameConfig(BankerTypeEnum.SN, "com.jxx.lucky.domain.PointGame")
        ));
        issueService.initBankerQueue();
    }


    @Test
    public void testBecomeBankerWhenTooLittleTopBet() {
        UserVO userVO1 = createUser(1L, 1000);
        Mockito.when(userServiceApi.getById(1L)).thenReturn(userVO1);
        BizException bizException = Assertions.assertThrows(BizException.class, () -> {
            issueService.becomeBanker(1L, BankerTypeEnum.BIG_SMALL, 500);
        });
        Assertions.assertEquals(bizException.getErrCode(), "BIZ_LUCKY_MONEY_TOO_LITTLE");
    }

    private UserVO createUser(Long id, Integer money) {
        UserVO userVO1 = new UserVO();
        userVO1.setId(id);
        userVO1.setMoney(money);
        return userVO1;
    }

    private void mockGetUser(Long id, Integer money) {
        UserVO userVO1 = createUser(id, money);
        Mockito.when(userServiceApi.getById(id)).thenReturn(userVO1);
    }

    @Test
    public void testBecomeBankerWhenNotEnoughMoney() {
        mockGetUser(1L, 1000);
        BizException bizException = Assertions.assertThrows(BizException.class, () -> {
            issueService.becomeBanker(1L, BankerTypeEnum.BIG_SMALL, 1001);
        });
        Assertions.assertEquals(bizException.getErrCode(), "BIZ_LUCKY_NOT_ENOUGH_MONEY");
    }

    @Test
    public void testBecomeBanker() {
        mockGetUser(1L, 1000);
        BankerRecordDO bankerRecordDO1 = new BankerRecordDO();
        bankerRecordDO1.setAmount(1000);
        bankerRecordDO1.setBankerType(BankerTypeEnum.NN);
        bankerRecordDO1.setPlayerId(1L);
        bankerRecordDO1.setIssueNo(issueNo);

        Mockito.when(bankerRecordMapper.insert(bankRecordCaptor.capture())).thenReturn(1);
        issueService.becomeBanker(1L, BankerTypeEnum.NN, 1000);
        Assertions.assertEquals(bankerRecordDO1, bankRecordCaptor.getValue());
        Map<BankerTypeEnum, Banker> currentBankerMap = issueService.getCurrentBanker();
        Banker banker = new Banker(new Player(), BankerTypeEnum.NN);
        banker.setUserId(1L);
        banker.setMoney(1000);
        banker.setType(BankerTypeEnum.NN);
        Assertions.assertEquals(banker, currentBankerMap.get(BankerTypeEnum.NN));
    }

    @Test
    public void testBecomeBankerInQueue() {
        IssueNN currentIssue = (IssueNN) ReflectionTestUtils.getField(issueService, "currentIssue");
        Map<BankerTypeEnum, Banker> currentBanker = issueService.getCurrentBanker();
        Player banker = new Player();
        banker.setId(1L);
        banker.setMoney(10000);
        currentIssue.getGameMap().get(BankerTypeEnum.NN).becomeBanker(banker);

        mockGetUser(2L, 2000);
        issueService.becomeBanker(2L, BankerTypeEnum.NN, 1001);
        Map<BankerTypeEnum, ConcurrentLinkedQueue<Player>> bankerQueue
                = (Map<BankerTypeEnum, ConcurrentLinkedQueue<Player>>)ReflectionTestUtils.getField(issueService, "bankerQueueMap");

        Player player2 = new Player();
        player2.setMoney(1001);
        player2.setId(2L);
        Assertions.assertEquals(player2, bankerQueue.get(BankerTypeEnum.NN).peek());
    }

    @Test
    public void testBet() {
        mockGetUser(1L, 1000);
        issueService.becomeBanker(1L, BankerTypeEnum.NN, 1000);

        mockGetUser(3L, 1000);
        Mockito.when(betMapper.insert(betRecordDOArgumentCaptor.capture())).thenReturn(1);
        issueService.bet(
                3L,
                Arrays.asList(new BetParam(BetTypeEnum.BET_1, 100, null), new BetParam(BetTypeEnum.BET_2, 100, null)));
        List<BetRecordDO> betRecordDOList = betRecordDOArgumentCaptor.getAllValues();
        String betNo = "3-" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        BetRecordDO betRecordDO1 = new BetRecordDO();
        betRecordDO1.setBetNo(betNo);
        betRecordDO1.setIssueNo(issueNo);
        betRecordDO1.setBetType(BetTypeEnum.BET_1.ordinal());
        betRecordDO1.setMoney(100);
        betRecordDO1.setPlayerId(3L);
        betRecordDO1.setState(BetStateEnum.ONGOING);
        Assertions.assertEquals(betRecordDO1, betRecordDOList.get(0));

        BetRecordDO betRecordDO2 = new BetRecordDO();
        betRecordDO2.setBetNo(betNo);
        betRecordDO2.setIssueNo(issueNo);
        betRecordDO2.setBetType(BetTypeEnum.BET_2.ordinal());
        betRecordDO2.setMoney(100);
        betRecordDO2.setPlayerId(3L);
        betRecordDO2.setState(BetStateEnum.ONGOING);
        Assertions.assertEquals(betRecordDO2, betRecordDOList.get(1));
    }

    @Test
    public void testOpenWhenOffBanker() {
        // 情况1：下庄后有玩家在等待队列
        // 情况2：下庄后没有玩家在等待队列，机器人顶上
        // 情况3：队列中的玩家下庄
        mockGetUser(1L, 10000);
        issueService.becomeBanker(1L, BankerTypeEnum.NN, 10000);
        mockGetUser(2L, 10000);
        issueService.becomeBanker(2L, BankerTypeEnum.NN, 10000);


        mockGetUser(4L, 10000);
        issueService.becomeBanker(4L, BankerTypeEnum.SN, 10000);

        mockGetUser(3L, 1000);
        issueService.bet(
                3L,
                Arrays.asList(new BetParam(BetTypeEnum.BET_1, 100, null), new BetParam(BetTypeEnum.BET_2, 100, null)));

        issueService.offBanker(1L, BankerTypeEnum.NN);
        issueService.offBanker(4L, BankerTypeEnum.SN);

        Player robot = new Player();
        robot.setId(5L);
        robot.setMoney(10000);
        Mockito.when(robotService.createRobot()).thenReturn(robot);
        issueService.open(new String[]{"1", "1", "1234.08", "1234.08", "1234.08"});

        Map<BankerTypeEnum, Banker> currentBanker = issueService.getCurrentBanker();
        Assertions.assertEquals(2L, currentBanker.get(BankerTypeEnum.NN).getUserId());
        Assertions.assertEquals(5L, currentBanker.get(BankerTypeEnum.SN).getUserId());
    }

    @Test
    public void testOpenWhenNotEnoughOffBanker() {
        mockGetUser(1L, 1000);
        issueService.becomeBanker(1L, BankerTypeEnum.NN, 1000);
        mockGetUser(2L, 10000);
        issueService.becomeBanker(2L, BankerTypeEnum.NN, 10000);

        mockGetUser(3L, 1000);
        issueService.bet(
                3L,
                Arrays.asList(new BetParam(BetTypeEnum.BET_1, 50, null), new BetParam(BetTypeEnum.BET_2, 50, null)));
        issueService.open(new String[]{"1", "1", "1234.08", "1234.08", "1234.18"});
        Map<BankerTypeEnum, Banker> currentBanker = issueService.getCurrentBanker();
        Assertions.assertEquals(2L, currentBanker.get(BankerTypeEnum.NN).getUserId());
    }
}
