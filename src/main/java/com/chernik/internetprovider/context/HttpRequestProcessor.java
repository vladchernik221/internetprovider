package com.chernik.internetprovider.context;

import com.chernik.internetprovider.servlet.command.RequestType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an annotated class is a "request processor". Such classes as well as classes with annotation
 * <code>Component</code> can be injected to other via <code>@Autowired</code> annotation. Semantically this classes
 * appropriate to controller level of architecture.
 *
 * @see Autowired
 * @see Component
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HttpRequestProcessor {
    /**
     * For specifying URI, that should be processed by this request processor.
     *
     * @return URI (absolute path, e.g. <code>/example/new</code>)
     */
    String uri();

    /**
     * For specifying request type, that should be processed by this request processor.
     *
     * @return request type, default value is <code>RequestType.ERROR</code>.
     * @see RequestType
     */
    RequestType method() default RequestType.ERROR;
}
