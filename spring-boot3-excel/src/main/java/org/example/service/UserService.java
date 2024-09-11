package org.example.service;

import com.github.pagehelper.PageInfo;
import org.example.domain.entity.SysUser;
import org.example.domain.request.UserPageReq;
import org.example.domain.vo.UserVO;

import java.util.List;

/**
 * @author Jonny
 */
public interface UserService {

    List<UserVO> list();

    PageInfo<SysUser> page(UserPageReq req);

    void exportUsers();
}