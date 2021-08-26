package com.jxx.lucky.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.jxx.lucky.domain.BankerTypeEnum;
import com.jxx.lucky.domain.Player;
import com.jxx.lucky.domain.Robot;
import com.jxx.lucky.service.IssueService;
import com.jxx.lucky.service.RobotService;
import com.jxx.user.service.IUserService;
import com.jxx.user.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class RobotServiceImpl implements RobotService {

    private IssueService issueService;

    @Autowired
    IUserService userService;

    @Value("${robot-min-money}")
    private Integer robotMinMoney;

    @Value("${robot-max-money}")
    private Integer robotMaxMoney;

    @Value("${min-ongoing-issue}")
    private Integer minOngoingIssue;

    @Value("${max-ongoing-issue}")
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
        log.debug("是否为机器人：playerId = {}, inGameRobotMap={}", playerId, inGameRobotMap);
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
            log.debug("robot={}", robot);
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
    public void handleBecameBankerEvent(BankerTypeEnum bankerType, Long playerId) {
        log.debug("成为庄家消息：playerId = {}, bankerType = {}", playerId, bankerType);
        Robot robot = new Robot();
        robot.setBankerType(bankerType);
        robot.setId(playerId);
        inGameRobotMap.put(playerId, robot);
        log.debug("成功庄家消息：inGameRobotMap={}", inGameRobotMap);
    }

    @Override
    public void handleOffedBankerEvent(Long playerId) {
        inGameRobotMap.remove(playerId);
    }
}
