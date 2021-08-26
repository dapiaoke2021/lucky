package com.jxx.lucky.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IssueOpenedEvent {
    private String issueNo;
    private String[] points;
    private Map<Long, Integer> result;
    private Map<Long, Integer> tax;
}
