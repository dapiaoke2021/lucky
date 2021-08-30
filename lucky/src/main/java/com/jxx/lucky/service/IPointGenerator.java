package com.jxx.lucky.service;

import com.binance.client.model.market.Candlestick;

import java.util.List;

public interface IPointGenerator {

    String[] getPoint(String no);

    Candlestick getCandlestick(String no);
}
