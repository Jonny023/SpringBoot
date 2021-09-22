package com.jonny.repository;

import com.jonny.entity.Student;
import com.jonny.entity.vo.StudentVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Map;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    Student findByName(String stuName);
    Map<String, Object> queryMap(String stuName);
    StudentVO queryStudentVO(String stuName);
}