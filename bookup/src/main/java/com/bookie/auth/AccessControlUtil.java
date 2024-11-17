package com.bookie.auth;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.bookie.dao.UserDAO;

public class AccessControlUtil {

    /**
     * Main method to check access control based on annotations.
     */
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
            SameUser sameUserAnnotation = method.getAnnotation(SameUser.class);
            String paramName = sameUserAnnotation.value();
            String username = getArgumentValue(paramName, method, args);
            if (username == null || !UserDAO.isSameUser(username)) {
                throw new SecurityException("Access denied: User mismatch");
            }
        }

        // Check for @IsAdminOrSameUser
        if (method.isAnnotationPresent(IsAdminOrSameUser.class)) {
            IsAdminOrSameUser annotation = method.getAnnotation(IsAdminOrSameUser.class);
            String paramName = annotation.value();
            String username = getArgumentValue(paramName, method, args);

            if (!UserDAO.isUserAnAdmin() && (username == null || !UserDAO.isSameUser(username))) {
                throw new SecurityException("Access denied: Admin or user match required");
            }
        }
    }

    /**
     * Helper method to find a method by its name.
     */
    private static Method findMethod(Class<?> clazz, String methodName) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    /**
     * Helper method to get the value of a parameter by its name.
     */
    private static String getArgumentValue(String paramName, Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            // Compare the parameter name with the provided paramName
            if (parameters[i].getName().equals(paramName) && args[i] instanceof String) {
                return (String) args[i];
            }
        }
        return null;
    }

    /**
     * Checks if a method has a specific annotation.
     */
    public static boolean hasAnnotation(Method method, Class<? extends Annotation> annotationClass) {
        return method.isAnnotationPresent(annotationClass);
    }

    /**
     * Extracts the parameter names from a given method.
     */
    public static String[] getParameterNames(Method method) {
        Parameter[] parameters = method.getParameters();
        String[] paramNames = new String[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            paramNames[i] = parameters[i].getName();
        }
        return paramNames;
    }
}