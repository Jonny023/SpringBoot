package com.example;

import org.example.domain.vo.ProductVO;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ExportTest {

    @Test
    public void test() {
        List<ProductVO> batch = ProductVO.batch();
        System.out.println(batch);
    }

}