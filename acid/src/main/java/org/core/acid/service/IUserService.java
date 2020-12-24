package org.core.acid.service;


import org.core.acid.base.Page;
import org.core.acid.entity.User;

public interface IUserService {

    User save(User user);

    User update(User user);

    User delete();

    User get(String username);

    Page<User> list(Page page);

}
