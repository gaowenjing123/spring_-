package com.lagou.edu.enums;


public enum Isolation {

    DEFAULT(0),


    READ_UNCOMMITTED(1),


    READ_COMMITTED(2),


    REPEATABLE_READ(3),


    SERIALIZABLE(4);


    private final int value;


    Isolation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
