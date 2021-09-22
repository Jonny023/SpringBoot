package org.jonny.controller;

import org.hamcrest.Matchers;
import org.jonny.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;

/**
 * @author: Jonny
 * @description:
 * @date:created in 2021/5/30 15:45
 * @modificed by:
 */
@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
class MainController2Test {

    @Resource
    private MockMvc mockMvc;
    @MockBean
    private IUserService userService;

    @Test
    void run1() {
        try {
            System.out.println(userService.run());
            mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("hello world")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}