package org.silverpeas.sandbox.jee7test.model;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import org.hamcrest.CoreMatchers;
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
import org.silverpeas.sandbox.jee7test.repository.UserRepository;
import org.silverpeas.sandbox.jee7test.service.MessageBucket;
import org.silverpeas.sandbox.jee7test.util.ServiceProvider;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import java.io.File;
import java.util.List;
import java.util.Timer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.silverpeas.sandbox.jee7test.model.UserBuilder.aNewUser;
import static org.silverpeas.sandbox.jee7test.model.UserBuilder.anExistingUser;

/**
 * @author mmoquillon
 */

/**
 * In a such test, we check the named queries are correct.
 */
@RunWith(Arquillian.class)
public class UserPersistenceIntegrationTest {

  public static final Operation CLEAN_UP = Operations.deleteAllFrom("User");
  public static final Operation USER_SET_UP = Operations.insertInto("User")
      .columns("id", "firstName", "lastName")
      .values(0, "Edouard", "Lafortin")
      .values(1, "Rohan", "Lapointe")
      .build();

  @Inject
  private MessageBucket messageBucket;
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
        .addPackages(true, "org.silverpeas.sandbox.jee7test.util")
        .addAsLibraries(libs)
        .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
        .addAsResource("META-INF/services/test-org.silverpeas.sandbox.jee7test.util.BeanContainer",
            "META-INF/services/org.silverpeas.sandbox.jee7test.util.BeanContainer")
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
        .addAsWebInfResource("test-ds.xml", "test-ds.xml");
  }

  @Test
  @Ignore
  public void emptyTest() {
  }

  @Test
  public void getAnExistingUserShouldReturnNothing() {
    dbSetupTracker.skipNextLaunch();
    User user = User.getById("2");
    assertThat(user, nullValue());
  }

  @Test
  public void getAUserByItsLastNameShouldReturnIt() {
    dbSetupTracker.skipNextLaunch();
    User expectedUser = anExistingUser(1, "Rohan", "Lapointe");
    List<User> users = User.getByLastName(expectedUser.getLastName());
    assertThat(users.size(), is(1));
    assertThat(users.get(0), is(expectedUser));
  }

  @Test
  public void persistANewUserShouldCreateIt() {
    User newUser = aNewUser("Toto", "Chez-les-papoos");
    newUser.save();
    assertThat(newUser.getId(), notNullValue());

    User actualUser = User.getById(newUser.getId());
    assertThat(actualUser, is(newUser));
  }

  @Test
  public void persistANewUserShouldNotifyIt() throws InterruptedException {
    User newUser = aNewUser("Toto", "Chez-les-papoos");
    newUser.save();
    assertThat(newUser.getId(), notNullValue());

    Thread.sleep(1000); // wait for the JMS notification

    assertThat(messageBucket.getContent().size(), is(1));
  }
}
