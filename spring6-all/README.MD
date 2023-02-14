## Spring基础

[参考](https://www.bilibili.com/video/BV1kR4y1b7Qc?p=48&spm_id_from=pageDriver&vd_source=610e097b4d28ca7a9353304c7307c4a9)

### spring创建对象

* 核心依赖

```xml

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>6.0.2</version>
</dependency>
```

* xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="user" class="com.example.domain.User"></bean>
</beans>
```

* 核心代码

> 创建user这个bean时会先调用无参构造，因为底层通过反射创建，所以会先执行无参构造器

```java
package com.example;

import com.example.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

    @Test
    public void testUser() {
        ApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("beans.xml");
        User user = (User) classPathXmlApplicationContext.getBean("user");
        System.out.println(user);

        user.add();
    }
}
```

* spring创建bean原理
    1. 加载beans.xml配置
    2. 解析xml配置
    3. 获取xml文件bean标签属性值（id和class属性）
    4. 使用反射根据类全路径创建对象
  ```java
    Class clazz = Class.forName("com.example.domain.User");
        //低版本创建对象
        //clazz.newInstance();

        //jdk17反射创建对象
        User user = (User) clazz.getDeclaredConstructor().newInstance();
        System.out.println(user);
        user.add();
    ```
* bean对象存放位置
    * 存放在org.springframework.beans.factory.support.DefaultListableBeanFactory.beanDefinitionMap中
  ```java
  Map<String, BeanDefinition> beanDefinitionMap
  //key: 唯一标识
  //value: 类定义（描述信息：作用域，是否懒加载等等）
  ```

* jdk11开始移除了@Resource注解，需要手动引入依赖才能使用

```xml

<dependency>
    <groupId>jakarta.annotation</groupId>
    <artifactId>jakarta.annotation-api</artifactId>
    <version>2.1.1</version>
</dependency>
```

### 基于全注解开发

* 增加配置类

```java
package com.example.cnofig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example")
public class SpringConfig {

}
```

* 启动类

```java
package com.example;

import com.example.cnofig.SpringConfig;
import com.example.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class);
        User user = ac.getBean("user", User.class);
        user.add();
    }
}
```

### 反射

```java
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

```

### 动态代理

#### JDK动态代理

* 代理类

```java
package com.example.spring6aop;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * JDK动态代理
 */
@Slf4j
public class JdkProxyFactory implements InvocationHandler {

    private Object target;

    public JdkProxyFactory(Object target) {
        this.target = target;
    }

    /**
     * @param proxy  代理对象
     * @param method 目标对象的方法
     * @param args method方法对应的参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("aop start: {}, 参数: {}", method.getName(), Arrays.toString(args));
        Object result = method.invoke(target, args);
        log.info("aop end: {}, 结果: {}", method.getName(), result);
        return result;
    }

    //public Object getProxy() {
    //    Class<?> clazz = target.getClass();
    //    ClassLoader classLoader = clazz.getClassLoader();
    //    Class<?>[] interfaces = clazz.getInterfaces();
    //    InvocationHandler invocationHandler = (proxy, method, args) -> {
    //        log.info("aop日志: {}, 参数: {}", method.getName(), Arrays.toString(args));
    //        Object result = method.invoke(target, args);
    //        log.info("aop日志: {}, 结果: {}", method.getName(), result);
    //        return result;
    //    };
    //    return Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
    //}

    public Object getProxy() {
        Class<?> clazz = target.getClass();
        ClassLoader classLoader = clazz.getClassLoader();
        Class<?>[] interfaces = clazz.getInterfaces();
        return Proxy.newProxyInstance(classLoader, interfaces, this);
    }
}
```

* 测试类

```java
package com.example.spring6aop;

import org.junit.jupiter.api.Test;

public class TestJdkProxyFactory {

    @Test
    public void test() {
        JdkProxyFactory proxyFactory = new JdkProxyFactory(new UserServiceImpl());
        UserService userService = (UserService) proxyFactory.getProxy();
        userService.add("张三");
    }
}
```

#### cglib代理

* 依赖

```xml

<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>3.2.7</version>
</dependency>
```

* 代理类

```java
package com.example.spring6aop;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * cglib动态代理
 * JDK 8 中有关反射相关的功能自从 JDK 9 开始就已经被限制了，为了兼容原先的版本，需要在运行项目时添加vm options
 * --add-opens java.base/java.lang=ALL-UNNAMED
 * 选项来开启这种默认不被允许的行为。
 */
@Slf4j
public class CglibProxyFactory implements MethodInterceptor {

    private Object target;

    public CglibProxyFactory(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        log.info("cglib start: {}, 参数: {}", method.getName(), Arrays.toString(objects));
        Object result = methodProxy.invokeSuper(o, objects);
        log.info("cglib end: {}, 结果: {}", method.getName(), result);
        return result;
    }

    public Object getProxy() {
        // 通过CGLIB动态代理获取代理对象的过程
        Enhancer enhancer = new Enhancer();
        // 设置enhancer对象的父类
        enhancer.setSuperclass(this.target.getClass());
        // 设置enhancer的回调对象
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }
}
```

* 测试类

```java
package com.example.spring6aop;

import org.junit.jupiter.api.Test;

public class TestCglibProxyFactory {

