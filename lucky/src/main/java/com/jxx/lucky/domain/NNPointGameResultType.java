package com.jxx.lucky.domain;

import com.jxx.lucky.domain.nn.NNGameResultType;
import com.jxx.lucky.domain.nn.NiuEnum;
import lombok.Data;

@Data
public class NNPointGameResultType extends NNGameResultType {

    public NNPointGameResultType() {
        super("");
    }

    public NNPointGameResultType(String point) {
        super(point);
    }

    @Override
    protected Integer getOdds(NiuEnum niuEnum) {
        return 10;
    }
}
