package at.ac.tuwien.ifs.tinyapp;

import javafx.util.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * A Spring managed, ApplicationContextAware JavaFX Callback that uses the respective ApplicationContext to retrieve
 * Spring beans. If the ApplicationContext does not contain a component of the class, an attempt is made to reflectively
 * create a new instance and autowire the bean manually with an AutowireCapableBeanFactory.
 */
@Component
public class BeanFactoryCallback implements Callback<Class<?>, Object>, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(BeanFactoryCallback.class);

    private ApplicationContext applicationContext;

    @Override
    public Object call(Class<?> clazz) {
        if (getApplicationContext() == null) {
            throw new NullPointerException("Spring ApplicationContext is null");
        }

        try {
            return getApplicationContext().getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            LOG.debug("No qualifying bean of type [{}] is defined. Trying to autowire manually...", clazz.getName());
        }

        try {
            Object bean = clazz.newInstance();
            getApplicationContext().getAutowireCapableBeanFactory().autowireBean(bean);
            return bean;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new BeanInstantiationException(clazz, "No default constructor available", ex);
        }
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
