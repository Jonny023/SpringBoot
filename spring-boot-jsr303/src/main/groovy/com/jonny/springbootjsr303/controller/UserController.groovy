package com.jonny.springbootjsr303.controller

import com.jonny.springbootjsr303.form.UserAddOrUpdateForm
import com.jonny.springbootjsr303.validation.Insert
import com.jonny.springbootjsr303.validation.Update
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {

    @PostMapping("/save")
    UserAddOrUpdateForm save(@Validated([Insert.class]) @RequestBody UserAddOrUpdateForm form) {
        return form
    }

    @PostMapping("/update")
    UserAddOrUpdateForm update(@Validated([Update.class, Insert.class]) @RequestBody UserAddOrUpdateForm form) {
        return form
    }
}
