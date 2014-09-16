package org.silverpeas.sandbox.jee7test.repository;

import org.silverpeas.sandbox.jee7test.model.UserGroup;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mmoquillon
 */
public class UserGroupDAO {

  private static final String CREATE_TABLE = "create table if not exists UserGroup (id bigint not null primary key, name varchar(32) not null)";
  private static final String INSERT = "insert into UserGroup (id, name) values (?, ?)";
  private static final String ALL = "select id, name from UserGroup";
  private static long currentId = 0;

  @Resource(mappedName = "java:/datasources/jee7test")
  private DataSource dataSource;

  @PostConstruct
  protected void createTable() {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(CREATE_TABLE)) {
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<UserGroup> getAllUserGroups() {
    List<UserGroup> groups = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(ALL);
         ResultSet rs = statement.executeQuery()) {
      while(rs.next()) {
          long id = rs.getLong("id");
          String name = rs.getString("name");
          UserGroup userGroup = new UserGroup(name);
          setId(userGroup, id);
          groups.add(userGroup);
        }
    } catch (SQLException|IllegalAccessException|NoSuchFieldException e) {
      e.printStackTrace();
    }
    return groups;
  }

  public void createUserGroup(final UserGroup userGroup) {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(INSERT)) {
      statement.setLong(1, currentId);
      statement.setString(2, userGroup.getName());
      statement.executeUpdate();
      setId(userGroup, currentId++);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  private void setId(UserGroup userGroup, long id)
      throws NoSuchFieldException, IllegalAccessException {
    Field idField = UserGroup.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(userGroup, id);
  }
}
