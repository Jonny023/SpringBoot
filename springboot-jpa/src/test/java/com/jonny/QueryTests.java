package com.jonny;

import com.alibaba.fastjson.JSON;
import com.jonny.entity.Student;
import com.jonny.entity.vo.StudentVO;
import com.jonny.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
public class QueryTests {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void exe() {
        Specification<Student> specification = new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                Predicate c1 = criteriaBuilder.equal(root.get("sex"), '男');
                Predicate c2 = criteriaBuilder.equal(root.get("address"), "北京");
                return criteriaBuilder.and(c1, c2);
            }
        };
        List<Student> list = studentRepository.findAll(specification);
        System.out.println(JSON.toJSON(list));
    }

    @PersistenceContext
    private EntityManager em;

    @Test
    void exe1() {
        //查询工厂
        CriteriaBuilder cb = em.getCriteriaBuilder();
        //查询类
        CriteriaQuery<Student> query = cb.createQuery(Student.class);
        Root<Student> stu = query.from(Student.class);
        //查询条件
        List<Predicate> predicates = new LinkedList<>();
        //查询条件设置
        predicates.add(cb.equal(stu.get("sex"), '男'));
        predicates.add(cb.like(stu.get("address"), "北京"));
        //拼接where查询
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        //用JPA 2.0的TypedQuery进行查询
        TypedQuery<Student> typedQuery = em.createQuery(query);
        System.out.println(JSON.toJSONString(typedQuery.getResultList()));
    }

    @Test
    void queryField() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = cb.createQuery();
        Root root = criteriaQuery.from(Student.class);
        criteriaQuery.multiselect(
                root.get("id"),
                root.get("stuName"),
                root.get("address")
        );
        criteriaQuery.where(cb.equal(root.get("address"), "北京"));
        List list = em.createQuery(criteriaQuery).getResultList();
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    void exe3() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root e = cq.from(Student.class);
        cq.select(cb.construct(StudentVO.class, e.get("id"), e.get("stuName")));
        cq.where(cb.equal(e.get("address"), "北京"));
        Query query = em.createQuery(cq);
        List<StudentVO> result = query.getResultList();
        System.out.println(JSON.toJSONString(result));
    }
}
