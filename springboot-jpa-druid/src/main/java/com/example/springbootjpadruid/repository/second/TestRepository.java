package com.example.springbootjpadruid.repository.second;

import com.example.springbootjpadruid.domain.entity.second.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {


}
