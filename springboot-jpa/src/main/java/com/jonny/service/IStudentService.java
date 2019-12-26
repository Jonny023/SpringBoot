package com.jonny.service;

import com.jonny.entity.Student;
import com.jonny.response.ResponseApi;

public interface IStudentService {

    ResponseApi save(Student student);

    ResponseApi update(Student student);

    ResponseApi queryStudent(String stuName);

    ResponseApi queryStudentBySql(String stuName);

    ResponseApi list();

    void queryCol ();

}
