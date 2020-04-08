package com.lagou.edu.Transaction;

import java.lang.reflect.Proxy;

public class TransactionEnabledAnnotationProxyManager {

    private TransactionManager transactionManager;

    public TransactionEnabledAnnotationProxyManager(TransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    public Object proxyFor(Object object)
    {
        return Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), new AnnotationTransactionInvocationHandler(object, transactionManager));
    }

}
