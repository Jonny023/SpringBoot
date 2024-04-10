package org.example.shiro.util;

import org.apache.shiro.SecurityUtils;
import org.example.domain.vo.UserVO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户上下文
 *
 * @author Jonny
 */
public class ContextUtil {

    /**
     * 用于存储全局参数
     */
    private static final ThreadLocal<Map<String, Object>> GLOBAL_PARAMS = new InheritableThreadLocal<>();

    /**
     * 当前登录用户信息
     *
     * @return 当前用户信息
     */
    public static Optional<UserVO> currentUser() {
        return Optional.ofNullable((UserVO) SecurityUtils.getSubject().getPrincipal());
    }

    /**
     * 获取当前用户名
     *
     * @return 用户名
     */
    public static String username() {
        return currentUser().map(UserVO::getUsername).orElse(null);
    }

    /**
     * 获取当前用ID
     *
     * @return 用户ID
     */
    public static Long userId() {
        return currentUser().map(UserVO::getId).orElse(null);
    }

    /**
     * 设置全局参数
     *
     * @param params 参数
     */
    public static void set(Map<String, Object> params) {
        init();
        GLOBAL_PARAMS.set(params);
    }

    /**
     * 初始化全局参数
     */

    private static void init() {
        if (GLOBAL_PARAMS.get() == null) {
            GLOBAL_PARAMS.set(new HashMap<>());
        }
    }

    /**
     * 获取全局参数
     *
     * @return 全局参数
     */
    public static Map<String, Object> get() {
        init();
        return GLOBAL_PARAMS.get();
    }

    /**
     * 移除全局参数
     */
    public static void remove() {
        GLOBAL_PARAMS.remove();
    }
}