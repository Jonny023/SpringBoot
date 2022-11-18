package com.jonny;

import com.alibaba.fastjson.JSON;
import com.jonny.entity.Demo;
import com.jonny.repository.DemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NamedQueryXMLTests {

    @Autowired
    private DemoRepository demoRepository;

    @Test
    void exe() {
        Demo demo = demoRepository.findByValue1("cq");
        System.out.println(JSON.toJSON(demo));
    }
}
