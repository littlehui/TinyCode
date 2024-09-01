package org.tinycode.web.tests.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tinycode.web.app.TinyCodeWebApplication;
import org.tinycode.web.mock.controller.ControllerAwareTest;

/**
 * TODO
 *
 * @author littlehui
 * @version 1.0
 * @date 2022/04/19 21:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TinyCodeWebApplication.class)
public class TinyCodeControllerTests extends ControllerAwareTest {

    @Test
    public void getTest() throws Exception {
        get(null, "/tinycode/list");
    }

}
