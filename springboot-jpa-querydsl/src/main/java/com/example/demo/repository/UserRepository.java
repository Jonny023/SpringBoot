package com.example.demo.repository;

import com.example.demo.base.BaseRepository;
import com.example.demo.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

}