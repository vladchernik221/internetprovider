package com.chernik.internetprovider.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an annotated class is a "component". Such classes can be injected to other via <code>@Autowired</code>
 * annotation.
 *
 * @see Autowired
 * @see Repository
 * @see Service
 * @see HttpRequestProcessor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
}
