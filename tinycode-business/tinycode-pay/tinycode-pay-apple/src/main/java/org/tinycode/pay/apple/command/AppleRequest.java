package org.tinycode.pay.apple.command;

import org.tinycode.pay.apple.exception.AppleServerRequestException;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 15:32
 */
public abstract class AppleRequest {

    private static final String HEADER_ACCESS_TOKEN = "Authorization";

    @Getter
    private String accessToken;

    @Getter
    @Setter
    private String requestUrl;

    @Getter
    @Setter
    Map<String, Object> formParams = new HashMap<>();

    @Getter
    @Setter
    Map<String, Object> urlParams = new HashMap<>();

    @Getter
    private Map<String, String> headers = new HashMap<>();

    @Getter
    @Setter
    private String requestBody;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        headers.put(HEADER_ACCESS_TOKEN, "Bearer " +  accessToken);
    }

    public AppleRequest(String url) {
        this.requestUrl = url;
    }

    protected abstract <T> T doRequest() throws AppleServerRequestException;

}
