package org.silverpeas.sandbox.jee7test.model;

import org.junit.Before;
import org.junit.Test;
import org.silverpeas.sandbox.jee7test.messaging.EventNotifier;
import org.silverpeas.sandbox.jee7test.repository.UserRepository;
import org.silverpeas.sandbox.jee7test.util.BeanContainer;
import org.silverpeas.sandbox.jee7test.util.ServiceProvider;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.silverpeas.sandbox.jee7test.model.UserBuilder.aNewUser;
import static org.silverpeas.sandbox.jee7test.model.UserBuilder.anExistingUser;

/**
 * @author mmoquillon
 */
public class UserTest {

  private UserRepository userRepository;
  private EventNotifier eventNotifier;

  @Before
  public void prepareCommonBehaviors() {
    BeanContainer beanContainer = mock(BeanContainer.class);
    ServiceProvider.setBeanContainer(beanContainer);
    userRepository = mock(UserRepository.class);
    eventNotifier = mock(EventNotifier.class);

    when(beanContainer.getBeanByType(UserRepository.class)).thenReturn(userRepository);
    when(beanContainer.getBeanByType(EventNotifier.class)).thenReturn(eventNotifier);
    when(userRepository.getAllUsers()).thenReturn(Collections.<User>emptyList());
  }

  @Test
  public void askForAnExistingUserShouldReturnNull() {
    User user = User.getById("0");
    assertThat(user, nullValue());
  }

  @Test
  public void askForAnExistingUserShouldReturnIt() {
    User expectedUser = anExistingUser(0, "Toto", "Chez-les-papoos");
    when(userRepository.getUserById("0")).thenReturn(expectedUser);

    User actualUser = User.getById(expectedUser.getId());
    assertThat(actualUser, notNullValue());
    assertThat(actualUser, is(expectedUser));
  }

  @Test
  public void withNoUsersAskForAllUsersShouldReturnNothing() {
    List<User> allUsers = User.getAll();
    assertThat(allUsers.isEmpty(), is(true));
  }

  @Test
  public void withSomeUsersAskForAllUsersShouldReturnThem() {
    when(userRepository.getAllUsers()).thenReturn(Arrays.asList(
        anExistingUser(0, "Toto", "Chez-les-papoos"),
        anExistingUser(1, "Bob", "Lapointe")));

    List<User> allUsers = User.getAll();
    assertThat(allUsers.size(), is(2));
    assertThat(allUsers.contains(anExistingUser(0, "Toto", "Chez-les-papoos")), is(true));
    assertThat(allUsers.contains(anExistingUser(1, "Bob", "Lapointe")), is(true));
  }

  @Test
  public void whenSavingAUserANotificationShouldBeSent() {
    User user = aNewUser("Toto", "Chez-les-papoos");
    user.save();

    String event =
        MessageFormat.format(User.USER_CREATED_PATTERN, user.getFirstName(), user.getLastName());
    verify(userRepository).putUser(user);
    verify(eventNotifier).notify(event);
  }
}
