package com.jonny.service.impl;

import com.alibaba.fastjson.JSON;
import com.jonny.entity.Student;
import com.jonny.entity.vo.StudentVO;
import com.jonny.repository.StudentRepository;
import com.jonny.response.ResponseApi;
import com.jonny.service.IStudentService;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.query.internal.NativeQueryReturnBuilder;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudnetService implements IStudentService {

    @Autowired
    private StudentRepository studentDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ResponseApi save(Student student) {
        return ResponseApi.success(studentDao.save(student));
    }

    @Override
    public ResponseApi update(Student student) {
        return ResponseApi.success(studentDao.saveAndFlush(student));
    }

    /**
     *  自定义JPQL查询冰返回map
     * @param stuName
     * @return
     */
    @Override
    public ResponseApi queryStudent(String stuName) {
        // 自定义JPQL查询，返回map,map不区分大小写
//        Query query = entityManager.createQuery("select new map(stu.id as id, stu.stuName as stuName) from Student stu");
        Query query = entityManager.createQuery("select new com.jonny.entity.vo.StudentVO(stu.id as id, stu.stuName as stuName) from Student stu");
        List list = query.getResultList();
        System.out.println(JSON.toJSON(list).toString());
        return null;
    }

    /**
     *  自定义原生sql查询
     * @param stuName
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseApi queryStudentBySql(String stuName) {
        // 返回map
//        Query query = entityManager.createNativeQuery("select stu.id, stu.stu_name as stuName from student stu")
//                .unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 返回VO类
        Query query = entityManager.createNativeQuery("select stu.id, stu.stu_name as stuName from student stu")
                .unwrap(NativeQueryImpl.class)
                .addScalar("id", new LongType())
                .addScalar("stuName", new StringType())
                .setResultTransformer(Transformers.aliasToBean(StudentVO.class));

        System.out.println(JSON.toJSON(query.getResultList()));
        return null;
    }

    /**
     *  分页
     * @return
     */
    @Override
    public ResponseApi list() {
        Pageable pageable = PageRequest.of(0, 10);
        Page page = studentDao.findAll(pageable);
        List<Student> list = page.getContent();
        AtomicInteger i = new AtomicInteger(1);
        // lambda默认是final，里面不允许修改变量
        list.forEach(stu -> {
            stu.setStuName(stu.getStuName() + i);
            i.getAndIncrement();
        });
        System.out.println(JSON.toJSON(page));
        return null;
    }

    @Override
    public void queryCol () {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Student> q = cb.createQuery(Student.class);
//        Root<Student> root = q.from(Student.class);
//        q.select(
//                cb.construct(
//                        Student.class,
//                        root.get("id"),
//                        root.get("stuName")));
//        Query query = entityManager.createQuery(q);


        // 执行效果：select count(student0_.id) as col_0_0_ from student student0_ where student0_.id=1
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
//        Root<Student> root = query.from(Student.class);
//        query.select(criteriaBuilder.count(root.get("id")));
//        Predicate predicate = criteriaBuilder.equal(root.get("id"), 1);
//        query.where(predicate);
//        Long singleResult = entityManager.createQuery(query).getSingleResult();

        // 将查询数据封装到VO类中
        // 执行效果：select student0_.id as col_0_0_, student0_.stu_name as col_1_0_ from student student0_ where student0_.id=1
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StudentVO> query = criteriaBuilder.createQuery(StudentVO.class);
        Root<Student> root = query.from(Student.class);
        query.select(
                criteriaBuilder.construct(
                        StudentVO.class,
                        root.get("id"),
                        root.get("stuName")));
        Predicate predicate = criteriaBuilder.equal(root.get("id"), 1);
        query.where(predicate);
        Query query1 = entityManager.createQuery(query);

        System.out.println(query1.getResultList());
    }

}
