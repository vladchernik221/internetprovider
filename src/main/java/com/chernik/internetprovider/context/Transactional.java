package com.chernik.internetprovider.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Uses for method in class annotated as {@link Service}.
 * Annotation guarantees that the method will be executed in one transaction.
 *
 * @see Service
 * @see TransactionManager
 * @see ServiceInvocationHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transactional {
}
