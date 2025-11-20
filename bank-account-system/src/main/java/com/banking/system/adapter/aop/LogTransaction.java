package com.banking.system.adapter.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogTransaction {
    String type();  // "DEPOSIT", "WITHDRAW", "TRANSFER"
    String description() default "";
}