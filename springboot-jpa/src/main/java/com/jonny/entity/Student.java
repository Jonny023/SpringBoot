package com.jonny.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
//@NamedQuery(name = "Student.findByName", query = "select stu from Student stu where stu.stuName = ?1")
@NamedQueries({
        @NamedQuery(name = "Student.findByName", query = "select stu from Student stu where stu.stuName = ?1"),
        @NamedQuery(name = "Student.queryMap", query = "select new map(stu.id as id, stu.stuName as name) from Student stu where stu.stuName = ?1"),
        @NamedQuery(name = "Student.queryStudentVO", query = "select new com.jonny.entity.vo.StudentVO(stu.id, stu.stuName) from Student stu where stu.stuName = ?1")
})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stuName;
    private char sex;
    private String address;
}
