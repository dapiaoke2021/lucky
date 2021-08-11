package io.renren.config;

import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Filter配置
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-21 21:56
 */
@Component
@Async
public class OhMyEmailConfiguration {





    public void send(String monitorKeywords, String email, String html, File sendFile, String sendFileName) {
        try {
            OhMyEmail.subject(monitorKeywords)
                    .from(monitorKeywords)
                    .to(email)
                    .html(html)
                    .attach(sendFile,sendFileName)
                    .send();
        } catch (SendMailException e) {
            e.printStackTrace();
        }
    }
}
