package com.example.springbootjpadruid.service.impl;

import com.example.springbootjpadruid.domain.common.PageVO;
import com.example.springbootjpadruid.domain.entity.primary.User;
import com.example.springbootjpadruid.domain.query.UserQuery;
import com.example.springbootjpadruid.domain.vo.UserVO;
import com.example.springbootjpadruid.repository.primary.UserRepository;
import com.example.springbootjpadruid.service.JpaCommonService;
import com.example.springbootjpadruid.service.UserService;
import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.ResultTransformer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private JpaCommonService jpaCommonService;

    @Override
    public String init(List<User> users) {
        repository.saveAll(users);
        return "ok";
    }

    @Override
    public User getUser() {
        PredicateBuilder<User> where = Specifications.and();
        where.eq("username", "zhangsan");
        where.eq("age", 20);
        return repository.findOne(where.build()).orElse(null);
    }

    /**
     * 分页1
     *
     * @param param
     * @return
     */
    @Override
    public PageVO<UserVO> listPage(UserQuery param) {
        PageRequest pageable = PageRequest.of(param.getPage(), param.getSize());
        Page<User> page = repository.findAll(pageable);
        return PageVO.of(page, UserVO.class);
    }

    /**
     * 分页2
     *
     * @param param 查询参数
     * @return 分页数据
     */
    @SuppressWarnings("all")
    @Override
    public PageVO<UserVO> nativeListPage(UserQuery param) {
        StringBuffer sql = new StringBuffer("select id as id, username as username, age as age, address as address from sys_user where 1 = 1");
        buildWhere(param, sql);

        StringBuffer countSql = new StringBuffer("select count(1) from sys_user where 1 = 1");
        buildWhere(param, countSql);

        PageRequest pageable = PageRequest.of(param.getPage(), param.getSize());

        NativeQuery query = entityManager.createNativeQuery(sql.toString()).unwrap(NativeQuery.class);
        query.setFirstResult(param.getOffset());
        query.setMaxResults(param.getSize());
        // 主键id无法直接映射，需要手动设置
        // query.setResultTransformer(Transformers.aliasToBean(UserVO.class));

        // 由于返回数据包含主键id，无法直接映射，只能借助投影手动映射
        query.setResultTransformer(new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] objects, String[] strings) {
                UserVO userVO = new UserVO();
                userVO.setId(Long.parseLong(objects[0].toString()));
                userVO.setUsername(objects[1].toString());
                userVO.setAge(Integer.parseInt(objects[2].toString()));
                userVO.setAddress(objects[3].toString());
                return userVO;
            }

            @Override
            public List transformList(List list) {
                return list;
            }
        });
        buildParam(param, query);

        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        buildParam(param, countQuery);
        long total = Long.parseLong(countQuery.getSingleResult().toString());

        PageImpl<UserVO> pageImpl = new PageImpl<>(query.getResultList(), pageable, total);
        return PageVO.of(pageImpl);
    }

    /**
     * 分页3
     * 原生sql自定转驼峰，带下划线不管大小自动转标准驼峰，全大写会转小写然后转标准驼峰
     * 解决分页方法2的原生sql如果没用as别名，无法自动转驼峰的问题
     *
     * @param param
     * @return
     */
    @SuppressWarnings("all")
    @Override
    public PageVO<UserVO> listPageByJpaUtil(UserQuery param) {
        // StringBuffer sql = new StringBuffer("select id, username, age, address, user_role as USER_ROLE from sys_user where 1 = 1");
        StringBuffer sql = new StringBuffer("select * from sys_user where 1 = 1");
        buildWhere(param, sql);

        StringBuffer countSql = new StringBuffer("select count(1) from sys_user where 1 = 1");
        buildWhere(param, countSql);

        PageRequest pageable = PageRequest.of(param.getPage(), param.getSize());
        param.setNeedPage(Boolean.TRUE);
        List<UserVO> userList = (List<UserVO>) jpaCommonService.queryList(sql.toString(), param, UserVO.class);
        long total = jpaCommonService.count(countSql.toString(), param);

        PageImpl<UserVO> pageImpl = new PageImpl<>(userList, pageable, total);
        return PageVO.of(pageImpl);
    }

    private void buildParam(UserQuery param, Query query) {
        if (StringUtils.hasText(param.getUsername())) {
            query.setParameter("username", param.getUsername());
        }
    }

    private void buildWhere(UserQuery param, StringBuffer sql) {
        if (StringUtils.hasText(param.getUsername())) {
            sql.append(" and username = :username");
        }
    }

}