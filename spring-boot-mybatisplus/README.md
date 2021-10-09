# mybatis-plus

* 打包

```shell
mvn clean package -Ptest -DskipTests=true
```

# mapper.xml分页查询

* xml

```xml
<select id="listPage" resultType="com.example.response.MouldListResponseVO">
    select m.*, md.diagram_name from mould m left join mandril_diagram md on m.diagram_id = md.id
    <trim prefix="WHERE" prefixOverrides="AND">
        m.is_del = 0
        <if test="param.startDateTime != null and param.endDateTime != null">
            and m.create_time between #{param.startDateTime} and #{param.endDateTime}
        </if>
        <if test="param.mouldName != null and param.mouldName != ''">
            and m.mould_name like concat('%', #{param.mouldName}, '%')
        </if>
    </trim>
</select>
```

* Mapper interface

```java
public interface MouldMapper extends BaseMapper<Mould> {

    IPage<MouldListResponseVO> listPage(Page<MouldListResponseVO> page, @Param("param") MouldListRequestVO param);
}
```

* serviceImpl

> 如果想要不分页，可以把page的size设置为-1，负数也可以

```java
@Override
public IPage<MouldListResponseVO> listPage(MouldListRequestVO param) {
    if (StringUtils.hasText(param.getStartTime())) {
        param.setStartDateTime(DateUtil.str2LocalDateTime(param.getStartTime()).with(LocalTime.MIN));
    }
    if (StringUtils.hasText(param.getEndTime())) {
        param.setEndDateTime(DateUtil.str2LocalDateTime(param.getEndTime()).with(LocalTime.MAX));
    }
    if (param.getStartDateTime() != null && param.getEndDateTime() != null) {
        if (param.getStartDateTime().isAfter(param.getEndDateTime())) {
            throw new BizException(ResultEnum.DATE_VALID_ERROR);
        }
    }
    Page<MouldListResponseVO> page = new Page<>(param.getPageNo(), param.getPageSize());
    return baseMapper.listPage(page, param);
}
```