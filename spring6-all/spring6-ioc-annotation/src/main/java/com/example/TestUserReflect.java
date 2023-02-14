package com.example;

import com.example.domain.Book;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射
 */
public class TestUserReflect {

    /**
     * 获取class对象的方式
     */
    @Test
    public void getReflectClass() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        //1.类名.class
        Class clazz1 = Book.class;

        //2.实例对象.class
        Class clazz2 = new Book().getClass();

        //3.Class.forName("类全路径");
        Class clazz3 = Class.forName("com.example.domain.Book");

        //实例化
        Book book = (Book) clazz3.getDeclaredConstructor().newInstance();
        System.out.println(book);
    }

    /**
     * 获取构造方法
     */
    @Test
    public void main() throws Exception {

        Class clazz = Book.class;
        //1.获取public方法 clazz.getConstructor获取public方法
        Constructor constructor = clazz.getConstructor(String.class, String.class);
        Book book = (Book) constructor.newInstance("水浒传", "施耐庵");
        System.out.println(book);

        //2.获取私有方法 clazz.getDeclaredConstructor能获取private、public的方法
        Constructor declaredConstructor = clazz.getDeclaredConstructor(String.class, String.class, Integer.class);
        //设置访问权限
        declaredConstructor.setAccessible(true);
        Book book1 = (Book) declaredConstructor.newInstance("水浒传", "施耐庵", 30);
        System.out.println(book1);
    }

    /**
     * 获取属性
     */
    @Test
    public void getField() throws Exception {
        Class clazz = Book.class;

        Book book = (Book) clazz.getDeclaredConstructor().newInstance();

        //获取所有public属性
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }

        //获取所有属性（包含私有的private属性）
        Field[] declaredFields = clazz.getDeclaredFields();
        System.out.println();
        System.out.println("所有属性");
        for (Field field : declaredFields) {
            System.out.println(field.getName());
            field.setAccessible(true);
            if ("name".equals(field.getName())) {
                //设置属性值
                field.set(book, "西游记");
            }
            if ("author".equals(field.getName())) {
                field.set(book, "吴承恩");
            }
            if ("count".equals(field.getName())) {
                field.set(book, 50000);
            }
        }
        System.out.println(book);
    }

    /**
     * 获取方法
     */
    @Test
    public void getMethod() throws InvocationTargetException, IllegalAccessException {
        Book book = new Book("红楼梦", "曹雪芹", 1000000);
        Class clazz = book.getClass();

        //获取public方法
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            //执行方法
            if ("toString".equals(method.getName())) {
                String result = (String) method.invoke(book);
                System.out.println(result);
            }
        }

        //获取私private有方法
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            //执行方法
            if ("test".equals(method.getName())) {
                method.setAccessible(true);
                String result = (String) method.invoke(book);
                System.out.println(result);
            }
        }
    }
}
