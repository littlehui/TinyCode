package org.tinycode.pay.alipayglobal.exception;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2024/1/24 18:00
 */
public class AlipaySdkException extends Exception {

    public AlipaySdkException(Exception e) {
        super(e);
    }

    public AlipaySdkException(String message) {
        super(message);
    }
}
