package org.jonny.mock;

import org.jonny.App;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {App.class})
@AutoConfigureMockMvc
public class MockTest {

    @Resource
    protected MockMvc mockMvc;

    protected void perform(MockHttpServletRequestBuilder requestBuilder) {
        try {
            this.mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
