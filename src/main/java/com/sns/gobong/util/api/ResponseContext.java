package com.sns.gobong.util.api;

public class ResponseContext {

    public static ThreadLocal<Long> requestAt = new ThreadLocal<>();

    public static void clear() {
        requestAt.remove();
    }
}
