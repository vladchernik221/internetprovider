package com.chernik.internetprovider.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Uses for specifying methods, that should be invoked immediately after creating object.
 *
 * @see BeforeDestroy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterCreate {
}
