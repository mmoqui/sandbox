package net.moquillon.resteasy.web;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.jboss.resteasy.plugins.spring.SpringContextLoaderListener;
import org.eclipse.jetty.server.Server;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextListener;

/**
 * @author mmoquillon
 */
public class JAXRSWebServer {

  private final Server jettyServer;

  private final ServletContextHandler servletContextHandler;

  public JAXRSWebServer(String springContext) throws Exception {

    jettyServer = new Server();
    jettyServer.setSendServerVersion(false);
    jettyServer.setStopAtShutdown(false);

    servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SECURITY);
    servletContextHandler.setContextPath("/");
    // listeners order is important!
    servletContextHandler.addEventListener(new ResteasyBootstrap());
    servletContextHandler.addEventListener(new SpringContextLoaderListener());
    servletContextHandler.addEventListener(new RequestContextListener());
    servletContextHandler.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, springContext);

    createHttpConnector();

    createRestEasyServlet();

    HandlerList handlers = new HandlerList();
    handlers.addHandler(servletContextHandler);
    jettyServer.setHandler(handlers);
  }

  public void start() throws Exception {
    jettyServer.start();
  }

  public void stop() throws Exception {
    jettyServer.stop();
  }

  private void createHttpConnector() {
    Connector httpConnector = new SelectChannelConnector();
    httpConnector.setPort(8081);
    httpConnector.setMaxIdleTime(30000);
    httpConnector.setRequestHeaderSize(8192);
    jettyServer.addConnector(httpConnector);
  }

  private void createRestEasyServlet() {
    ServletHolder servletHolder = new ServletHolder(new HttpServletDispatcher());
    //servletHolder.setInitParameter("resteasy.servlet.mapping.prefix", "/services");
    servletHolder.setInitOrder(1);
    servletContextHandler.addServlet(servletHolder, "/*");
  }
}
