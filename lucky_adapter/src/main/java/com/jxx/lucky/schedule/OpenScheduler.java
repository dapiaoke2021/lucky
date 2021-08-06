package com.jxx.lucky.schedule;

import com.jxx.lucky.service.IPointGenerator;
import com.jxx.lucky.service.IssueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OpenScheduler {

    @Autowired
    IssueService issueService;

    @Autowired
    IPointGenerator pointGenerator;

    @Scheduled(cron = "5 * * * * ?")
    public void openEveryMinute() {
        int point = pointGenerator.getPoint(issueService.getCurrentIssueNo());
        log.info("{}期开奖{}", issueService.getCurrentIssueNo(), point);
        issueService.open(point);
    }

    @Scheduled(cron = "55 * * * * ?")
    public void closeIssue() {
        log.info("{}期停止下注", issueService.getCurrentIssueNo());
        issueService.closeBet();
    }
}
