package org.silverpeas.sandbox.jee7test.web.rest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.silverpeas.sandbox.jee7test.messaging.EventNotifier;
import org.silverpeas.sandbox.jee7test.model.User;
import org.silverpeas.sandbox.jee7test.repository.UserRepository;
import org.silverpeas.sandbox.jee7test.repository.UserRepositoryProvider;
import org.silverpeas.sandbox.jee7test.test.util.Reflection;
import org.silverpeas.sandbox.jee7test.test.util.TestBeanContainer;
import org.silverpeas.sandbox.jee7test.util.BeanContainer;
import org.silverpeas.sandbox.jee7test.util.ServiceProvider;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.silverpeas.sandbox.jee7test.model.UserBuilder.*;
import static org.silverpeas.sandbox.jee7test.test.util.TestBeanContainer.getMockedBeanContainer;

/**
 * @author mmoquillon
 */
public class UserResourceTest {

  private UserResource userResource = new UserResource();
  private UserRepository userRepository;
  private EventNotifier eventNotifier;

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Before
  public void prepareCommonBehaviors() throws Exception {
    BeanContainer beanContainer = getMockedBeanContainer();
    UserRepositoryProvider userRepositoryProvider = mockUserRepositoryProvider();
    userRepository = mock(UserRepository.class);
    eventNotifier = mock(EventNotifier.class);

    UriInfo uriInfo = mock(UriInfo.class);
    UriBuilder uriBuilder = mock(UriBuilder.class);

    Field dependency = UserResource.class.getDeclaredField("uriInfo");
    Reflection.setField(userResource, dependency, uriInfo);

    when(userRepositoryProvider.getBean()).thenReturn(userRepository);
    when(beanContainer.getBeanByType(EventNotifier.class)).thenReturn(eventNotifier);
    when(userRepository.getAllUsers()).thenReturn(Arrays.asList(
            anExistingUser(0, "Edouard", "Lafortin"), anExistingUser(1, "Rohan", "Lapointe"))
    );
    when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
    when(uriBuilder.build(any())).thenReturn(new URI("http://localhost/test/services/users/1"));
  }

  @Test
  public void askingForAllUsersShouldReturnAllOfThem() {
    List<User> allUsers = userResource.getUsers();
    assertThat(allUsers.size(), is(2));
    assertThat(allUsers.contains(anExistingUser(0, "Edouard", "Lafortin")), is(true));
    assertThat(allUsers.contains(anExistingUser(1, "Rohan", "Lapointe")), is(true));
  }

  @Test
  public void askingForAnUnexistingUserShouldThrownAWebApplicationException() {
    exception.expect(WebApplicationException.class);
    exception.expectMessage(Response.Status.NOT_FOUND.getReasonPhrase());

    userResource.getUser("0");
  }

  @Test
  public void askingForAnExistingUserShouldReturnIt() {
    User expectedUser = anExistingUser(0, "Toto", "Chez-les-papoos");
    when(userRepository.getUserById(expectedUser.getId())).thenReturn(expectedUser);

    User actualUser = userResource.getUser(expectedUser.getId());
    assertThat(actualUser, is(expectedUser));
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

    Response response = userResource.newUser(aNewUser(firstName, lastName));
    assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

    List<User> users = (List<User>) response.getEntity();
    assertThat(users.size(), is(3));
    assertThat(users.contains(anExistingUser(0, "Edouard", "Lafortin")), is(true));
    assertThat(users.contains(anExistingUser(1, "Rohan", "Lapointe")), is(true));
    assertThat(users.contains(anExistingUser(2, firstName, lastName)), is(true));
  }
}
