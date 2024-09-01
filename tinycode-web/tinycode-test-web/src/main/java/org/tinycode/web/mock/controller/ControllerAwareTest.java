package org.tinycode.web.mock.controller;

import org.junit.Before;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * TODO
 *
 * @author littlehui
 * @version 1.0
 * @date 2022/04/19 22:00
 */
public class ControllerAwareTest extends ControllerExecuteTest implements ApplicationContextAware {

    ApplicationContext applicationContext;

    private static final String SUBFIX_TEST = "Tests";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Before
    public void setup() {
        String actualBeanName = this.getClass().getSimpleName();
        actualBeanName = toLowerCaseFirstOne(actualBeanName);
        Object controller = applicationContext.getBean(actualBeanName.replaceAll(SUBFIX_TEST, ""));
        super.setup(controller);
    }

    /**
     * 首字母转小写
     * 首字母转小写
     * @param s
     * @author littlehui
     * @date 2021/11/15 17:20
     * @return java.lang.String
     */
    public String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 首字母转大写
     * @param s
     * @author littlehui
     * @date 2021/11/15 17:07
     * @return java.lang.String
     */
    public String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

}
