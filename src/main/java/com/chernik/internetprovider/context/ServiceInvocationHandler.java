package com.chernik.internetprovider.context;

import com.chernik.internetprovider.persistence.TransactionalConnectionPool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceInvocationHandler implements InvocationHandler {
    private TransactionalConnectionPool transactionalConnectionPool;
    private Object service;

    ServiceInvocationHandler(TransactionalConnectionPool transactionalConnectionPool, Object service) {
        this.transactionalConnectionPool = transactionalConnectionPool;
        this.service = service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!service.getClass().getMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Transactional.class)) {
            return method.invoke(service, args);
        }

        Object returnObject;

        transactionalConnectionPool.openTransaction();
        try {
            returnObject = method.invoke(service, args);
            transactionalConnectionPool.commit();
        } catch (Throwable th) {
            transactionalConnectionPool.rollback();
            throw th;
        }

        return returnObject;
    }
}
