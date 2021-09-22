package com.example.util;

/**
 * @description:
 * @author: Jonny
 * @date: 2021-09-03
 */
@FunctionalInterface
public interface BeanUtilsCallBack<S, T> {

    void callBack(S t, T s);
}
