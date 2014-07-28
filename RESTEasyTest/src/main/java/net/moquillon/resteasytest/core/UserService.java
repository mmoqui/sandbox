/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.moquillon.resteasytest.core;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

/**
 *
 * @author mmoquillon
 */
@Service
public class UserService {

  @Inject
  private UserRepository repository;

  @PostConstruct
  public void preCreateSomeUsers() {
    User user = new User("Miguel", "Moquillon");
    repository.put(user);
  }

  public User getUser(String id) {
    return repository.findById(id);
  }

  public void persistUser(final User user) {
    repository.put(user);
  }

  public List<User> getAllUsers() {
    return repository.findAll();
  }
}
