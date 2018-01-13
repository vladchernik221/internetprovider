package com.chernik.internetprovider.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Uses for specifying fields that should be initialized automatically after creating object. Set object of appropriate
 * type if it annotates one of this annotations: <code>Component</code>, <code>Repository</code>, <code>Service</code>
 * or <code>HttpRequestProcessor</code>. If target field is <code>List</code>, all objects of appropriate types will be
 * added to it.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Autowired {
}
