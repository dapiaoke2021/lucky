package com.jxx.lucky.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.Candlestick;
import com.jxx.lucky.config.BinanceClientConfig;
import com.jxx.lucky.service.IPointGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class BtcUsdtPointGenerator implements IPointGenerator {

    @Autowired
    private SyncRequestClient syncRequestClient;

    @Override
    public String[] getPoint(String no) {
        int year = DateUtil.date().getField(DateField.YEAR);
        DateTime date = DateUtil.parse(year + no, "yyyyMMddHHmm");
        String response = HttpUtil.get("https://api.binance.com//api/v3/klines", new HashMap<String, Object>(){{
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

    @Override
    public Candlestick getCandlestick(String no) {
        int year = DateUtil.date().getField(DateField.YEAR);
        DateTime date = DateUtil.parse(year + no, "yyyyMMddHHmm");
        List<Candlestick> candlesticks = syncRequestClient.getCandlestick("BTCUSDT",
                CandlestickInterval.ONE_MINUTE,
                DateUtil.offsetMinute(date, -1).getTime(),
                DateUtil.offsetSecond(date, 1).getTime(),
                1
        );
        if(CollUtil.isEmpty(candlesticks)) {
            return null;
        }
        return candlesticks.get(0);
    }
}
