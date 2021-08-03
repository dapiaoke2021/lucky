package com.jxx.lucky.event;

import lombok.Data;

import java.util.Map;

@Data
public class IssueOpenedEvent {
    private String issueNo;
    private Integer point;
    private Map<Long, Integer> result;
    private Map<Long, Integer> tax;
}
