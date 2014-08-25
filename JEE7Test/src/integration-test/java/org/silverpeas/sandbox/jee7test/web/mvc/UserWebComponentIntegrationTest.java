package org.silverpeas.sandbox.jee7test.web.mvc;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.silverpeas.sandbox.jee7test.model.User;
import org.silverpeas.sandbox.jee7test.model.UserBuilder;
import org.silverpeas.sandbox.jee7test.service.MessageBucket;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.silverpeas.sandbox.jee7test.model.UserBuilder.anExistingUser;

/**
 * @author mmoquillon
 */
@RunWith(Arquillian.class)
public class UserWebComponentIntegrationTest {

  public static final Operation CLEAN_UP = Operations.deleteAllFrom("User");
  public static final Operation USER_SET_UP = Operations.insertInto("User")
      .columns("id", "firstName", "lastName")
      .values(0, "Edouard", "Lafortin")
      .values(1, "Rohan", "Lapointe")
      .build();

  @Inject
  private UserWebComponent webComponent;
  @Resource
  private DataSource dataSource;
  private DbSetupTracker dbSetupTracker = new DbSetupTracker();

  @Before
  public void prepareDataSource() {
    Operation preparation = Operations.sequenceOf(
        CLEAN_UP,
        USER_SET_UP);
    DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), preparation);
    dbSetupTracker.launchIfNecessary(dbSetup);
  }

  @Deployment
  public static Archive<?> createTestArchive() {
    File[] libs = Maven.resolver()
        .loadPomFromFile("pom.xml")
        .resolve("org.mockito:mockito-all", "com.ninja-squad:DbSetup")
        .withTransitivity()
        .asFile();
    return ShrinkWrap.create(WebArchive.class, "test.war")
        .addPackage("org.silverpeas.sandbox.jee7test.model")
        .addPackage("org.silverpeas.sandbox.jee7test.repository")
        .addPackage("org.silverpeas.sandbox.jee7test.messaging")
        .addPackage("org.silverpeas.sandbox.jee7test.service")
        .addPackage("org.silverpeas.sandbox.jee7test.web.mvc")
        .addPackage("org.silverpeas.sandbox.jee7test.util")
        .addAsLibraries(libs)
        .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
        .addAsWebInfResource("test-ds.xml", "test-ds.xml");
  }

  @Test
  @Ignore
  public void emptyTest() {
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
