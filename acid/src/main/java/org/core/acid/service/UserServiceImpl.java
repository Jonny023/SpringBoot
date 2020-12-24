package org.core.acid.service;

import com.querydsl.core.QueryResults;
import org.core.acid.base.AbstractService;
import org.core.acid.base.Page;
import org.core.acid.entity.QUser;
import org.core.acid.entity.User;
import org.core.acid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends AbstractService<User, Long> implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User save(User user) {
        System.out.println(userRepository);
        return baseRepository.save(user);
    }

    @Override
    public User update(User user) {
        QUser userModel = QUser.user;
        super.jpaQueryFactory.update(userModel).set(userModel, user).execute();
        return user;
    }

    @Override
    public User delete() {
        QUser userModel = QUser.user;
        super.jpaQueryFactory.delete(userModel).where(userModel.username.eq("admin")).execute();
        return null;
    }

    @Override
    public User get(String username) {
        QUser userModel = QUser.user;
        User user = super.jpaQueryFactory.select(userModel).from(userModel).where(userModel.username.eq(username)).fetchOne();
        return user;
    }

    @Override
    public Page<User> list(Page page) {
        QUser userModel = QUser.user;
        QueryResults<User> queryResults = super.jpaQueryFactory
                .select(userModel).from(userModel)
                .where(userModel.username.eq("guest"))
                .offset(page.getIndex(page))
                .limit(page.getPageSize())
                .fetchResults();
        return Page.calc(queryResults);
    }
}
