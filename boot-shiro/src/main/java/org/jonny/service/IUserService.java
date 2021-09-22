package org.jonny.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jonny.entity.User;
import org.jonny.vo.UserVO;

import java.util.List;

public interface IUserService extends IService<User> {

    UserVO getById(Long id);

    List<User> list();
}
