package com.jonny.repository;

import com.jonny.entity.Demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DemoRepository extends JpaRepository<Demo, Long> {

    @Query(name = "Demo.findByValue1")
    Demo findByValue1(String value);
}
