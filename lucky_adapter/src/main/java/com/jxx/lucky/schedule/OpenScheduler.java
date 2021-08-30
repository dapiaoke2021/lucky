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
        String[] points;
        try{
            points = pointGenerator.getPoint(issueService.getCurrentIssueNo());
        }catch (Exception e) {
            String[] p = {
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
            points = p;
        }
        log.info("{}期开奖{}", issueService.getCurrentIssueNo(), points);
        issueService.open(points);
    }

    @Scheduled(cron = "55 * * * * ?")
    public void closeIssue() {
        log.info("{}期停止下注", issueService.getCurrentIssueNo());
        issueService.closeBet();
    }
}
