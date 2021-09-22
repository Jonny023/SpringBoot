package org.jonny.service.impl;

import org.jonny.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * @author: e-lijing6
 * @description:
 * @date:created in 2021/5/30 16:53
 * @modificed by:
 */
@Service
public class UserService implements IUserService {
    @Override
    public String run() {
        return "hello world!";
    }
}
