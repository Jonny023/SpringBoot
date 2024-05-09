package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jxls.command.ImageCommand;
import org.jxls.util.Util;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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

    /**
     * 二维码byte
     */
    private byte[] qrcodeBytes;

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

    public static List<ProductVO> batchToJxls() throws IOException {

        // 文件流，输入一张叫fly的png图片
        // InputStream imageInputStream = new FileInputStream("D:/qr.jpg");
        File file = ResourceUtils.getFile("classpath:static/img/qr.jpg");
        // 使用工具方法把流转成byte数组
        byte[] imageBytes = Util.toByteArray(Files.newInputStream(file.toPath()));

        // imageType图片类型默认是PNG，支持：PNG, JPEG, EMF, WMF, PICT, DIB，模板中src是model中传入的字节数组byte[]
        return IntStream.iterate(1, i -> i + 1).limit(100).mapToObj(i -> ProductVO.builder()
                .productName("智能护理床")
                .productType(String.format("MU%d", i))
                .productNo(String.format("MU%04d", i))
                .hardwareVersion("001")
                .softwareVersion("001")
                .dateOfManufacture(LocalDate.now().toString())
                .phone(String.format("1300000000%d", i))
                .manufacturer("中国xx有限公司")
                .qrcodeBytes(imageBytes)
                .build()
        ).collect(Collectors.toList());
    }
}