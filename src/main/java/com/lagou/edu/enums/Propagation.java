package com.lagou.edu.enums;


/**
 * 事务传播行为
 */
public enum Propagation {

    /**
     * 如果当前没有事务，就新建一个事务，如果已经存在一个事务中， 加入到这个事务中。这是最常⻅的选择
     */
    REQUIRED(0),

    /**
     * 支持当前事务，如果当前没有事务，就以非事务方式执行
     */
    SUPPORTS(1),


    /**
     * 使用当前的事务，如果当前没有事务，就抛出异常
     */
    MANDATORY(2),


    /**
     * 新建事务，如果当前存在事务，把当前事务挂起
     */
    REQUIRES_NEW(3),

    /**
     * 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
     */
    NOT_SUPPORTED(4),

    /**
     * 以非事务方式执行，如果当前存在事务，则抛出异常
     */
    NEVER(5),

    /**
     * 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则 执行与PROPAGATION_REQUIRED类似的操作
     */
    NESTED(6);


    private final int value;


    Propagation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
