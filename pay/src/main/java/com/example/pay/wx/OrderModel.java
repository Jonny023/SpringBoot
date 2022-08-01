package com.example.pay.wx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {

    private String appid;
    private String mchid;
    private String description;
    private String out_trade_no;
    private String time_expire;
    private String attach;
    private String notify_url;
    /**
     * key -> total
     */
    private Amount amount;

    //h5需要使用
    private SceneInfo scene_info;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class Amount {
        private int total;
    }

    /**
     * DOC
     * H5需要
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SceneInfo {
        private String payer_client_ip;
        private H5Info h5_info;
    }

    /**
     * DOC
     * H5需要
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class H5Info {
        private String type;
    }
}