package org.silverpeas.sandbox.jee7test.security.annotation;

import org.silverpeas.sandbox.jee7test.security.SocialNetworkService;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author mmoquillon
 */
@Qualifier
@Inherited
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface SocialNetwork {

  SocialNetworkService value();
}
