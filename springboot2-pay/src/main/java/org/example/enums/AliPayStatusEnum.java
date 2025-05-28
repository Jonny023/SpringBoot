package org.example.enums;

import lombok.Getter;

@Getter
public enum AliPayStatusEnum {

    // WAIT_BUYER_PAY：表示等待买家付款，即买家已经创建了交易，但尚未完成付款操作。
    // WAIT_SELLER_SEND_GOODS：买家已付款，等待卖家发货。此时买家已经将货款支付到支付宝平台，资金处于冻结状态，等待卖家发货。
    // WAIT_BUYER_CONFIRM_GOODS：卖家已发货，等待买家确认收货。卖家已经完成发货操作，买家需要在收到货物后进行确认收货的操作。
    // TRADE_SUCCESS：表示交易成功，一般在买家确认收货后，支付宝会将交易状态更新为 TRADE_SUCCESS，此时货款会正式从支付宝平台转到卖家账户。在即时到账等一些支付场景中，支付成功后也会直接返回该状态。
    // TRADE_FINISHED：交易完成。该状态通常在交易成功且经过一定时间后，或者在一些特定的交易流程结束时出现，比如在涉及退款等复杂业务流程完成后也可能会进入此状态。与 TRADE_SUCCESS 的区别在于，TRADE_FINISHED 表示整个交易流程彻底结束，后续一般不会再有与该笔交易相关的主要操作。
    // TRADE_CLOSED：交易关闭。出现该状态可能是因为买家没付款系统自动关闭了交易，或者卖家手动关闭了交易，也可能是在开通高级即时到账功能且有退款操作时，人工操作退款导致交易关闭。

    WAIT_BUYER_PAY("WAIT_BUYER_PAY", "等待买家付款"),
    WAIT_SELLER_SEND_GOODS("WAIT_SELLER_SEND_GOODS", "买家已付款，等待卖家发货"),
    WAIT_BUYER_CONFIRM_GOODS("WAIT_BUYER_CONFIRM_GOODS", "卖家已发货，等待买家确认收货"),
    TRADE_SUCCESS("TRADE_SUCCESS", "交易成功"),
    TRADE_FINISHED("TRADE_FINISHED", "交易完成"),
    TRADE_CLOSED("TRADE_CLOSED", "交易关闭");

    private String value;
    private final String desc;

    AliPayStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
