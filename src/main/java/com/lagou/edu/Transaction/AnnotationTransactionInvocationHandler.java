package com.lagou.edu.Transaction;

import com.lagou.edu.config.Transactional;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AnnotationTransactionInvocationHandler implements InvocationHandler {

    private Object proxied;
    private TransactionManager transactionManager;

    AnnotationTransactionInvocationHandler(Object object, TransactionManager transactionManager)
    {
        this.proxied = object;
        this.transactionManager = transactionManager;
    }

    public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable
    {
        Method originalMethod = proxied.getClass().getMethod(method.getName(), method.getParameterTypes());
        if (!originalMethod.isAnnotationPresent(Transactional.class))
        {
            return method.invoke(proxied, objects);
        }
        transactionManager.beginTransaction();
        Object result = null;
        try
        {
            result = method.invoke(proxied, objects);
            transactionManager.commit();
        } catch (Exception e)
        {
            transactionManager.rollback();
        } finally
        {
            transactionManager.close();
        }
        return result;
    }
}