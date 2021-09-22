package com.jonny;

import com.alibaba.fastjson.JSON;
import com.jonny.entity.Student;
import com.jonny.entity.vo.StudentVO;
import com.jonny.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class NamedQueryTests {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void exe() {
        Student student = studentRepository.findByName("张浪");
        System.out.println(JSON.toJSON(student));

        Map<String, Object> map = studentRepository.queryMap("张浪");
        System.out.println(JSON.toJSON(map));

        StudentVO vo = studentRepository.queryStudentVO("张浪");
        System.out.println(JSON.toJSON(vo));
    }
}
