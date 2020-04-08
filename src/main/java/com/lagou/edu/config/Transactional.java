package com.lagou.edu.config;


import com.lagou.edu.enums.Isolation;
import com.lagou.edu.enums.Propagation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transactional {

}
