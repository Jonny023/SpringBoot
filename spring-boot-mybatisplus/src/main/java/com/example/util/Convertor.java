package com.example.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * @description: bean转换工具
 * @author: Jonny
 * @date: 2021-09-03
 */
public final class Convertor {

    private Convertor() {
    }

    public static <T> T convert(Object vo, T t) {
        if (vo == null) {
            return null;
        }
        copyProperties(vo, t);
        return t;
    }

    public static <T> T convert(Object vo, Supplier<T> target) {
        if (vo == null) {
            return null;
        }
        T t = target.get();
        copyProperties(vo, t);
        return t;
    }

    public static <T> T convert(Object vo, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        if (vo == null) {
            return null;
        }
        T object = clazz.newInstance();
        copyProperties(vo, object);
        return object;
    }

    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    /**
     * 使用场景：Entity、Bo、Vo层数据的复制，因为BeanUtils.copyProperties只能给目标对象的属性赋值，却不能在List集合下循环赋值，因此添加该方法
     * 如：List<AdminEntity> 赋值到 List<AdminVo> ，List<AdminVo>中的 AdminVo 属性都会被赋予到值
     * S: 数据源类 ，T: 目标类::new(eg: AdminVo::new)
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, BeanUtilsCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());

        T t = null;
        for (S source : sources) {
            t = target.get();
            copyProperties(source, t);
            if (callBack != null) {
                callBack.callBack(source, t);
            }
            list.add(t);
        }
        return list;
    }

    public static <T> T map2Bean(Map<String, Object> beanPropMap, Supplier<T> target) {
        T bean = target.get();
        Class<?> clazz = bean.getClass();
        try {
            for (Map.Entry<String, Object> entry : beanPropMap.entrySet()) {
                if (entry.getValue() != null) {
                    Field field = clazz.getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    field.set(bean, entry.getValue());
                    field.setAccessible(false);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public static <S, T> IPage<T> page(IPage<S> page, Supplier<T> target) {
        IPage<T> resultPage = new Page();
        resultPage.setCurrent(page.getCurrent());
        resultPage.setSize(page.getSize());
        resultPage.setPages(page.getPages());
        resultPage.setTotal(page.getTotal());
        resultPage.setRecords(Convertor.copyListProperties(page.getRecords(), target));
        return resultPage;
    }

}
