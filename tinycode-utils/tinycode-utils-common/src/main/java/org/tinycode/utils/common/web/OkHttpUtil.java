package org.tinycode.utils.common.web;

import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 请求工具封装
 * @author littlehui
 * @date 2021/11/15 14:43
 * @version 1.0
 */
public class OkHttpUtil {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    public static final MediaType X_FORM_VALUE
            = MediaType.get("application/x-www-form-urlencoded");

    private static String userAgent = "";

    private static Long TIMEOUT = 3L;


    /**
     *
     */
    private static OkHttpClient clientWithProxy = new OkHttpClient.Builder()
            //是否开启缓存
            .retryOnConnectionFailure(true)
            .connectionPool(new ConnectionPool(200, 5, TimeUnit.MINUTES))
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    private static OkHttpClient client = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectionPool(new ConnectionPool(200, 5, TimeUnit.MINUTES))
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    public static String postWithProxy(String url, String json, String proxyHost, int port) throws IOException {
        Request request = initRequest(url,json);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, port));
        clientWithProxy = clientWithProxy.newBuilder().proxy(proxy).build();
        try (Response response = clientWithProxy.newCall(request).execute()) {
            if (response == null || response.body() == null) {
                return "";
            }
            return response.body().string();
        }
    }

    public static String post(String url, String json) throws IOException {
        Request request = initRequest(url,json);
        try (Response response = client.newCall(request).execute()) {
            if (response == null || response.body() == null) {
                return "";
            }
            return response.body().string();
        }
    }

    public static Response postForResponse(String url, String json) throws IOException {
        Request request = initRequest(url,json);
        Response response = client.newCall(request).execute();
        return response;
    }

    private static Request initRequest(String url, String json) {
        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder builder = new Request.Builder().addHeader("Connection","close");
        builder.url(url).post(body);
        if (userAgent != null) {
            builder.addHeader("User-Agent", userAgent);
        }
        Request request = builder.build();
        return request;
    }

    private static Request initGetRequest(String url, String cookies) {
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .get();
        if (userAgent != null) {
            builder.addHeader("User-Agent", userAgent);
        }
        if (cookies != null) {
            builder.addHeader("Cookie", cookies);
        }
        builder.addHeader("Connection", "close");
        Request request = builder.build();
        return request;
    }

    public static String getWithProxy(String getUrl, String proxyHost, int port,String userAgent) throws IOException {
        Request request = initGetRequest(getUrl, null);
        if (userAgent != null) {
            request.newBuilder().addHeader("User-Agent", userAgent);
        }
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, port));
        clientWithProxy = clientWithProxy.newBuilder().proxy(proxy).build();
        try (Response response = clientWithProxy.newCall(request).execute()) {
            if (response == null || response.body() == null) {
                return "";
            }
            return response.body().string();
        }
    }
    public static String get(String getUrl, String cookies) throws IOException {
        Request request = initGetRequest(getUrl, cookies);
        try (Response response = client.newCall(request).execute()) {
            if (response == null || response.body() == null) {
                return "";
            }
            return response.body().string();
        }
    }

    public static String get(String getUrl, Map<String, Object> params, String cookies) throws IOException {
        Request request = initGetRequest(getUrl, cookies);
        try (Response response = client.newCall(request).execute()) {
            if (response == null || response.body() == null) {
                return "";
            }
            return response.body().string();
        }
    }

    public static Response postForResponse(String url, String json, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(body);
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                if (headers.get(key) != null) {
                    builder.addHeader(key, headers.get(key));
                }
            }
        }
        Request request = builder.build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static void main1(String[] args) {
        String signUrl = "http://api-pay.test.tlsy.cy.com/v2/notice/app?bpOrderId=1001201905291735314356&commodityTotalPrice=4259.0&environment=Sandbox&notifyId=64e5aed2fc2b47189da95dc553e8229c&notifyType=asynNotify&orderCode=1001201905291735314356&orderSerialNumber=2019052972380815673188352&payAccount=null&payBank=APPLE&payChannel=APPLE&payCode=2019052972380815673188352&payPrice=4259.0&payTime=1559122550798&payType=APPLE&receiptType=ProductionSandbox&refundPrice=4259.0&totalPrice=4259.00&tradeStatus=success&unid=1190&sign=d9d95d02055f5f204e0168f945be1136a7ca1984bc120ead8ed76fa05c02de87";
        String payVersion = "pay_version";
        Map<String, String> headers = new HashMap<>(10);
        headers.put("User-Agent", payVersion);
        headers.put("X-Tr-Request-Id", "testTraceId");
        headers.put("X-Tr-Rpc-Id", "testRpcid");
        try {
            Response result = OkHttpUtil.postForResponse(signUrl, "", headers);
            System.out.println(result.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main2(String[] args) throws IOException {
        String signUrl = "http://paylocal.17173.com/pp-service-impl/front/apple/verify.do?orderSerialNumber=2019050563568719497977856&transactionReceipt=MIIT7wYJKoZIhvcNAQcCoIIT4DCCE9wCAQExCzAJBgUrDgMCGgUAMIIDkAYJKoZIhvcNAQcBoIIDgQSCA30xggN5MAoCAQgCAQEEAhYAMAoCARQCAQEEAgwAMAsCAQECAQEEAwIBADALAgELAgEBBAMCAQAwCwIBDwIBAQQDAgEAMAsCARACAQEEAwIBADALAgEZAgEBBAMCAQMwDAIBCgIBAQQEFgI0KzAMAgEOAgEBBAQCAgC5MA0CAQMCAQEEBQwDMS4wMA0CAQ0CAQEEBQIDAdUoMA0CARMCAQEEBQwDMS4wMA4CAQkCAQEEBgIEUDI1MDAYAgEEAgECBBBlp0Ekh7QMkcPaAmg6H8v4MBsCAQACAQEEEwwRUHJvZHVjdGlvblNhbmRib3gwHAIBBQIBAQQUbws6ScsndXi5tTl6vyJyEN%2bcPYkwHgIBDAIBAQQWFhQyMDE5LTAzLTEzVDAyOjQxOjAzWjAeAgESAgEBBBYWFDIwMTMtMDgtMDFUMDc6MDA6MDBaMCMCAQICAQEEGwwZY29tLmN5b3Uuamlhb2ppYW8uaW9zYXBwMTBRAgEHAgEBBEkvB5HTy6thRzSFHKf1r9K1Nhe3cJENSK5LLGAnY384I%2b4J9OGLsb25eTcGKx%2fyi0ggo7Oyk2KvKcODpci6kdXnYvcbp21Xba%2bAMFwCAQYCAQEEVIatv4ba6%2fM59YzUzPS%2b4MsJoQOyH6s85UNkVHq3vTG4E%2fVvaq3CKq0GXqSEWVm64kxCTuk0KUMPoNppz0f8mn5jYilTHbeP6VhjON4jjEXfNSrjvzCCAVgCARECAQEEggFOMYIBSjALAgIGrAIBAQQCFgAwCwICBq0CAQEEAgwAMAsCAgawAgEBBAIWADALAgIGsgIBAQQCDAAwCwICBrMCAQEEAgwAMAsCAga0AgEBBAIMADALAgIGtQIBAQQCDAAwCwICBrYCAQEEAgwAMAwCAgalAgEBBAMCAQEwDAICBqsCAQEEAwIBATAMAgIGrgIBAQQDAgEAMAwCAgavAgEBBAMCAQAwDAICBrECAQEEAwIBADAbAgIGpwIBAQQSDBAxMDAwMDAwNTA5ODExMDgxMBsCAgapAgEBBBIMEDEwMDAwMDA1MDk4MTEwODEwHgICBqYCAQEEFQwTY29tLmN5b3Uuc2hvdWxpdWRhbjAfAgIGqAIBAQQWFhQyMDE5LTAzLTEzVDAyOjQxOjAzWjAfAgIGqgIBAQQWFhQyMDE5LTAzLTEzVDAyOjQxOjAzWqCCDmUwggV8MIIEZKADAgECAggO61eH554JjTANBgkqhkiG9w0BAQUFADCBljELMAkGA1UEBhMCVVMxEzARBgNVBAoMCkFwcGxlIEluYy4xLDAqBgNVBAsMI0FwcGxlIFdvcmxkd2lkZSBEZXZlbG9wZXIgUmVsYXRpb25zMUQwQgYDVQQDDDtBcHBsZSBXb3JsZHdpZGUgRGV2ZWxvcGVyIFJlbGF0aW9ucyBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTAeFw0xNTExMTMwMjE1MDlaFw0yMzAyMDcyMTQ4NDdaMIGJMTcwNQYDVQQDDC5NYWMgQXBwIFN0b3JlIGFuZCBpVHVuZXMgU3RvcmUgUmVjZWlwdCBTaWduaW5nMSwwKgYDVQQLDCNBcHBsZSBXb3JsZHdpZGUgRGV2ZWxvcGVyIFJlbGF0aW9uczETMBEGA1UECgwKQXBwbGUgSW5jLjELMAkGA1UEBhMCVVMwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQClz4H9JaKBW9aH7SPaMxyO4iPApcQmyz3Gn%2bxKDVWG%2f6QC15fKOVRtfX%2byVBidxCxScY5ke4LOibpJ1gjltIhxzz9bRi7GxB24A6lYogQ%2bIXjV27fQjhKNg0xbKmg3k8LyvR7E0qEMSlhSqxLj7d0fmBWQNS3CzBLKjUiB91h4VGvojDE2H0oGDEdU8zeQuLKSiX1fpIVK4cCc4Lqku4KXY%2fQrk8H9Pm%2fKwfU8qY9SGsAlCnYO3v6Z%2fv%2fCa%2fVbXqxzUUkIVonMQ5DMjoEC0KCXtlyxoWlph5AQaCYmObgdEHOwCl3Fc9DfdjvYLdmIHuPsB8%2fijtDT%2biZVge%2fiA0kjAgMBAAGjggHXMIIB0zA%2fBggrBgEFBQcBAQQzMDEwLwYIKwYBBQUHMAGGI2h0dHA6Ly9vY3NwLmFwcGxlLmNvbS9vY3NwMDMtd3dkcjA0MB0GA1UdDgQWBBSRpJz8xHa3n6CK9E31jzZd7SsEhTAMBgNVHRMBAf8EAjAAMB8GA1UdIwQYMBaAFIgnFwmpthhgi%2bzruvZHWcVSVKO3MIIBHgYDVR0gBIIBFTCCAREwggENBgoqhkiG92NkBQYBMIH%2bMIHDBggrBgEFBQcCAjCBtgyBs1JlbGlhbmNlIG9uIHRoaXMgY2VydGlmaWNhdGUgYnkgYW55IHBhcnR5IGFzc3VtZXMgYWNjZXB0YW5jZSBvZiB0aGUgdGhlbiBhcHBsaWNhYmxlIHN0YW5kYXJkIHRlcm1zIGFuZCBjb25kaXRpb25zIG9mIHVzZSwgY2VydGlmaWNhdGUgcG9saWN5IGFuZCBjZXJ0aWZpY2F0aW9uIHByYWN0aWNlIHN0YXRlbWVudHMuMDYGCCsGAQUFBwIBFipodHRwOi8vd3d3LmFwcGxlLmNvbS9jZXJ0aWZpY2F0ZWF1dGhvcml0eS8wDgYDVR0PAQH%2fBAQDAgeAMBAGCiqGSIb3Y2QGCwEEAgUAMA0GCSqGSIb3DQEBBQUAA4IBAQANphvTLj3jWysHbkKWbNPojEMwgl%2fgXNGNvr0PvRr8JZLbjIXDgFnf4%2bLXLgUUrA3btrj%2b%2fDUufMutF2uOfx%2fkd7mxZ5W0E16mGYZ2%2bFogledjjA9z%2fOjtxh%2bumfhlSFyg4Cg6wBA3LbmgBDkfc7nIBf3y3n8aKipuKwH8oCBc2et9J6Yz%2bPWY4L5E27FMZ%2fxuCk%2fJ4gao0pfzp45rUaJahHVl0RYEYuPBX%2fUIqc9o2ZIAycGMs%2fiNAGS6WGDAfK%2bPdcppuVsq1h1obphC9UynNxmbzDscehlD86Ntv0hgBgw2kivs3hi1EdotI9CO%2fKBpnBcbnoB7OUdFMGEvxxOoMIIEIjCCAwqgAwIBAgIIAd68xDltoBAwDQYJKoZIhvcNAQEFBQAwYjELMAkGA1UEBhMCVVMxEzARBgNVBAoTCkFwcGxlIEluYy4xJjAkBgNVBAsTHUFwcGxlIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MRYwFAYDVQQDEw1BcHBsZSBSb290IENBMB4XDTEzMDIwNzIxNDg0N1oXDTIzMDIwNzIxNDg0N1owgZYxCzAJBgNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSwwKgYDVQQLDCNBcHBsZSBXb3JsZHdpZGUgRGV2ZWxvcGVyIFJlbGF0aW9uczFEMEIGA1UEAww7QXBwbGUgV29ybGR3aWRlIERldmVsb3BlciBSZWxhdGlvbnMgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDKOFSmy1aqyCQ5SOmM7uxfuH8mkbw0U3rOfGOAYXdkXqUHI7Y5%2flAtFVZYcC1%2bxG7BSoU%2bL%2fDehBqhV8mvexj%2favoVEkkVCBmsqtsqMu2WY2hSFT2Miuy%2faxiV4AOsAX2XBWfODoWVN2rtCbauZ81RZJ%2fGXNG8V25nNYB2NqSHgW44j9grFU57Jdhav06DwY3Sk9UacbVgnJ0zTlX5ElgMhrgWDcHld0WNUEi6Ky3klIXh6MSdxmilsKP8Z35wugJZS3dCkTm59c3hTO%2fAO0iMpuUhXf1qarunFjVg0uat80YpyejDi%2bl5wGphZxWy8P3laLxiX27Pmd3vG2P%2bkmWrAgMBAAGjgaYwgaMwHQYDVR0OBBYEFIgnFwmpthhgi%2bzruvZHWcVSVKO3MA8GA1UdEwEB%2fwQFMAMBAf8wHwYDVR0jBBgwFoAUK9BpR5R2Cf70a40uQKb3R01%2fCF4wLgYDVR0fBCcwJTAjoCGgH4YdaHR0cDovL2NybC5hcHBsZS5jb20vcm9vdC5jcmwwDgYDVR0PAQH%2fBAQDAgGGMBAGCiqGSIb3Y2QGAgEEAgUAMA0GCSqGSIb3DQEBBQUAA4IBAQBPz%2b9Zviz1smwvj%2b4ThzLoBTWobot9yWkMudkXvHcs1Gfi%2fZptOllc34MBvbKuKmFysa%2fNw0Uwj6ODDc4dR7Txk4qjdJukw5hyhzs%2br0ULklS5MruQGFNrCk4QttkdUGwhgAqJTleMa1s8Pab93vcNIx0LSiaHP7qRkkykGRIZbVf1eliHe2iK5IaMSuviSRSqpd1VAKmuu0swruGgsbwpgOYJd%2bW%2bNKIByn%2fc4grmO7i77LpilfMFY0GCzQ87HUyVpNur%2bcmV6U%2fkTecmmYHpvPm0KdIBembhLoz2IYrF%2bHjhga6%2f05Cdqa3zr%2f04GpZnMBxRpVzscYqCtGwPDBUfMIIEuzCCA6OgAwIBAgIBAjANBgkqhkiG9w0BAQUFADBiMQswCQYDVQQGEwJVUzETMBEGA1UEChMKQXBwbGUgSW5jLjEmMCQGA1UECxMdQXBwbGUgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkxFjAUBgNVBAMTDUFwcGxlIFJvb3QgQ0EwHhcNMDYwNDI1MjE0MDM2WhcNMzUwMjA5MjE0MDM2WjBiMQswCQYDVQQGEwJVUzETMBEGA1UEChMKQXBwbGUgSW5jLjEmMCQGA1UECxMdQXBwbGUgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkxFjAUBgNVBAMTDUFwcGxlIFJvb3QgQ0EwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDkkakJH5HbHkdQ6wXtXnmELes2oldMVeyLGYne%2bUts9QerIjAC6Bg%2b%2bFAJ039BqJj50cpmnCRrEdCju%2bQbKsMflZ56DKRHi1vUFjczy8QPTc4UadHJGXL1XQ7Vf1%2bb8iUDulWPTV0N8WQ1IxVLFVkds5T39pyez1C6wVhQZ48ItCD3y6wsIG9wtj8BMIy3Q88PnT3zK0koGsj%2bzrW5DtleHNbLPbU6rfQPDgCSC7EhFi501TwN22IWq6NxkkdTVcGvL0Gz%2bPvjcM3mo0xFfh9Ma1CWQYnEdGILEINBhzOKgbEwWOxaBDKMaLOPHd5lc%2f9nXmW8Sdh2nzMUZaF3lMktAgMBAAGjggF6MIIBdjAOBgNVHQ8BAf8EBAMCAQYwDwYDVR0TAQH%2fBAUwAwEB%2fzAdBgNVHQ4EFgQUK9BpR5R2Cf70a40uQKb3R01%2fCF4wHwYDVR0jBBgwFoAUK9BpR5R2Cf70a40uQKb3R01%2fCF4wggERBgNVHSAEggEIMIIBBDCCAQAGCSqGSIb3Y2QFATCB8jAqBggrBgEFBQcCARYeaHR0cHM6Ly93d3cuYXBwbGUuY29tL2FwcGxlY2EvMIHDBggrBgEFBQcCAjCBthqBs1JlbGlhbmNlIG9uIHRoaXMgY2VydGlmaWNhdGUgYnkgYW55IHBhcnR5IGFzc3VtZXMgYWNjZXB0YW5jZSBvZiB0aGUgdGhlbiBhcHBsaWNhYmxlIHN0YW5kYXJkIHRlcm1zIGFuZCBjb25kaXRpb25zIG9mIHVzZSwgY2VydGlmaWNhdGUgcG9saWN5IGFuZCBjZXJ0aWZpY2F0aW9uIHByYWN0aWNlIHN0YXRlbWVudHMuMA0GCSqGSIb3DQEBBQUAA4IBAQBcNplMLXi37Yyb3PN3m%2fJ20ncwT8EfhYOFG5k9RzfyqZtAjizUsZAS2L70c5vu0mQPy3lPNNiiPvl4%2f2vIB%2bx9OYOLUyDTOMSxv5pPCmv%2fK%2fxZpwUJfBdAVhEedNO3iyM7R6PVbyTi69G3cN8PReEnyvFteO3ntRcXqNx%2bIjXKJdXZD9Zr1KIkIxH3oayPc4FgxhtbCS%2bSsvhESPBgOJ4V9T0mZyCKM2r3DYLP3uujL%2flTaltkwGMzd%2fc6ByxW69oPIQ7aunMZT7XZNn%2fBh1XZp5m5MkL72NVxnn6hUrcbvZNCJBIqxw8dtk2cXmPIS4AXUKqK1drk%2fNAJBzewdXUhMYIByzCCAccCAQEwgaMwgZYxCzAJBgNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSwwKgYDVQQLDCNBcHBsZSBXb3JsZHdpZGUgRGV2ZWxvcGVyIFJlbGF0aW9uczFEMEIGA1UEAww7QXBwbGUgV29ybGR3aWRlIERldmVsb3BlciBSZWxhdGlvbnMgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkCCA7rV4fnngmNMAkGBSsOAwIaBQAwDQYJKoZIhvcNAQEBBQAEggEAjm05U3spTgTXUdxhyCaFTuiAdWRtIZ8E0RvFTf0Nbtr6GDoCAdtYHIgXG71Nl5c1FdKn7yQj4Lmwglk%2baHBNJwU%2fH82wX5jHU0ltz4xS4JOrWQXNljjCyK2PbqEDqUadX52Q7Zb8WXiE%2bRYHlCBRGBzrjpZSWerBKUZknay54q%2fp4gpu6b3dAEnRgAZAvF8bTYlmj72g8r7REo7MChC%2bzHWBGGBevqkQOb0nYJ7MxK172utzbyUoFEs9g9KbR9N8DZuBA0bWtb9UnZHIWJV%2b6Zdaq4RbSST7uJ1CpTTho6tqFHXWJpDkk40lDEvK6GfyWwylYwhArFiSIZzdayUvdg%3d%3d&sign=19e7ed6d447ee8658d928a859e74b8ce&bpOrderId=-11361682&bpId=10079";
        String result = OkHttpUtil.post(signUrl, "");
        System.out.println(result);
    }

    public static Response postFormValueEncoded(String url, Map<String, String> params,String auth) {
        StringBuffer bodyBuffer = new StringBuffer();
        boolean firstFlag = true;
        if (params != null) {
            for (String key : params.keySet()) {
                if (params.get(key) == null) {
                    continue;
                }
                if (!firstFlag) {
                    bodyBuffer.append("&");
                }
                bodyBuffer.append(key).append("=").append(params.get(key));
                firstFlag = false;
            }
        }
        RequestBody body = RequestBody.create(bodyBuffer.toString(), X_FORM_VALUE);
        Request.Builder builder = new Request.Builder()
                .addHeader("Connection","close")
                .addHeader("Authorization", "Bearer " + auth);
        builder.url(url).post(body);
        Request request = builder.build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
