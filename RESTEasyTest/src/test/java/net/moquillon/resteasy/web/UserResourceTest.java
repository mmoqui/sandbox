/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.moquillon.resteasy.web;

import net.moquillon.resteasytest.core.User;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jboss.resteasy.test.TestPortProvider;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.InputStream;
import java.net.ResponseCache;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author mmoquillon
 */
public class UserResourceTest extends WebResourceTest {

  public UserResourceTest() {
    super("classpath:/spring-config.xml");
  }

  @Test
  public void getAnUnexistingUser() {
    Client client = ClientBuilder.newClient();
    Response response = client.target(TestPortProvider.generateURL("/users/3")).request().get();
    assertThat(response.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
  }

  @Test
  public void getExistingUser() {
    Client client = ClientBuilder.newClient();
    User user = client.target(TestPortProvider.generateURL("/users/0")).request().get(User.class);
    assertThat(user, notNullValue());
    assertThat(user.getId(), is("0"));
    assertThat(user.getFirstName(), is("Miguel"));
    assertThat(user.getLastName(), is("Moquillon"));
  }

  @Test
  public void createANewUser() {
    User user = new User("Toto", "Chez-les-Papoos");
    Client client = ClientBuilder.newClient();
    Response response = client.target(TestPortProvider.generateURL("/users")).request()
        .post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE));
    assertThat(response.getStatus(), is(Status.CREATED.getStatusCode()));
    User createdUser = response.readEntity(User.class);
    assertThat(createdUser, notNullValue());
  }

  @Test
  public void uploadAFile() {
    InputStream file = getClass().getResourceAsStream("/signature.txt");
    MultipartFormDataOutput form = new MultipartFormDataOutput();
    form.addFormData("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE, "signature.txt");
    form.addFormData("name", "my signature", MediaType.TEXT_PLAIN_TYPE);
    Client client = ClientBuilder.newClient();
    Response response = client.target(TestPortProvider.generateURL("/users/files")).request()
        .post(Entity.entity(form, MediaType.MULTIPART_FORM_DATA_TYPE));
    assertThat(response.getStatus(), is(Status.CREATED.getStatusCode()));
  }
}
