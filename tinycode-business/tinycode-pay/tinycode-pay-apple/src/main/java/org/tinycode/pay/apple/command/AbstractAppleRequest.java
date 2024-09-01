package org.tinycode.pay.apple.command;

import com.google.gson.reflect.TypeToken;
import org.tinycode.pay.apple.exception.AppleServerRequestException;
import org.tinycode.pay.apple.utils.AppleOkHttpUtils;
import org.tinycode.pay.apple.utils.GsonUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2023/8/8 13:56
 */
@Slf4j
public abstract class AbstractAppleRequest<R> extends AppleRequest {

    @Setter
    private TypeToken<R> responseTypeToken = null;

    @Setter
    private List<String> proxyUrls;

    public AbstractAppleRequest(String url) {
        super(url);
    }

    public AbstractAppleRequest(String url, TypeToken<R> responseTypeToken) {
        super(url);
        this.responseTypeToken = responseTypeToken;
    }

    @Override
    protected R doRequest() throws AppleServerRequestException {
        if (responseTypeToken == null) {
            throw new AppleServerRequestException("responseTypeToken未配置", -1);
        }
        if (proxyUrls != null && proxyUrls.size() > 0) {
            return doRequestWithProxy();
        } else {
            return doCommonRequest();
        }
    }

    public R doRequestWithProxy() throws AppleServerRequestException {
        String result = null;
        for (int i = 0; i < proxyUrls.size(); i++) {
            String currentProxyUrl = proxyUrls.get(0);
            try {
                log.info("AppleServer requestInfo:{}, {}, {}", getRequestUrl(), GsonUtil.toJson(getUrlParams()), GsonUtil.toJson(getHeaders()));
                Map<String, Object> paramMap = getUrlParams();
                result = AppleOkHttpUtils.getUrlWithParamsProxy(getRequestUrl(), paramMap, getHeaders(), currentProxyUrl);
                return GsonUtil.fromJson(result, responseTypeToken);
            } catch (Exception e) {
                String errorMsg = "请求苹果服务器失败：url: " + getRequestUrl() + ", headers:" + getHeaders() + "currentProxyUrl:  " + currentProxyUrl;
                log.error(errorMsg + "result:{}", result);
                if (i == proxyUrls.size() - 1) {
                    log.error("通过代理请求苹果服务器失败：url: {}, headers:{}, 代理详情：{}", getRequestUrl(), getHeaders(), GsonUtil.toJson(proxyUrls));
                    throw new AppleServerRequestException(e);
                }
            }
        }
        return null;
    }

    public R doCommonRequest() throws AppleServerRequestException {
        String result = null;
        for (int i = 0; i < 3; i++) {
            try {
                log.info("AppleServer requestInfo:{}, {}, {}", getRequestUrl(), GsonUtil.toJson(getUrlParams()), GsonUtil.toJson(getHeaders()));
                Map<String, Object> paramMap = getUrlParams();
                result = AppleOkHttpUtils.getUrlWithParams(getRequestUrl(), paramMap, getHeaders());
                return GsonUtil.fromJson(result, responseTypeToken);
            } catch (Exception e) {
                String errorMsg = "请求苹果服务器失败：url: " + getRequestUrl() + ", headers:" + getHeaders();
                log.error(errorMsg + "result:{}", result);
                if (i == 2) {
                    throw new AppleServerRequestException(e);
                }
            }
        }
        return null;
    }
}
