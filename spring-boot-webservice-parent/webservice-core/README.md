## 提供webservice的服务

* http://localhost:8080/soap
* 访问地址：http://localhost:8080/soap/user?wsdl


## 启动报错

* 1.启动报错，找不到properties，提示如下
```shell
Description:

The Bean Validation API is on the classpath but no implementation could be found

Action:

Add an implementation, such as Hibernate Validator, to the classpath
```
> 需要jsr303依赖

```xml
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>
```