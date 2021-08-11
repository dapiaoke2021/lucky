package io.renren.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动加载类
 * 启动完成之后将三级联动数据加载进内存
 */
@Component
public class StartupLoading implements CommandLineRunner {


    @Autowired
    private OhMyEmailConfig ohMyEmailConfig;


    @Override
    public void run(String... args) throws Exception {

    }

}
