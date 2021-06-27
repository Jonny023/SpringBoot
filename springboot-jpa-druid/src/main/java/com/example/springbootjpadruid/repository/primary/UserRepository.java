package com.example.springbootjpadruid.repository.primary;

import com.example.springbootjpadruid.entity.primary.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
