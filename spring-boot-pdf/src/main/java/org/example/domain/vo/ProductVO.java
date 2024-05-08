package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVO {

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 产品编号
     */
    private String productNo;

    /**
     * 硬件版本
     */
    private String hardwareVersion;

    /**
     * 软件版本
     */
    private String softwareVersion;

    /**
     * 生产日期
     */
    private String dateOfManufacture;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 二维码
     */
    private String qrcode;

    public static List<ProductVO> batch() {
        return IntStream.iterate(1, i -> i + 1).limit(100).mapToObj(i -> ProductVO.builder()
                .productName("智能护理床")
                .productType(String.format("MU%d", i))
                .productNo(String.format("MU%04d", i))
                .hardwareVersion("001")
                .softwareVersion("001")
                .dateOfManufacture(LocalDate.now().toString())
                .phone(String.format("1300000000%d", i))
                .manufacturer("中国xx有限公司")
                .qrcode("http://localhost:8082/img/qr.jpg")
                .build()
        ).collect(Collectors.toList());
    }
}