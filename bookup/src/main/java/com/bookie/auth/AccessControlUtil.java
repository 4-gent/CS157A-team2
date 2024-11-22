package com.bookie.auth;

import java.lang.reflect.Field;
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
            // Check if paramName matches the parameter directly
            if (paramName.equals(parameters[i].getName()) && args[i] instanceof String) {
                return (String) args[i];
            }

            // Handle nested fields (e.g., "paymentDetails.username")
            if (paramName.contains(".") && args[i] != null) {
                String[] parts = paramName.split("\\."); // Split by '.'
                if (parameters[i].getName().equals(parts[0])) {
                    return resolveNestedField(args[i], parts, 1); // Start resolving from the second part
                }
            }
        }
        return null; // No match found
    }

    /**
     * Resolves a nested field path using reflection.
     * 
     * @param obj The root object
     * @param parts The parts of the nested path
     * @param index The current index in the path
     * @return The resolved field value as a String
     */
    private static String resolveNestedField(Object obj, String[] parts, int index) {
        if (index >= parts.length || obj == null) {
            return null;
        }
        try {
            // Get the field by name and make it accessible
            Field field = obj.getClass().getDeclaredField(parts[index]);
            field.setAccessible(true);
            Object value = field.get(obj);

            // If this is the final part, return the value as a String
            if (index == parts.length - 1) {
                return value != null ? value.toString() : null;
            }

            // Otherwise, recurse for deeper parts
            return resolveNestedField(value, parts, index + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}