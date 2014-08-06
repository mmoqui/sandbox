package org.silverpeas.sandbox.jee7test.web.rest.annotation;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author mmoquillon
 */
@InterceptorBinding
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Authenticated {
}
