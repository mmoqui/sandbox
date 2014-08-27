package org.silverpeas.sandbox.jee7test.repository;

import org.silverpeas.sandbox.jee7test.repository.UserRepository;
import org.silverpeas.sandbox.jee7test.util.providers.BeanProvider;

/**
 * @author mmoquillon
 */
public class UserRepositoryProvider implements BeanProvider<UserRepository> {
  @Override
  public Class<UserRepository> getProvidedBeanType() {
    return UserRepository.class;
  }
}
