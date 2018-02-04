package com.chernik.internetprovider.context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceInvocationHandler implements InvocationHandler {
    private TransactionManager transactionManager;
    private Object service;

    ServiceInvocationHandler(TransactionManager transactionManager, Object service) {
        this.transactionManager = transactionManager;
        this.service = service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!service.getClass().getMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Transactional.class)) {
            return invokeNotTransactional(method, args);
        } else {
            return invokeTransactional(method, args);
        }
    }

    private Object invokeNotTransactional(Method method, Object[] args) throws Throwable {
        Object returnObject;

        try {
            returnObject = method.invoke(service, args);
        } catch (Throwable th) {
            throw th.getCause();
        }

        return returnObject;
    }

    private Object invokeTransactional(Method method, Object[] args) throws Throwable {
        Object returnObject;

        transactionManager.openTransaction();
        try {
            returnObject = method.invoke(service, args);
            transactionManager.commit();
        } catch (Throwable th) {
            transactionManager.rollback();
            throw th.getCause();
        }

        return returnObject;
    }
}
