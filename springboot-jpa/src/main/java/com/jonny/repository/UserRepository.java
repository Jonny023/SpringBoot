package com.jonny.repository;

import com.jonny.entity.User;
import com.jonny.entity.vo.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Map;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     *  根据username查询用户
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     *  根据username查询用户
     *  JPA会自动封装查询参数，最终执行效果如： where username=? and name=?
     * @param username
     * @return
     */
    User findByUsernameAndName(String username, String name);

    /**
     *  指定查询列
     * @return
     */
    @Query("select new com.jonny.entity.vo.UserVO(u.id, u.username) from User u")
    List<UserVO> queryUsernames();

    /**
     *  指定查询列封装为VO
     * @return
     */
    @Query("select new com.jonny.entity.vo.UserVO(u.id, u.username) from User u where u.username=?1 and u.name=?2")
    List<UserVO> queryColByUsernameAndName(String username, String name);

    /**
     *  查询指定列返回map
     *  @return
     */
    @Query("select new map(u.id as id, u.username as username) from User u where u.name=?2 and u.username=?1")
    List<Map<String, Object>> userList(String username, String name);

    /**
     *  原生SQL查询返回map
     *  @return
     */
    @Query(value = "select u.id, u.username from User u where u.name=?2 and u.username=?1", nativeQuery = true)
    List<Map<String, Object>> sqlQuery(String username, String name);
}
