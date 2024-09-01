package org.tinycode.pay.apple.command;

import lombok.Builder;
import lombok.Data;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 16:38
 */
@Data
@Builder
public class TokenPayLoad {

    private String iss;

    private Long iat;

    private Long exp;

    private String aud;

    private String bid;

}
