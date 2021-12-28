package org.tinycode.web.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * TinyCode的示范代码
 *
 * @author littlehui
 * @version 1.0
 * @date 2021/12/08 15:19
 */
@MapperScan("com.reader.dao.dao")
@SpringBootApplication(scanBasePackages = {"org.tinycode.web.app.controller"
        ,"org.tinycode.web.app.dao"
        ,"org.tinycode.web.app.rao"
        ,"org.tinycode.web.app.service"})
public class TinyCodeWebApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication application = new SpringApplication(TinyCodeWebApplication.class);
        application.run(args);
    }
}
