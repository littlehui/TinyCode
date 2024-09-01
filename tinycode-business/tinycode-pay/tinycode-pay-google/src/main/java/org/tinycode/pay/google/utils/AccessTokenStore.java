package org.tinycode.pay.google.utils;

/**
 * Created by littlehui on 2020/5/21.
 */
public interface AccessTokenStore {

    public String getAccessToken(AccessTokenGetter accessTokenGetter);

    public String refreshAccessToken(AccessTokenRefresher accessTokenRefresher);
}
