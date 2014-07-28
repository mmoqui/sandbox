/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.moquillon.resteasytest.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mmoquillon
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

  @XmlElement(nillable = false)
  private String firstName;
  @XmlElement(nillable = false)
  private String lastName;
  private String email;
  private String id;

  public static final User NO_USER = new User(null, null);

  public User(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  protected User() {

  }

  public void save() {

  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isDefined() {
    return this != NO_USER;
  }

  public boolean isNotDefined() {
    return !isDefined();
  }

  public String getId() {
    return this.id;
  }

  protected void setId(String id) {
    this.id = id;
  }

}
