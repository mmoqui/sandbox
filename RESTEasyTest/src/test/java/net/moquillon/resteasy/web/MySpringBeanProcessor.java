package net.moquillon.resteasy.web;

import org.jboss.resteasy.plugins.spring.SpringBeanProcessor;
import org.jboss.resteasy.plugins.spring.SpringResourceFactory;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author mmoquillon
 */
public class MySpringBeanProcessor extends SpringBeanProcessor {

  public MySpringBeanProcessor(final ResteasyDeployment deployment) {
    super(deployment);
  }

  @Override
  public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
    super.postProcessBeanFactory(beanFactory);
    for (SpringResourceFactory resourceFactory : resourceFactories.values())
    {
      getRegistry().addResourceFactory(resourceFactory, resourceFactory.getContext());
    }
  }
}
