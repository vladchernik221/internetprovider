package com.chernik.internetprovider.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an annotated class is a "service". Such classes as well as classes with annotation
 * <code>Component</code> can be injected to other via <code>@Autowired</code> annotation. Semantically this classes
 * appropriate to service level of architecture.
 *
 * @see Autowired
 * @see Component
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Service {
}
