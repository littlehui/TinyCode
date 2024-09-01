package org.tinycode.web.mock.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.tinycode.utils.common.obj.ObjectUtil;

import javax.print.attribute.standard.Media;
import java.util.Map;

/**
 * TODO
 *
 * @author littlehui
 * @version 1.0
 * @date 2022/04/19 21:31
 */
@Slf4j
public class ControllerExecuteTest extends ControllerLogTest implements ControllerBaseTest {

    protected MockMvc mockMvc;

    @Override
    public void setup(Object singleTon) {
        initMockMvc(singleTon);
    }

    protected void initMockMvc(Object singleTon) {
        mockMvc = MockMvcBuilders.standaloneSetup(singleTon).build();
    }

    @Override
    public void post(Object param, String url) throws Exception {
        RequestBuilder requestBuilder = initRequestBuilder(param, url, null, MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();;
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
        MvcResult mvcResult = mockMvc.perform(initRequestBuilder(param, url, null, MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Override
    public void get(Object param, String url, Map<String, String> header) throws Exception {
        RequestBuilder requestBuilder = initRequestBuilder(param, url, header, MediaType.APPLICATION_JSON);
        ResultActions resultActions = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        MvcResult mvcResult = resultActions.andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Override
    public void get(Object param, String url) throws Exception {
        RequestBuilder requestBuilder = initRequestBuilder(param, url, null, MediaType.APPLICATION_JSON);
        ResultActions resultActions = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        MvcResult mvcResult = resultActions.andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Override
    public void delete(Object param, String url) throws Exception {
        RequestBuilder requestBuilder = initRequestBuilder(param, url, null, MediaType.APPLICATION_JSON);
        ResultActions resultActions = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        MvcResult mvcResult = resultActions.andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    protected RequestBuilder initRequestBuilder(Object param, String url, Map<String, String> headers, MediaType mediaType) {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        if (param != null && mediaType == MediaType.APPLICATION_JSON) {
            requestBuilder.content(JSONObject.toJSONString(param));
        }
        if (param != null && mediaType == MediaType.APPLICATION_FORM_URLENCODED) {
            //requestBuilder.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            if (param instanceof Map) {
                for (Object key : ((Map)param).keySet()) {
                    String value = ((Map)param).get(key) + "";
                    if (value != null && !"".equals(value) && !"null".equals(value)) {
                        requestBuilder.param(key + "", ((Map)param).get(key) + "");
                    }
                }
            } else {
                Map<String, Object> paramMap = ObjectUtil.toMap(param);
                for (String key : paramMap.keySet()) {
                    String value = paramMap.get(key) + "";
                    if (value != null && !"".equals(value) && !"null".equals(value)) {
                        requestBuilder.param(key, paramMap.get(key) + "");
                    }
                }
            }
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
}
