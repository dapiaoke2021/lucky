package com.jxx.lucky.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.jxx.lucky.domain.BankerTypeEnum;
import com.jxx.lucky.domain.Player;
import com.jxx.lucky.domain.Robot;
import com.jxx.lucky.service.IssueService;
import com.jxx.lucky.service.RobotService;
import com.jxx.user.service.IUserServiceApi;
import com.jxx.user.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RobotServiceImpl implements RobotService {

    private IssueService issueService;

    @Reference
    IUserServiceApi userService;

    @Value("${lucky.robot-min-money}")
    private Integer robotMinMoney;

    @Value("${lucky.robot-max-money}")
    private Integer robotMaxMoney;

    @Value("${lucky.min-ongoing-issue}")
    private Integer minOngoingIssue;

    @Value("${lucky.max-ongoing-issue}")
    private Integer maxOngoingIssue;

    private Map<Long, Robot> inGameRobotMap;

    public RobotServiceImpl(IssueService issueService) {
        this.issueService = issueService;
        inGameRobotMap = new HashMap<>();
    }

    @Override
    public Player createRobot() {
        UserVO robot = userService.getRobot(RandomUtil.randomInt(robotMinMoney, robotMaxMoney));
        Player player = new Player();
        player.setId(robot.getId());
        player.setMoney(robot.getMoney());
        return player;
    }

    @Override
    public boolean isRobot(Long playerId) {
        return inGameRobotMap.containsKey(playerId);
    }

    @Override
    public void prepareOff(Long playerId) {
        Robot robot = inGameRobotMap.get(playerId);
        if (robot == null) {
            log.error("{} 不是机器人，准备下庄失败", playerId);
            return;
        }

        robot.setOnGoingIssueCount(RandomUtil.randomInt(minOngoingIssue, maxOngoingIssue));
    }

    @Override
    public void handleIssueOpenEvent() {
        inGameRobotMap.forEach((robotId, robot) -> {
            Integer onGoingIssueCount = robot.getOnGoingIssueCount();
            if (onGoingIssueCount != null) {
                if (onGoingIssueCount > 0) {
                    robot.setOnGoingIssueCount(onGoingIssueCount - 1);
                } else {
                    issueService.offBanker(robot.getId(), robot.getBankerType());
                }
            }
        });
    }

    @Override
    public void handleBecameBankerEvent(BankerTypeEnum bankerType, Player banker) {
        Robot robot = new Robot();
        robot.setBankerType(bankerType);
        robot.setId(banker.getId());
        inGameRobotMap.put(banker.getId(), robot);
    }

    @Override
    public void handleOffedBankerEvent(Long playerId) {
        inGameRobotMap.remove(playerId);
    }
}
