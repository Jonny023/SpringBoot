# springboot-jpa-demo


## 错误

* No.1

```bash
Caused by: java.sql.SQLException: The server time zone value '�й���׼ʱ��' is unrecognized or represents more than one time zone. You must configure either the server or JDBC driver (via the serverTimezone configuration property) to use a more specifc time zone value if you want to utilize time zone support.
```

> 解决方法：加上时区

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jpa?serverTimezone=UTC
```

* No.2

```bash
nested exception is java.lang.IllegalArgumentException: Not a managed type: class com.jonny.entity.User
```

> 实体类忘记添加`@Entity`注解



# 查询API

> 在`interface`中使用这些方法可以实现简单查询操作 [文档](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference)

| Keyword                | Sample                                                       | JPQL snippet                                                 |
| :--------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| `And`                  | `findByLastnameAndFirstname`                                 | `… where x.lastname = ?1 and x.firstname = ?2`               |
| `Or`                   | `findByLastnameOrFirstname`                                  | `… where x.lastname = ?1 or x.firstname = ?2`                |
| `Is`, `Equals`         | `findByFirstname`,`findByFirstnameIs`,`findByFirstnameEquals` | `… where x.firstname = ?1`                                   |
| `Between`              | `findByStartDateBetween`                                     | `… where x.startDate between ?1 and ?2`                      |
| `LessThan`             | `findByAgeLessThan`                                          | `… where x.age < ?1`                                         |
| `LessThanEqual`        | `findByAgeLessThanEqual`                                     | `… where x.age <= ?1`                                        |
| `GreaterThan`          | `findByAgeGreaterThan`                                       | `… where x.age > ?1`                                         |
| `GreaterThanEqual`     | `findByAgeGreaterThanEqual`                                  | `… where x.age >= ?1`                                        |
| `After`                | `findByStartDateAfter`                                       | `… where x.startDate > ?1`                                   |
| `Before`               | `findByStartDateBefore`                                      | `… where x.startDate < ?1`                                   |
| `IsNull`, `Null`       | `findByAge(Is)Null`                                          | `… where x.age is null`                                      |
| `IsNotNull`, `NotNull` | `findByAge(Is)NotNull`                                       | `… where x.age not null`                                     |
| `Like`                 | `findByFirstnameLike`                                        | `… where x.firstname like ?1`                                |
| `NotLike`              | `findByFirstnameNotLike`                                     | `… where x.firstname not like ?1`                            |
| `StartingWith`         | `findByFirstnameStartingWith`                                | `… where x.firstname like ?1` (parameter bound with appended `%`) |
| `EndingWith`           | `findByFirstnameEndingWith`                                  | `… where x.firstname like ?1` (parameter bound with prepended `%`) |
| `Containing`           | `findByFirstnameContaining`                                  | `… where x.firstname like ?1` (parameter bound wrapped in `%`) |
| `OrderBy`              | `findByAgeOrderByLastnameDesc`                               | `… where x.age = ?1 order by x.lastname desc`                |
| `Not`                  | `findByLastnameNot`                                          | `… where x.lastname <> ?1`                                   |
| `In`                   | `findByAgeIn(Collection<Age> ages)`                          | `… where x.age in ?1`                                        |
| `NotIn`                | `findByAgeNotIn(Collection<Age> ages)`                       | `… where x.age not in ?1`                                    |
| `True`                 | `findByActiveTrue()`                                         | `… where x.active = true`                                    |
| `False`                | `findByActiveFalse()`                                        | `… where x.active = false`                                   |
| `IgnoreCase`           | `findByFirstnameIgnoreCase`                                  | `… where UPPER(x.firstame) = UPPER(?1)`                      |


### JsonIgnore的坑

> 默认序列化和反序列化都会过滤掉，导致数据赋值为null，解决方法如下：

```java
@JsonIgnore
public String getPassword() {
    return password;
}

@JsonProperty
public void setPassword(String password) {
    this.password = password;
}
```

* 指定序列化、反序列化权限
    * `Access.AUTO`(不控制序列化反序列化权限)
    * `READ_ONLY`(仅可以序列化)
    * `WRITE_ONLY`(仅可以反序列化)
    * `READ_WRITE`(序列化反序列化都可以)

* 数据插入或更新时只操作有值的数据，实体类添加@DynamicInsert和@DynamicUpdate

> 加了这个注解，调用方法时，会比对数据和数据库的值是否发生变化，没有变化的不更新，变化的会执行更新，若都无变化，不会执行更新语句

```java
@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class Student {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String stuName;
    private char sex;
}
```

### JPA更新数据前会先查询数据发出两个sql问题

> 更新流程：先执行 select 语句再执行 insert 语句，究其原因是 JPA 无法判断要执行 save(entity) 方法时，当前实体数据是否在数据库中已存在，所以要先查询再做判断：不存在 -> insert；存在 -> update。

[参考](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.entity-persistence.saving-entites)

> 解决方法如下：

* 方法1：在需要 insert 数据的时候使用 entityManager.persist(…) 方法；update 数据时使用 entityManager.merge(…) 方法
* 方法2：让实体实现 Persistable 接口，并实现 isNew() 方法
> 出于多方考虑，我选择了第二种方法来解决问题。对于第二种方法，需要通过标识判断当前待保存实体的状态：如果是新增的表单数据则 isNew() 返回 true；其他状态返回 false。

#### PS：问题
> 实体实现 Persistable 接口时，如果你的业务代码中有如下的情况：在一个事务中保存一个新实体对象时，如果引用到另一个参照对象，而这个参照对象不是已存在在数据库中的数据对象，则保存会报引用瞬态对象的错误。解决方法可以先将引用的参照对象保存到数据库（可以改变事务传播方式实现），然后再保存新实体对象即可。如果不想将参照对象保存到数据库，则实体不实现 Persistable 接口即可（目前没找到其他配置方法可以不持久化参照对象，汗...）。

* [引文](https://www.caokuan.cn/index.php/archives/jpasave.html)


* 分页核心代码
> 分页后`List<Student> list = page.getContent();`就是list，要操作需要遍历list

```java
Pageable pageable = PageRequest.of(0, 10);
Page page = studentDao.findAll(pageable);
```