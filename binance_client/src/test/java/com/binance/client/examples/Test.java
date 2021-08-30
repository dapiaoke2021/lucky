package com.binance.client.examples;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.Candlestick;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author chenweilong
 * @email 1433471850@qq.com
 * @date 2021-08-07 16:12
 */
public class Test {


    public static void main(String[] args) {
        Date now = new Date();

        RequestOptions options = new RequestOptions();
        options.setUrl("https://api.binance.com");
        SyncRequestClient syncRequestClient = SyncRequestClient.create("P9ZTWSIFxu7s5KjJ2aZovMywRhuRaOsHWdaZjTpZx25yD8wgjcGLCguov7qdWc8Q", "ZSu4ehNCBDiapRefL4hFj454YwysMIbHvoGssIXnt8ly9zPhBm91sOpbWeWtNrU6",
                options);
//        System.out.println();


        List<DateTime> dateTimes = DateUtil.rangeToList(DateUtil.offsetDay(now, -30), DateUtil.offsetDay(now, -1), DateField.DAY_OF_YEAR);


        for (DateTime dateTime : dateTimes) {
            DateTime dateTime00 = DateUtil.beginOfDay(dateTime);
            DateTime dateTime12 = DateUtil.offsetHour(dateTime00, 12);
            DateTime dateTime24 = DateUtil.endOfDay(dateTime);

            List<Candlestick> btcusdt1 = syncRequestClient.getCandlestick("ETHUSDT", CandlestickInterval.ONE_MINUTE, dateTime00.getTime(), dateTime12.getTime(), 1000);

            List<Candlestick> btcusdt2 = syncRequestClient.getCandlestick("ETHUSDT", CandlestickInterval.ONE_MINUTE, dateTime12.getTime(), dateTime24.getTime(), 1000);

            boolean bool = btcusdt1.addAll(btcusdt2);

            if (bool) {
                FileUtil.writeString(JSONUtil.toJsonStr(btcusdt1),new File("/Users/chenweilong/Downloads/binance_history_data/eth/"+DateUtil.format(dateTime00,"yyyyMMdd")),"UTF-8");
            }


        }
    }
}
