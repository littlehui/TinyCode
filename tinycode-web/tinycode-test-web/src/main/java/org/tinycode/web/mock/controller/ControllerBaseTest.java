package org.tinycode.web.mock.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

/**
 * TODO
 * @author littlehui
 * @date 2021/11/15 17:02
 * @version 1.0
 */
public interface ControllerBaseTest {

    /**
     * 初始化启动注入要测试的controller单例
     * @param singleTon
     * @author littlehui
     * @date 2021/11/15 17:21
     * @return void
     */
    public void setup(Object singleTon);

    /**
     * 测试post请求
     * @param param
     * @param url
     * @author littlehui
     * @date 2021/11/15 17:22
     * @return void
     * @throws Exception
     */
    public void post(Object param, String url) throws Exception;

    /**
     * 测试post请求包含header
     * @param param
     * @param url
     * @param header
     * @author littlehui
     * @date 2021/11/15 17:22
     * @return void
     * @throws Exception
     */
    public void post(Object param, String url, Map<String, String> header) throws Exception;

    /**
     * 测试的get请求包含header
     * @param param
     * @param url
     * @param header
     * @author littlehui
     * @date 2021/11/15 17:22
     * @return void
     * @throws Exception
     */
    public void get(Object param, String url, Map<String, String> header) throws Exception;

    /**
     * 测试的get请求
     * @param param
     * @param url
     * @author littlehui
     * @date 2021/11/15 17:22
     * @return void
     * @throws Exception
     */
    public void get(Object param, String url) throws Exception;

    /**
     * 测试delete请求
     * @param param
     * @param url
     * @author littlehui
     * @date 2021/11/15 17:23
     * @return void
     * @throws Exception
     */
    public void delete(Object param, String url) throws Exception;
}
