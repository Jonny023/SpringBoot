package com.example.webservicecore.service;

import com.example.webservicecore.constrains.WsConst;
import com.example.webservicecore.domain.vo.UserDTO;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

@WebService(targetNamespace = WsConst.NAMESPACE_URI, name = "userPortType")
public interface UserService {

    /**
     * 根据用户id获取用户信息
     */
    @WebMethod(operationName = "getUserById")
    UserDTO get(@WebParam(name = "id") Integer id);

    /**
     * 获取全部用户信息
     * */
    @WebMethod(operationName="list")
    List<UserDTO> list();
}
