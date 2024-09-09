package org.example.service;

import org.example.domain.vo.UserVO;

import java.util.List;

/**
 * @author Jonny
 */
public interface UserService {

    List<UserVO> list();
}