    @Test
    public void test() {
        CglibProxyFactory proxyFactory = new CglibProxyFactory(new UserServiceImpl());
        UserService userService = (UserService) proxyFactory.getProxy();
        userService.add("张三");
    }
}
```

#### spring aop + aspectj

* 依赖

```xml
<!--spring aop-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aop</artifactId>
    <version>6.0.2</version>
</dependency>
<dependency>
<groupId>org.springframework</groupId>
<artifactId>spring-aspects</artifactId>
<version>6.0.2</version>
</dependency>
```

* 配置类

```java
package com.example.spring6aop.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(value = {"com.example.spring6aop.aop", "com.example.spring6aop.service"})
public class MainConfig {
}
```

* 切面类

```java
package com.example.spring6aop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogAspect {

    private int step = 1;

    @Pointcut("execution(* com.example.spring6aop.service.UserServiceImpl.*(..))")
    public void pointCut() {

    }

    //@Before("execution(* com.example.spring6aop.service.UserServiceImpl.*(..))")
    //@Before(value = "execution(public String com.example.spring6aop.service.UserServiceImpl.add(..))")
    @Before("com.example.spring6aop.aop.LogAspect.pointCut()")
    public void beforeMethod(JoinPoint joinPoint) {
        log.info("{} 前置通知", step++);
        log.info("调用方法:{}, 参数: {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }

    //@After("execution(* com.example.spring6aop.service.*.*(..))")
    @After("pointCut()")
    public void afterMethod() {
        log.info("{} 后置通知", step++);
    }

    //@AfterReturning(value = "execution(* com.example.spring6aop.service.*.*(..))", returning = "result")
    @AfterReturning(value = "pointCut()", returning = "result")
    public void afterReturningMethod(JoinPoint joinPoint, Object result) {
        log.info("{} 返回通知", step++);
        log.info("返回数据: {}", result);
    }

    //@AfterThrowing(value = "execution(* com.example.spring6aop.service.*.*(..))", throwing = "ex")
    @AfterThrowing(value = "pointCut()", throwing = "ex")
    public void AfterThrowingMethod(JoinPoint joinPoint, Throwable ex) {
        log.info("{} 异常通知", step++);
        log.info("异常信息: ", ex);
    }

    //@Around(value = "execution(* com.example.spring6aop.service.*.*(..))")
    @Around(value = "pointCut()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            log.info("{} 环绕通知 目标方法前", step++);
            result = joinPoint.proceed();
            log.info("{} 环绕通知 目标方法后", step++);
        } catch (Throwable throwable) {
            log.info("{} 环绕通知 目标方法异常", step++);
        } finally {
            log.info("{} 环绕通知 目标方法finally", step++);
        }
        return result;
    }
}
```

* 测试类

```java
package com.example.spring6aop.aop;

import com.example.spring6aop.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestAop {

    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(MainConfig.class);
        UserService userService = ac.getBean(UserService.class);
        userService.add("张三");
    }
}
```

### junit单元测试

#### junit4

* 依赖

```xml

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>6.0.2</version>
</dependency>
<dependency>
  <groupId>junit</groupId>
  <artifactId>junit</artifactId>
  <version>4.13.2</version>
</dependency>
```

* 测试类

> @Test注解包路径为：org.junit.Test

```java
package com.example.junit.junit4;

import com.example.junit.MainConfig;
import com.example.junit.domain.User;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainConfig.class)
public class SpringJunit4Tests {

    @Resource
    private User user;

    @Test
    public void test() {
        user.add();
    }
}
```

#### junit5

* 依赖

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>6.0.2</version>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.6.3</version>
</dependency>
```

* 测试类
> @Test注解包路径为：org.junit.jupiter.api.Test

```java
package com.example.junit.junit5;

import com.example.junit.MainConfig;
import com.example.junit.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * 用@ExtendWith(SpringExtension.class)和@ContextConfiguration(classes = MainConfig.class)
 * 或者@SpringJUnitConfig(MainConfig.class)是一样的
 */
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = MainConfig.class)
@SpringJUnitConfig(MainConfig.class)
public class SpringJunit5Tests {

    @Resource
    private User user;

    @Test
    public void test() {
        user.add();
    }
}
```

### spring6

#### JIT和AOT

> AOT是spring6的新特性

* JIT(just in time)，动态编译（实时编译）边运行边编译
  * 程序运行时，动态生成代码
  * 启动比较慢，编译时需占用运行时资源
* AOT(ahead of time)，运行前编译（提前编译）
  * 把源代码直接转换为机器码
    * 启动快
    * 内存占用低
    * 运行时不能优化，程序安装时间长
    * 不能跨平台
  * spring6支持AOT技术，需借助GraalVM JDK
  * native image

#### AOT GraalVM安装

*  [下载](https://www.graalvm.org/downloads/)

* 配置环境变量

* 安装native-image插件
```shell
gu install native-image

gu list
```

* 安装c++环境
  * [下载visualstudio](https://visualstudio.microsoft.com/zh-hans/downloads/)
  * 安装时勾选【使用c++的桌面开发】
* 配置环境变量
  * 配置INCLUDE、LIB、PATH
* 打开x64 Native Tools Command Prompt for VS 2022
* 执行构建命令
  ```sh
  # 构建Hello.java类
  native-image Hello
  ```
* 生成Hello.exe可以直接运行