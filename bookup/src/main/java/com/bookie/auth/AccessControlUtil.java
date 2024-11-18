package com.bookie.auth;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.bookie.dao.UserDAO;

public class AccessControlUtil {
    public static void checkAccess(Object target, String methodName, Object... args) throws Exception {
        Method method = findMethod(target.getClass(), methodName);
        if (method == null) {
            throw new NoSuchMethodException("Method not found: " + methodName);
        }

        // Check for @IsAdmin
        if (method.isAnnotationPresent(IsAdmin.class)) {
            if (!UserDAO.isUserAnAdmin()) {
                throw new SecurityException("Access denied: Admin privileges required");
            }
        }

        // Check for @SameUser
        if (method.isAnnotationPresent(SameUser.class)) {
            SameUser annotation = method.getAnnotation(SameUser.class);
            String paramName = annotation.value();
            String username = getArgumentValue(paramName, method, args);

            if (username == null || !UserDAO.isSameUser(username)) {
                throw new SecurityException("Access denied: User mismatch");
            }
        }
    }

    private static Method findMethod(Class<?> clazz, String methodName) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    private static String getArgumentValue(String paramName, Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getName().equals(paramName) && args[i] instanceof String) {
                return (String) args[i];
            }
        }
        return null;
    }
}