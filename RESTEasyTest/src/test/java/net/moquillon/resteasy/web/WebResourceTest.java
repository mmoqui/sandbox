/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.moquillon.resteasy.web;

import net.moquillon.resteasytest.web.UsersResource;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.jboss.resteasy.plugins.spring.SpringBeanProcessor;
import org.jboss.resteasy.plugins.spring.SpringResourceFactory;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.springmvc.tjws.TJWSEmbeddedSpringMVCServer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.ws.rs.core.Application;

/**
 *
 * @author mmoquillon
 */
public class WebResourceTest {

  //private static final TJWSEmbeddedJaxrsServer server = new TJWSEmbeddedJaxrsServer();
  private static JAXRSWebServer server;
  private static final String CLASSPATH_KEYWORK = "classpath:";
  private long s;

  @Before
  public  void bootstrapWebContainer() throws Exception {
    s = System.currentTimeMillis();
    server = new JAXRSWebServer("classpath:/spring-config.xml");
    server.start();
    /*server.setPort(8081);
    server.start();

    ConfigurableApplicationContext factory = new ClassPathXmlApplicationContext("/spring-config.xml");
    factory.start();
    SpringBeanProcessor springProcessor = new MySpringBeanProcessor(server.getDeployment());
    springProcessor.postProcessBeanFactory(factory.getBeanFactory());*/
  }

  @After
  public  void shutdownWebContainer() throws Exception {
    server.stop();
    System.out.println("DURATION: " + (System.currentTimeMillis() - s));
  }

  public WebResourceTest(String springContext) {
    /*String classpath = springContext;
    if (springContext.startsWith(CLASSPATH_KEYWORK)) {
      classpath = springContext.substring(CLASSPATH_KEYWORK.length());
    }
    ConfigurableListableBeanFactory factory = new XmlBeanFactory(
        new ClassPathResource(classpath));
    SpringBeanProcessor springProcessor = new SpringBeanProcessor(server.getDeployment());
    springProcessor.postProcessBeanFactory(factory);*/
  }
}
