package com.bookie.auth;

import com.bookie.bizlogic.UserServiceInterface;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class UserServiceProxy implements InvocationHandler {
    private final UserServiceInterface target;

    public UserServiceProxy(UserServiceInterface target) {
        this.target = target;
    }

    public static UserServiceInterface create(UserServiceInterface target) {
        return (UserServiceInterface) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                new Class[]{UserServiceInterface.class},
                new UserServiceProxy(target)
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        AccessControlUtil.checkAccess(target, method.getName(), args);
        return method.invoke(target, args);
    }
}