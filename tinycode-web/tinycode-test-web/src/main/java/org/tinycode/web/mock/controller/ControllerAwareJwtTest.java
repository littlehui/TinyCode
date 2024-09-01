package org.tinycode.web.mock.controller;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

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
public class ControllerAwareJwtTest extends ControllerAwareTest implements ApplicationContextAware {

    private String jwtToken;

    private HandlerInterceptor handlerInterceptor;

    public void setInterceptor(HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    private static final String AUTHORIZATION = "Authorization";

    @Override
    public RequestBuilder initRequestBuilder(Object param, String url, Map<String, String> headers, MediaType mediaType) {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                .header(AUTHORIZATION, jwtToken);
        if (param != null) {
            requestBuilder.content(JSONObject.toJSONString(param));
        }
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                requestBuilder.header(key, headers.get(key));
            }
        }
        if (mediaType != null) {
            requestBuilder.contentType(mediaType);
        } else {
            requestBuilder.contentType(MediaType.APPLICATION_JSON);
        }
        return requestBuilder;
    }

    @Override
    protected void initMockMvc(Object singleTon) {
        mockMvc = MockMvcBuilders.standaloneSetup(singleTon).addInterceptors(handlerInterceptor).build();
    }

}
