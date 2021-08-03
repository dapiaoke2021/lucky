package com.jxx.common.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jxx.common.service.IPointGenerator;

import java.util.HashMap;

public class EthUsdtPointGenerator implements IPointGenerator {
    @Override
    public Integer getPoint(String no) {
        int year = DateUtil.date().getField(DateField.YEAR);
        DateTime date = DateUtil.parse(year + no, "yyyyMMddHHmm");
        String response = HttpUtil.get("https://www.binance.com/fapi/v1/klines", new HashMap<String, Object>(){{
            put("symbol", "ETHUSDT");
            put("interval", "1m");
            put("startTime", DateUtil.offsetMinute(date, -1).getTime());
            put("endTime", DateUtil.offsetSecond(date, 1).getTime());
        }});
        JSONArray array = JSON.parseArray(response);
        JSONArray last = array.getJSONArray(array.size() - 1);
        String data = (String) last.get(4);
        return Integer.parseInt(String.valueOf(data.charAt(data.length() - 1)));
    }
}
