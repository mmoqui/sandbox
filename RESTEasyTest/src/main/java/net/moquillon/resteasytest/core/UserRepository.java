/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.moquillon.resteasytest.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mmoquillon
 */
@Repository
public class UserRepository {

  private final Map<String, User> repository = new HashMap<String, User>();
  private static long count = 0;

  public void put(final User user) {
    user.setId(String.valueOf(count++));
    repository.put(user.getId(), user);
  }

  public List<User> findAll() {
    return new ArrayList<User>(repository.values());
  }

  public User findById(String id) {
    User user = repository.get(id);
    return (user == null ? User.NO_USER : user);
  }

  public long count() {
    return repository.size();
  }
}
