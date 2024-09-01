package org.tinycode.pay.apple.command;

import lombok.Data;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 16:41
 */
@Data
public class TokenHeader {

    private String alg;

    private String kid;

    private String typ;
}
