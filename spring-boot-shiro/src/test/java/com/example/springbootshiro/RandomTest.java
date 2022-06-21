package com.example.springbootshiro;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

public class RandomTest {

    @Test
    public void test() {
        char[] arr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+-=_".toCharArray();
        int len = arr.length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 128; i++) {
            builder.append(arr[RandomUtils.nextInt(0, len)]);
        }
        System.out.println(builder);
        System.out.println(builder.toString().length());
    }
}
