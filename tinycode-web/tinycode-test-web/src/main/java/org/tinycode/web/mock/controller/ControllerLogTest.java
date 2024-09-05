package org.tinycode.web.mock.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;

/**
 * TODO
 * @ClassName BaseControllerTest
 * @author littlehui
 * @date 2021/7/7 13:24
 * @version 1.0
 **/
@Slf4j
public class ControllerLogTest {

    @Before
    public void init() {
        log.info("开始测试...");
    }

    @After
    public void after() {
        log.info("测试结束...");
    }

}
