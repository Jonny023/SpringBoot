package com.example.springbootjpadruid.service;

import com.alibaba.fastjson.JSON;
import com.example.springbootjpadruid.domain.common.BasePage;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Jonny
 * @description 原生sql查询，自动将下划线转驼峰
 */
@Service
public class JpaCommonService {

    private static final SimpleTypeConverter CONVERTER = new SimpleTypeConverter();

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 查询数据集合
     *
     * @param sql    查询sql，参数用:name格式
     * @param params 查询参数map格式，key对应参数中的:name
     * @param clazz  实体类型，为空则直接转换为map格式
     * @return list
     */
    @SuppressWarnings("unchecked")
    public <P extends BasePage> List<?> queryList(String sql, P params, Class<?> clazz) {
        String jsonParam = JSON.toJSONString(params);
        Map<String, Object> mapParams = JSON.parseObject(jsonParam, Map.class);
        // 提取 SQL 中的参数并过滤无效参数
        Set<String> sqlParams = extractSqlParams(sql);
        Map<String, Object> filteredParams = filterParams(sqlParams, mapParams);
        return queryList(sql, mapParams, filteredParams, clazz);
    }

    /**
     * 总记录数
     *
     * @param sql    查询sql，参数用:name格式
     * @param params 查询参数map格式，key对应参数中的:name
     * @return list
     */
    @SuppressWarnings("unchecked")
    public <P extends BasePage> long count(String sql, P params) {
        String jsonParam = JSON.toJSONString(params);
        Map<String, Object> mapParams = JSON.parseObject(jsonParam, Map.class);
        // 提取 SQL 中的参数并过滤无效参数
        Set<String> sqlParams = extractSqlParams(sql);
        Map<String, Object> filteredParams = filterParams(sqlParams, mapParams);
        return count(sql, mapParams, filteredParams, Long.class);
    }

    /**
     * 查询数据集合
     *
     * @param sql            查询sql，参数用:name格式
     * @param params         查询参数map格式，key对应参数中的:name
     * @param filteredParams sql中使用的参数
     * @param clazz          实体类型，为空则直接转换为map格式
     * @return list
     */
    @SuppressWarnings("all")
    public List<?> queryList(String sql, Map<String, Object> params, Map<String, Object> extractSqlParams, Class<?> clazz) {
        try {
            NativeQuery<?> query = entityManager.createNativeQuery(sql).unwrap(NativeQuery.class);
            if (Objects.nonNull(extractSqlParams)) {
                extractSqlParams.forEach((k, v) -> query.setParameter(k, v));
            }
            needPage(params, query);
            if (Objects.isNull(clazz)) {
                query.unwrap(NativeQuery.class).setResultTransformer(new AliasToEntityMapResultTransformer());
                return query.getResultList();
            } else {
                query.unwrap(NativeQuery.class).setResultTransformer(new AliasToBeanResultTransformer(clazz));
                return query.getResultList();
            }
        } catch (Exception e) {
            throw new RuntimeException("查询或转换数据时出错", e);
        }
    }

    @SuppressWarnings("all")
    public long count(String countSql, Map<String, Object> params, Map<String, Object> filteredParams, Class<?> clazz) {
        long total;
        try {
            Query countQuery = entityManager.createNativeQuery(countSql);
            if (Objects.nonNull(filteredParams)) {
                filteredParams.forEach(countQuery::setParameter);
            }
            total = Long.parseLong(countQuery.getSingleResult().toString());
        } catch (Exception e) {
            throw new RuntimeException("查询记录数出错", e);
        }
        return total;
    }

    /**
     * 判断是否需要分页
     * 当需要分页时，需要设置setNeedPage(true)，并设置offset和size
     *
     * @param params 查询参数
     * @param query  查询对象
     */
    private void needPage(Map<String, Object> params, Query query) {
        if (Boolean.TRUE.equals(params.get("needPage"))) {
            int offset = (int) params.get("offset");
            int size = (int) params.get("size");
            query.setFirstResult(offset);
            query.setMaxResults(size);
        }
    }

    /**
     * 从 SQL 中提取所有命名参数
     *
     * @param sql SQL 查询
     * @return 参数名集合
     */
    private static Set<String> extractSqlParams(String sql) {
        Pattern pattern = Pattern.compile(":(\\w+)");
        Matcher matcher = pattern.matcher(sql);

        Set<String> params = new HashSet<>();
        while (matcher.find()) {
            params.add(matcher.group(1));
        }
        return params;
    }

    /**
     * 根据 SQL 中的参数过滤多余的 Map 参数
     *
     * @param sqlParams SQL 中使用的参数
     * @param params    原始参数
     * @return 过滤后的参数
     */
    private static Map<String, Object> filterParams(Set<String> sqlParams, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return Collections.emptyMap();
        }
        return params.entrySet().stream().filter(entry -> sqlParams.contains(entry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static class AliasToEntityMapResultTransformer implements ResultTransformer {

        @Override
        public Object transformTuple(Object[] tuple, String[] aliases) {
            Map<String, Object> result = new HashMap<>();
            for (int i = 0; i < aliases.length; i++) {
                if (aliases[i] != null) {
                    result.put(aliases[i].toLowerCase(), tuple[i]);
                }
            }
            return result;
        }

        @Override
        public List transformList(List collection) {
            return collection;
        }
    }

    private static class AliasToBeanResultTransformer implements ResultTransformer {

        private final Class<?> resultClass;

        public AliasToBeanResultTransformer(Class<?> resultClass) {
            this.resultClass = resultClass;
        }

        /**
         * 转换结果集 自定将下划线转驼峰
         *
         * @param tuple   结果集
         * @param aliases 字段名
         * @return 实体对象
         */
        @Override
        public Object transformTuple(Object[] tuple, String[] aliases) {
            try {
                Object result = resultClass.getDeclaredConstructor().newInstance();
                PropertyDescriptor[] props = Introspector.getBeanInfo(resultClass).getPropertyDescriptors();
                Map<String, Method> writeMethodMap = Arrays.stream(props).filter(p -> Objects.nonNull(p.getWriteMethod())).collect(Collectors.toMap(p -> p.getName().toLowerCase(), PropertyDescriptor::getWriteMethod));

                for (int i = 0; i < aliases.length; i++) {
                    if (aliases[i] == null) {
                        continue;
                    }
                    String fieldName = aliases[i].toLowerCase().replace("_", "");
                    Method writeMethod = writeMethodMap.get(fieldName);
                    if (writeMethod != null) {
                        Object value = CONVERTER.convertIfNecessary(tuple[i], writeMethod.getParameterTypes()[0]);
                        writeMethod.invoke(result, value);
                    }
                }
                return result;
            } catch (Exception e) {
                throw new RuntimeException("实体映射错误: " + resultClass.getName(), e);
            }
        }

        @Override
        public List transformList(List collection) {
            return collection;
        }
    }
}
