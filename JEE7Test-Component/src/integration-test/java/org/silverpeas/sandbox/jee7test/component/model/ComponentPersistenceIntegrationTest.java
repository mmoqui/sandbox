package org.silverpeas.sandbox.jee7test.component.model;

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
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.silverpeas.sandbox.jee7test.component.model.ComponentBuilder.aNewComponent;
import static org.silverpeas.sandbox.jee7test.component.model.ComponentBuilder.anExistingComponent;

/**
 * @author mmoquillon
 */

/**
 * In a such test, we check the named queries are correct.
 */
@RunWith(Arquillian.class)
public class ComponentPersistenceIntegrationTest {

  public static final Operation CLEAN_UP = Operations.deleteAllFrom("Component");
  public static final Operation USER_SET_UP =
      Operations.insertInto("Component").columns("id", "name", "type")
          .values(0, "Edouard", "GED")
          .values(1, "Rohan", "Gallery")
          .build();

  @Resource
  private DataSource dataSource;
  private DbSetupTracker dbSetupTracker = new DbSetupTracker();

  @Before
  public void prepareDataSource() {
    Operation preparation = Operations.sequenceOf(CLEAN_UP, USER_SET_UP);
    DbSetup dbSetup = new DbSetup(new DataSourceDestination(dataSource), preparation);
    dbSetupTracker.launchIfNecessary(dbSetup);
  }

  @Deployment
  public static Archive<?> createTestArchive() {
    File[] libs = Maven.resolver().loadPomFromFile("pom.xml")
        .resolve("org.mockito:mockito-all", "com.ninja-squad:DbSetup").withTransitivity().asFile();
    return ShrinkWrap.create(WebArchive.class, "test.war")
        .addPackages(true, "org.silverpeas.sandbox.jee7test.component")
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
  public void getAnExistingComponentShouldReturnNothing() {
    dbSetupTracker.skipNextLaunch();
    Component component = Component.getById("2");
    assertThat(component, nullValue());
  }

  @Test
  public void getAComponentByItsTypeShouldReturnIt() {
    dbSetupTracker.skipNextLaunch();
    Component expectedComponent = anExistingComponent(1, "Rohan", "Gallery");
    List<Component> components = Component.getByType(expectedComponent.getType());
    assertThat(components.size(), is(1));
    assertThat(components.get(0), is(expectedComponent));
  }

  @Test
  public void persistANewComponentShouldCreateIt() {
    Component newComponent = aNewComponent("Toto", "GED");
    newComponent.save();
    assertThat(newComponent.getId(), notNullValue());

    Component actualComponent = Component.getById(newComponent.getId());
    assertThat(actualComponent, is(newComponent));
  }
}
