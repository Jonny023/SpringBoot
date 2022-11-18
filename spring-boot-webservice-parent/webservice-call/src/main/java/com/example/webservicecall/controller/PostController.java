package com.example.webservicecall.controller;

import com.alibaba.fastjson2.JSON;
import com.example.webservicecall.domain.vo.R;
import com.example.webservicecall.domain.vo.UserDTO;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PostController {

    /**
     *  包名类名一样，可以直接强制转换
     *  定义一个跟Object[] objects返回一样的类，就可以直接强转
     *  服务定义的@WebService(targetNamespace = "http://soap.domain.com"）地址就是返回对象的包
     *  对应的包路径为：com.domain.soap，targetNamespace的地址反过来
     * @param id
     * @return
     */
    @GetMapping("get1")
    public R<com.domain.soap.UserDTO> get1(Integer id) {

        JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();

        Client client = factory.createClient("http://localhost:8080/soap/user?wsdl");
        try {
            Object[] objects = client.invoke("getUserById", id);
            if (objects != null && objects.length > 0) {
                com.domain.soap.UserDTO user = (com.domain.soap.UserDTO)objects[0];
                return R.success(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.success(null);
    }

    /**
     *  通过fastjson转换
     * @param id
     * @return
     */
    @GetMapping("get")
    public R<UserDTO> get(Integer id) {

        JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();

        Client client = factory.createClient("http://localhost:8080/soap/user?wsdl");
        try {
            Object[] objects = client.invoke("getUserById", id);
            if (objects != null && objects.length > 0) {
                System.out.println("getUserById 调用结果：" + objects[0].toString());
                UserDTO user = JSON.parseObject(JSON.toJSONString(objects[0]), UserDTO.class);
                return R.success(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.success(null);
    }

    @GetMapping("list")
    public R<List<UserDTO>> list() {

        JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();

        Client client = factory.createClient("http://localhost:8080/soap/user?wsdl");
        try {
            Object[] objects = client.invoke("list");
            if (objects != null && objects.length > 0) {
                //返回的数据在数据的第一个对象里面，接收到的是服务方的实体对象，如果包名和类名一样可以直接强转
                System.out.println("getUsers调用部分结果：" + objects[0].toString());
                List<UserDTO> userDTOS = JSON.parseArray(JSON.toJSONString(objects[0]), UserDTO.class);
                return R.success(userDTOS);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.success(null);
    }

}
