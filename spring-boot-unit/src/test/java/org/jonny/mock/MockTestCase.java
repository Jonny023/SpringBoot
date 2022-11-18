package org.jonny.mock;

import org.jonny.service.impl.UserService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

public class MockTestCase extends MockTest {

    @Resource
    private UserService userService;

    @Test
    public void exe() {
        System.out.println(userService.run());
    }

}
