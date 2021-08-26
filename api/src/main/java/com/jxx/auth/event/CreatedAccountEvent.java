package com.jxx.auth.event;

import com.jxx.auth.vo.AccountVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatedAccountEvent {
    AccountVO accountVO;
}
