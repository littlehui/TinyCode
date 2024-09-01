package org.tinycode.pay.google.utils;

/**
 * Created by littlehui on 2020/5/21.
 */
public class Auth2AccesTokenStore implements AccessTokenStore {

    @Override
    public String getAccessToken(AccessTokenGetter accessTokenGetter) {
        String accessToken = accessTokenGetter.getAccessToken();
        return accessToken;
    }


    @Override
    public synchronized String refreshAccessToken(AccessTokenRefresher accessTokenRefresher) {
        return accessTokenRefresher.refreshToken();
    }
}
