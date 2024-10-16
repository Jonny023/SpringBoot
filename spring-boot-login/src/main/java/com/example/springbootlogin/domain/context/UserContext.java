package com.example.springbootlogin.domain.context;

import java.util.Optional;

/**
 * 用户上下文信息
 */
public final class UserContext {

    private static final ThreadLocal<UserData> LOCAL = new ThreadLocal<>();

    public static void set(UserData user) {
        LOCAL.set(user);
    }

    public static Optional<UserData> get() {
        return Optional.ofNullable(LOCAL.get());
    }

    public static void remove() {
        LOCAL.remove();
    }

    public static Long id() {
        return get().map(UserData::getId).orElse(null);
    }

    public static String username() {
        return get().map(UserData::getUsername).orElse(null);
    }
}