package org.tinycode.pay.apple.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/8/2 15:52
 */
@Slf4j
public class AppleOkHttpUtils {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    static Long TIMEOUT = 15L;

    private static OkHttpClient client = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)//是否开启缓存
            .connectionPool(new ConnectionPool(200, 5, TimeUnit.MINUTES))//连接池
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    private static OkHttpClient clientWithProxy = new OkHttpClient.Builder()
            .retryOnConnectionFailure(false)//是否开启缓存
            .connectionPool(new ConnectionPool(200, 5, TimeUnit.MINUTES))//连接池
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    public static String post(String url, String json) throws IOException {
        return postJson(url, json, null);
    }

    public static String postJson(String url, String json, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder builder = new Request.Builder().addHeader("Connection","close");
        if (headers != null && headers.size() > 0) {
            for (String headerKey : headers.keySet()) {
                builder.addHeader(headerKey, headers.get(headerKey));
            }
        }
        builder.url(url).post(body);
        Request request = builder.build();
        try (Response response = client.newCall(request).execute()) {
            if (response == null || response.body() == null) {
                return "";
            }
            return response.body().string();
        }
    }


    public static String get(String getUrl) throws IOException {
        return get(getUrl, null);
    }

    public static String get(String getUrl, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder();
        builder.url(getUrl).get();
        if (headers != null && headers.size() > 0) {
            for (String headerKey : headers.keySet()) {
                if (headers.get(headerKey) != null) {
                    builder.addHeader(headerKey, headers.get(headerKey));
                }
            }
        }
        builder.addHeader("Connection", "close");
        Request request = builder.build();
        try (Response response = client.newCall(request).execute()) {
            if (response == null || response.body() == null) {
                return null;
            }
            return response.body().string();
        }
    }

    public static String getUrlWithParams(String getUrl, Map<String, Object> params, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            for (String headerKey : headers.keySet()) {
                if (headers.get(headerKey) != null) {
                    builder.addHeader(headerKey, headers.get(headerKey));
                }
            }
        }
        builder.url(attachUrlParam(getUrl, params)).get();
        builder.addHeader("Connection", "close");
        Request request = builder.build();
        try (Response response = client.newCall(request).execute()) {
            if (response == null || response.body() == null) {
                return null;
            }
            return response.body().string();
        }
    }

    public static String getUrlWithParamsProxy(String getUrl, Map<String, Object> params, Map<String, String> headers, String proxyUrl) throws IOException {
        Request.Builder builder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            for (String headerKey : headers.keySet()) {
                if (headers.get(headerKey) != null) {
                    builder.addHeader(headerKey, headers.get(headerKey));
                }
            }
        }
        String[] proxyHostPort = proxyUrl.split(":");
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHostPort[0], Integer.parseInt(proxyHostPort[1])));
        clientWithProxy = clientWithProxy.newBuilder().proxy(proxy).build();
        builder.url(attachUrlParam(getUrl, params)).get();
        builder.addHeader("Connection", "close");
        Request request = builder.build();
        try (Response response = clientWithProxy.newCall(request).execute()) {
            if (response == null || response.body() == null) {
                return null;
            }
            return response.body().string();
        }
    }

    private static String attachUrlParam(String getUrl, Map<String, Object>  params) {
        StringBuffer urlParamBuffer = new StringBuffer();
        if (params != null && params.size() > 0) {
            int i = 0;
            for (String key : params.keySet()) {
                if (params.get(key) != null) {
                    if (i == 0) {
                        if (!getUrl.contains("?")) {
                            urlParamBuffer.append("?");
                        }
                    } else {
                        urlParamBuffer.append("&");
                    }
                    urlParamBuffer.append(key).append("=").append(params.get(key));
                }
                i++;
            }
        }
        return getUrl + urlParamBuffer.toString();
    }

    public static String postFormData(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        FormBody.Builder formBodyBuilder = buildFormRequestBody(params, null);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(formBodyBuilder.build());
        if (headers != null && headers.size() > 0) {
            for (String headerKey : headers.keySet()) {
                requestBuilder.addHeader(headerKey, headers.get(headerKey));
            }
        }
        requestBuilder.addHeader("Content-type", "application/x-www-form-urlencoded");
        Request request = requestBuilder.build();
        try (Response response = client.newCall(request).execute()) {
            if (response == null || response.body() == null) {
                return "";
            }
            return response.body().string();
        }
    }

    private static FormBody.Builder buildFormRequestBody(Map<String, Object> req, String[] skipBuilds) throws Exception {
        Map<String, Object> params = buildRequestParams(req, skipBuilds);
        FormBody.Builder builder = new FormBody.Builder();
        params.forEach((k, v) -> builder.add(k, String.valueOf(v)));
        return builder;
    }

    private static Map<String, Object> buildRequestParams(Map<String, Object> params, String[] skipBuilds) throws Exception {
        if (skipBuilds != null) {
            for (String skipField : skipBuilds) {
                params.remove(skipField);
            }
        }
        return params;
    }
}
