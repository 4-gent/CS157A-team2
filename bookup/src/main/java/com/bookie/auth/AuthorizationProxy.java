package com.bookie.auth;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AuthorizationProxy implements InvocationHandler {
    private final Object target;

    public AuthorizationProxy(Object target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new AuthorizationProxy(target)
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Perform authorization checks using AccessControlUtil
        AccessControlUtil.checkAccess(target, method.getName(), args);
        
        // Invoke the actual method on the target object
        return method.invoke(target, args);
    }
}