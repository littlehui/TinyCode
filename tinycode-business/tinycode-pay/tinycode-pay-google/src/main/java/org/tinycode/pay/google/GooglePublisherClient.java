package org.tinycode.pay.google;

import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.google.api.services.androidpublisher.model.VoidedPurchase;
import org.tinycode.pay.google.exception.CallbackException;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/12/15 9:58
 */
public interface GooglePublisherClient {

    /**
     * 配置google使用的代理
     *
     * @param proxyUrl
     * @param proxyPort
     * @return void
     * @description setProxy
     * @author littlehui
     * @date 2023/1/10 11:09
     */
    public void setProxy(String proxyUrl, String proxyPort);

    /**
     * 获取已经购买的订单商品
     *
     * @param keyJson
     * @param packageName
     * @param purchaseToken
     * @param productId
     * @return com.google.api.services.androidpublisher.model.ProductPurchase
     * @description googlePurchase
     * @author littlehui
     * @date 2022/12/16 10:32
     */
    public ProductPurchase productPurchase(String keyJson, String packageName, String purchaseToken, String productId);

    /**
     * 获取已经取消的订单列表
     *
     * @param keyJson
     * @param startTimeMills
     * @param endTimeMills
     * @return java.util.List<com.google.api.services.androidpublisher.model.VoidedPurchase>
     * @Param packageName
     * @description googleVoidedPurchases
     * @author littlehui
     * @date 2022/12/16 10:33
     */
    public List<VoidedPurchase> voidedPurchases(String keyJson, String packageName, Long startTimeMills, Long endTimeMills);

    /**
     * 商品确认
     *
     * @param keyJson
     * @param packageName
     * @param productId
     * @param purchaseToken
     * @return java.lang.Boolean
     * @description productAcknowledge
     * @author littlehui
     * @date 2023/1/11 18:46
     */
    public Future<Boolean> productAcknowledge(String keyJson, String packageName, String productId, String purchaseToken) throws CallbackException;

    /**
     * 延迟确认
     * @description productDelayAcknowledge
     * @param keyJson
     * @param packageName
     * @param productId
     * @param purchaseToken
     * @author littlehui
     * @date 2023/1/13 18:13
     * @return void
     */
    public void productDelayAcknowledge(String keyJson, String packageName, String productId, String purchaseToken);

    /**
     * googletoken获取
     * @description takeToken
     * @param serviceAccountBase64
     * @author littlehui
     * @date 2023/1/17 12:07
     * @return String
     */
    public String takeToken(String serviceAccountBase64);
}
