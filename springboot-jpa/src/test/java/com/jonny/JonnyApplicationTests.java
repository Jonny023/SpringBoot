package com.jonny;

import com.jonny.entity.Student;
import com.jonny.service.IStudentService;
import com.jonny.service.impl.StudnetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class JonnyApplicationTests {

    @Autowired
    private IStudentService studentService;

    @Test
    void exe() {
        Student stu = new Student();
        stu.setId(1L);
        stu.setStuName("张三121");
        stu.setAddress(null);
//        stu.setAddress("重庆");
        studentService.update(stu);
    }

    @Test
    void options() {
//        studentService.queryStudent("张三");
//        studentService.queryStudentBySql("张三");
//        studentService.list();
        studentService.queryCol();
    }

}
