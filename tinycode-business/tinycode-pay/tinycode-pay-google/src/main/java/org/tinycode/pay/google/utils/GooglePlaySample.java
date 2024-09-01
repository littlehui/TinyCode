package org.tinycode.pay.google.utils;

/**
 * @Description TODO
 * @ClassName GooglePlaySample
 * @Author littlehui
 * @Date 2020/5/20 14:06
 * @Version 1.0
 **/
public class GooglePlaySample {

    public static void main(String[] args) throws Exception {
/*        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        PrivateKey privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(
                SecurityUtils.getPkcs12KeyStore(),
                new FileInputStream("{13}"),
                "notasecret", "privatekey", "notasecret"
        );
        GoogleCredential googleCredential = new GoogleCredential.Builder()
                .setTransport(transport).setJsonFactory(JacksonFactory.getDefaultInstance())
                .setServiceAccountId("{email address")
                .setServiceAccountScopes(AndroidPublisherScopes.all())
                .setServiceAccountPrivateKey(privateKey).build();

        AndroidPublisher publisher = new AndroidPublisher.Builder(transport,
                JacksonFactory.getDefaultInstance(),googleCredential).build();

        AndroidPublisher.Purchases.Products products = publisher.purchases().products();

        AndroidPublisher.Purchases.Products.Get product = products.get("packageName",
                "productId", "token");

        ProductPurchase purchase = product.execute();*/
    }
}
