package com.sns.gobong.util.api;

public class ResponseContext {

    public static ThreadLocal<Long> requestAt = ThreadLocal.withInitial(() -> null);

    public static void setRequestAt(long time) {
        requestAt.set(time);
    }

    public static void clear() {
        requestAt.remove();
    }
}
