package com.jxx.auth.event;

import com.jxx.auth.vo.AccountVO;
import lombok.Data;

@Data
public class CreatedAccountEvent {
    AccountVO accountVO;
}
