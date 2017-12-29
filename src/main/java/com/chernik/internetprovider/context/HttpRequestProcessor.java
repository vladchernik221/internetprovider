package com.chernik.internetprovider.context;

import com.chernik.internetprovider.servlet.command.HttpRequestType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HttpRequestProcessor {
    String uri();

    HttpRequestType method() default HttpRequestType.ALL;
}
