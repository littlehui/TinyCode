package org.tinycode.web.mock.controller;

import org.junit.Before;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * 此类作为测试类的基类
 * 假设要测试的controller类是DemoMockController 那么新建一个类DemoMockControllerTests，继承 ControllerAwareJwtTest 代码如下：
 * <p>@RunWith(SpringRunner.class)
 * <p>@SpringBootTest(class = Application.class)
 * <p>@WebAppConfiguration
 * <p>@ActiveProfiles("local")
 * <p>public class DemoMockControllerTests extends ControllerAwareJwtTest {
 * <p>
 * <p>  //项目中用来处理jwtToken的解释器
 * <p>    @Autowire
 * <p>    private HandlerInterceptor jwtInterceptor;
 * <p>
 * <p>    //测试用jwtToken
 * <p>    private String jwtToken;
 * <p>
 * <p>    @PostConstruct
 * <p>    public void initJwtTokenAndInterceptor() {
 * <p>        //设置Token
 * <p>        setJwtToken(jwtToken);
 * <p>        //设置token解释器
 * <p>        setInterceptor(jwtInterceptor);
 * <p>    }
 *
 * <p>    @Test
 * <p>    public void test1() {
 * <p>         post(null, "/admin/get?mockId=11", "xxxxloginToken");
 * <p>    }
 * <p>}
 * 执行test1()方法，ControllerAwareJwtTest会自动去找 DemoMockControllerTests所关联的DemoMockController类，并测试/admin/get方法。
 * @author littlehui
 * @date 2021/7/10 17:23
 * @version 1.0
 **/
public class ControllerAwareJwtTest extends ControllerJwtTest implements ApplicationContextAware {

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
