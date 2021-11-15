package org.tinycode.web.mock.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * TODO
 * @ClassName ReaderControllerTest
 * @author littlehui
 * @date 2021/7/8 20:57
 * @version 1.0
 **/
@Slf4j
public class ControllerJwtTest extends ControllerLogTest implements ControllerBaseTest{

    private String jwtToken;

    private HandlerInterceptor handlerInterceptor;

    public void setInterceptor(HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    private static final String AUTHORIZATION = "Authorization";

    private MockMvc mockMvc;

    @Override
    public void setup(Object singleTon) {
        mockMvc = MockMvcBuilders.standaloneSetup(singleTon).addInterceptors(handlerInterceptor).build();
    }

    @Override
    public void post(Object param, String url) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .header(AUTHORIZATION, jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(param)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Override
    public void post(Object param, String url, Map<String, String> header) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url);
        if (header != null && header.size() > 0) {
            for (String key : header.keySet()) {
                requestBuilder.header(key, header.get(key));
            }
        }
        MvcResult mvcResult = mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, jwtToken)
                .content(JSONObject.toJSONString(param)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Override
    public void get(Object param, String url, Map<String, String> header) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                .header(AUTHORIZATION, jwtToken);
        requestBuilder.contentType(MediaType.APPLICATION_JSON);
        if (param != null) {
            requestBuilder.content(JSONObject.toJSONString(param));
        }
        if (header != null && header.size() > 0) {
            for (String key : header.keySet()) {
                requestBuilder.header(key, header.get(key));
            }
        }
        ResultActions resultActions = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        MvcResult mvcResult = resultActions.andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Override
    public void get(Object param, String url) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                .header(AUTHORIZATION, jwtToken);
        requestBuilder.contentType(MediaType.APPLICATION_JSON);
        if (param != null) {
            requestBuilder.content(JSONObject.toJSONString(param));
        }
        ResultActions resultActions = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        MvcResult mvcResult = resultActions.andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Override
    public void delete(Object param, String url) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url)
                .header(AUTHORIZATION, jwtToken);
        requestBuilder.contentType(MediaType.APPLICATION_JSON);
        if (param != null) {
            requestBuilder.content(JSONObject.toJSONString(param));
        }
        ResultActions resultActions = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        MvcResult mvcResult = resultActions.andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }
}
