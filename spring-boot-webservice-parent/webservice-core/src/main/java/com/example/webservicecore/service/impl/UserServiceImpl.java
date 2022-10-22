package com.example.webservicecore.service.impl;

import com.example.webservicecore.constrains.WsConst;
import com.example.webservicecore.domain.vo.UserDTO;
import com.example.webservicecore.service.UserService;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@WebService(
        targetNamespace = WsConst.NAMESPACE_URI, //wsdl命名空间
        name = "userPortType",                 //portType名称 客户端生成代码时 为接口名称
        serviceName = "userService",           //服务name名称
        portName = "userPortName",             //port名称
        endpointInterface = "com.example.webservicecore.service.UserService")//指定发布webservcie的接口类，此类也需要接入@WebService注解
public class UserServiceImpl implements UserService {

    @Override
    public UserDTO get(Integer id) {
        return getData().stream().filter(elem -> Objects.equals(id, elem.getId())).findFirst().get();
    }

    @Override
    public List<UserDTO> list() {
        return getData();
    }

    private List<UserDTO> getData() {
        List<UserDTO> list = Arrays.asList(
                UserDTO.builder().id(1).username("admin").age(20).sex("男").build(),
                UserDTO.builder().id(2).username("zhangsan").age(28).sex("女").build(),
                UserDTO.builder().id(3).username("lisi").age(30).sex("男").build(),
                UserDTO.builder().id(4).username("wangwu").age(27).sex("女").build(),
                UserDTO.builder().id(5).username("zhaoliu").age(24).sex("男").build()
        );
        return Collections.synchronizedList(list);
    }
}
