package com.example.springbootjpadruid.repository.primary;

import com.example.springbootjpadruid.domain.entity.primary.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


}
