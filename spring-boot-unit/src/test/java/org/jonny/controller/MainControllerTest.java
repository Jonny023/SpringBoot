package org.jonny.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * @author: Jonny
 * @description:
 * @date:created in 2021/5/30 15:45
 * @modificed by:
 */
@SpringBootTest
class MainControllerTest {

    private MockMvc mockMvc;
    @Resource
    private WebApplicationContext wac;

    @Test
    void hello() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        RequestBuilder request = MockMvcRequestBuilders.get("http://localhost:8080/hello");
        String response = mockMvc.perform(request).andReturn().getResponse().getContentAsString();
        System.out.println(response);
    }
}