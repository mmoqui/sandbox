package org.silverpeas.sandbox.jee7test.web.rest;

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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.silverpeas.sandbox.jee7test.model.User;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.silverpeas.sandbox.jee7test.model.UserBuilder.aNewUser;
import static org.silverpeas.sandbox.jee7test.model.UserBuilder.anExistingUser;

/**
 * @author mmoquillon
 */
@RunWith(Arquillian.class)
public class UserResourceIntegrationTest {

  public static final Operation CLEAN_UP = Operations.deleteAllFrom("User");
  public static final Operation USER_SET_UP = Operations.insertInto("User")
      .columns("id", "firstName", "lastName")
      .values(0, "Edouard", "Lafortin")
      .values(1, "Rohan", "Lapointe")
      .build();

  @Resource(lookup = "java:/datasources/jee7test")
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
        .resolve("org.mockito:mockito-all",
            "com.ninja-squad:DbSetup",
            "org.springframework.social:spring-social-linkedin",
            "org.springframework.social:spring-social-facebook")
        .withTransitivity()
        .asFile();
    return ShrinkWrap.create(WebArchive.class, "test.war")
        .addPackage("org.silverpeas.sandbox.jee7test.model")
        .addPackage("org.silverpeas.sandbox.jee7test.repository")
        .addPackage("org.silverpeas.sandbox.jee7test.messaging")
        .addPackage("org.silverpeas.sandbox.jee7test.service")
        .addPackage("org.silverpeas.sandbox.jee7test.security")
        .addPackages(true, "org.silverpeas.sandbox.jee7test.web")
        .addPackages(true, "org.silverpeas.sandbox.jee7test.util")
        .addAsLibraries(libs)
        .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
        .addAsResource("META-INF/services/test-org.silverpeas.sandbox.jee7test.util.BeanContainer",
            "META-INF/services/org.silverpeas.sandbox.jee7test.util.BeanContainer")
        .addAsResource("test-socialNetwork.properties", "socialNetwork.properties")
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
        .addAsWebInfResource("test-ds.xml", "test-ds.xml");
  }

  @Test
  @Ignore
  public void emptyTest() {
  }

  @Test
  public void askingForAllUsersShouldReturnAllOfThem() {
    Client client = ClientBuilder.newClient();
    Response response = client.target("http://localhost:8080/test/services/users")
        .request(MediaType.APPLICATION_JSON)
        .header("Authorization", "basic dG90bzp0b3Rv")
        .get();
    assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

    List<User> actualUsers = Arrays.asList(response.readEntity(User[].class));
    assertThat(actualUsers.size(), is(2));
    assertThat(actualUsers.contains(anExistingUser(0, "Edouard", "Lafortin")), is(true));
    assertThat(actualUsers.contains(anExistingUser(1, "Rohan", "Lapointe")), is(true));
  }

  @Test
  public void addANewUserShouldPersistIt() {
    final String firstName = "Toto";
    final String lastName = "Chez-les-papoos";
    Client client = ClientBuilder.newClient();
    Response response = client.target("http://localhost:8080/test/services/users")
        .request(MediaType.APPLICATION_JSON)
        .header("Authorization", "basic dG90bzp0b3Rv")
        .post(Entity.entity(aNewUser(firstName, lastName), MediaType.APPLICATION_JSON_TYPE));
    assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

    List<User> actualUsers = Arrays.asList(response.readEntity(User[].class));
    assertThat(actualUsers.size(), is(3));
    assertThat(actualUsers.contains(anExistingUser(0, "Edouard", "Lafortin")), is(true));
    assertThat(actualUsers.contains(anExistingUser(1, "Rohan", "Lapointe")), is(true));
    assertThat(actualUsers.contains(anExistingUser(2, firstName, lastName)), is(true));
  }
}
