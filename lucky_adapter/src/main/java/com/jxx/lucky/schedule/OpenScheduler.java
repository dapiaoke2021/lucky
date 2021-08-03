package com.jxx.lucky.schedule;

import com.jxx.common.service.IPointGenerator;
import com.jxx.lucky.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OpenScheduler {

    @Autowired
    IssueService issueService;

    @Autowired
    IPointGenerator pointGenerator;

    @Scheduled(cron = "1 * * * * ?")
    public void openEveryMinute() {
        pointGenerator.getPoint(issueService.getCurrentIssueNo());
    }

    @Scheduled(cron = "55 * * * * ？")
    public void closeIssue() {
        issueService.closeBet();
    }
}
