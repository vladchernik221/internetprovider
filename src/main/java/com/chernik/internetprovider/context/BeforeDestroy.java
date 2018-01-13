package com.chernik.internetprovider.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Uses for specifying methods, that should be invoked directly before destroying object.
 *
 * @see AfterCreate
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeDestroy {
}
