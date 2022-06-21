package com.example.springbootshiro.service;

import com.example.springbootshiro.domain.form.LoginForm;
import com.example.springbootshiro.domain.response.R;

public interface UserService {
    R login(LoginForm form);
}
