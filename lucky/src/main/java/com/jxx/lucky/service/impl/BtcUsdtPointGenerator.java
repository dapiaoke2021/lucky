package com.jxx.lucky.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jxx.lucky.service.IPointGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class BtcUsdtPointGenerator implements IPointGenerator {

    @Override
    public String[] getPoint(String no) {
        int year = DateUtil.date().getField(DateField.YEAR);
        DateTime date = DateUtil.parse(year + no, "yyyyMMddHHmm");
        String response = HttpUtil.get("https://www.binance.com/fapi/v1/klines", new HashMap<String, Object>(){{
            put("symbol", "BTCUSDT");
            put("interval", "1m");
            put("startTime", DateUtil.offsetMinute(date, -1).getTime());
            put("endTime", DateUtil.offsetSecond(date, 1).getTime());
        }});
        JSONArray array = JSON.parseArray(response);
        JSONArray last = array.getJSONArray(array.size() - 1);
        log.debug("{}期，请求startTime={} endTime={}, last={}",
                no,
                DateUtil.offsetMinute(date, -1).getTime(),
                DateUtil.offsetSecond(date, 1).getTime(),
                last
        );
        return last.stream().map(Object::toString).toArray(String[]::new);
    }
}
