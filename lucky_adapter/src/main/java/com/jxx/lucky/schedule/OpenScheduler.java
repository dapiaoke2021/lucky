package com.jxx.lucky.schedule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.jxx.lucky.service.IPointGenerator;
import com.jxx.lucky.service.IssueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Slf4j
@Component
public class OpenScheduler {

    @Autowired
    IssueService issueService;

    @Autowired
    IPointGenerator pointGenerator;

    @Scheduled(cron = "5 * * * * ?")
    public void openEveryMinute() {
//        String[] points = pointGenerator.getPoint(issueService.getCurrentIssueNo());
//        0 = {Long@12978} 1630243920000
//        1 = "48536.15000000"
//        2 = "48550.34000000"
//        3 = "48515.00000000"
//        4 = "48528.30000000"
//        5 = "15.36139000"
//        6 = {Long@12984} 1630243979999
//        7 = "745562.22750430"
//        8 = {Integer@12986} 754
//        9 = "5.76265000"
//        10 = "279671.75166710"
//        11 = "0"

        String[] points = {
                String.valueOf(DateUtil.date().getTime()),
                String.valueOf(RandomUtil.randomBigDecimal(BigDecimal.valueOf(30000), BigDecimal.valueOf(40000)).setScale(2, BigDecimal.ROUND_UP)),
                String.valueOf(RandomUtil.randomBigDecimal(BigDecimal.valueOf(30000), BigDecimal.valueOf(40000)).setScale(2, BigDecimal.ROUND_UP)),
                String.valueOf(RandomUtil.randomBigDecimal(BigDecimal.valueOf(30000), BigDecimal.valueOf(40000)).setScale(2, BigDecimal.ROUND_UP)),
                String.valueOf(RandomUtil.randomBigDecimal(BigDecimal.valueOf(30000), BigDecimal.valueOf(40000)).setScale(2, BigDecimal.ROUND_UP)),
                String.valueOf(RandomUtil.randomBigDecimal(BigDecimal.valueOf(10), BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_UP)),
                String.valueOf(DateUtil.date().getTime()),
                String.valueOf(RandomUtil.randomBigDecimal(BigDecimal.valueOf(30000), BigDecimal.valueOf(40000)).setScale(2, BigDecimal.ROUND_UP)),
                String.valueOf(754),
                String.valueOf(5.7626500),
                String.valueOf(RandomUtil.randomBigDecimal(BigDecimal.valueOf(30000), BigDecimal.valueOf(40000)).setScale(2, BigDecimal.ROUND_UP)),
                String.valueOf(0)};


        log.info("{}期开奖{}", issueService.getCurrentIssueNo(), points);
        issueService.open(points);
    }

    @Scheduled(cron = "55 * * * * ?")
    public void closeIssue() {
        log.info("{}期停止下注", issueService.getCurrentIssueNo());
        issueService.closeBet();
    }
}
