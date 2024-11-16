package com.bookie.auth;

public class UserContext {
    private static final ThreadLocal<String> userIdHolder = new ThreadLocal<>();

    // Set the user ID
    public static void setUserId(String userId) {
        userIdHolder.set(userId);
    }

    // Get the user ID
    public static String getUserId() {
        return userIdHolder.get();
    }

    // Clear the user ID
    public static void clear() {
        userIdHolder.remove();
    }
}
