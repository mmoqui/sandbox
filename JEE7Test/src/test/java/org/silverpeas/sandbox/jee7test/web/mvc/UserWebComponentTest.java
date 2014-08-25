package org.silverpeas.sandbox.jee7test.web.mvc;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.silverpeas.sandbox.jee7test.messaging.EventNotifier;
import org.silverpeas.sandbox.jee7test.model.User;
import org.silverpeas.sandbox.jee7test.repository.UserRepository;
import org.silverpeas.sandbox.jee7test.util.BeanContainer;
import org.silverpeas.sandbox.jee7test.util.ServiceProvider;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.silverpeas.sandbox.jee7test.model.UserBuilder.anExistingUser;

/**
 * @author mmoquillon
 */
public class UserWebComponentTest {

  private UserWebComponent webComponent = new UserWebComponent();
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
    when(userRepository.getAllUsers()).thenReturn(Arrays.asList(
            anExistingUser(0, "Edouard", "Lafortin"), anExistingUser(1, "Rohan", "Lapointe"))
    );
  }

  @Test
  public void askingForAllUsersShouldReturnAllOfThem() {
    Parameters parameters = webComponent.getAllUsers(new Parameters());

    assertThat(parameters.contains(UserWebComponent.ALL_USERS), is(true));
    List<User> actualUsers = parameters.get(UserWebComponent.ALL_USERS);
    assertThat(actualUsers.size(), is(2));
    assertThat(actualUsers.contains(anExistingUser(0, "Edouard", "Lafortin")), is(true));
    assertThat(actualUsers.contains(anExistingUser(1, "Rohan", "Lapointe")), is(true));
  }

  @Test
  public void addANewUserShouldPersistIt() {
    final String firstName = "Toto";
    final String lastName = "Chez-les-papoos";
    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(final InvocationOnMock invocationOnMock) throws Throwable {
        User userToSave = (User) invocationOnMock.getArguments()[0];
        assertThat(userToSave.getFirstName(), is(firstName));
        assertThat(userToSave.getLastName(), is(lastName));
        when(userRepository.getAllUsers()).thenReturn(Arrays.asList(
            anExistingUser(0, "Edouard", "Lafortin"),
            anExistingUser(1, "Rohan", "Lapointe"),
            anExistingUser(2, firstName, lastName)));
        return null;
      }
    }).when(userRepository).putUser(any(User.class));

    Parameters parameters = new Parameters();
    parameters.put(UserWebComponent.FIRST_NAME, firstName);
    parameters.put(UserWebComponent.LAST_NAME, lastName);
    parameters = webComponent.createUser(parameters);

    assertThat(parameters.contains(UserWebComponent.ALL_USERS), is(true));
    List<User> actualUsers = parameters.get(UserWebComponent.ALL_USERS);
    assertThat(actualUsers.size(), is(3));
    assertThat(actualUsers.contains(anExistingUser(0, "Edouard", "Lafortin")), is(true));
    assertThat(actualUsers.contains(anExistingUser(1, "Rohan", "Lapointe")), is(true));
    assertThat(actualUsers.contains(anExistingUser(2, firstName, lastName)), is(true));
  }
}
