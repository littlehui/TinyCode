package org.tinycode.pay.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 16:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse implements Serializable {

    private String environment;

    private Long appAppleId;

    private String bundleId;

    private List<SubscriptionGroupIdentifierItem> data;

}
