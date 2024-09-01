package org.tinycode.web.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * TODO
 *
 * @author littlehui
 * @version 1.0
 * @date 2022/12/16 18:20
 */
@SpringBootApplication
public class TinyCodeAdminWebApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication application = new SpringApplication(TinyCodeAdminWebApplication.class);
        application.run(args);
    }
}
