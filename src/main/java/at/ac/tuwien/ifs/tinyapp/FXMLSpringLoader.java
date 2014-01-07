package at.ac.tuwien.ifs.tinyapp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.BuilderFactory;
import javafx.util.Callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * A stateful wrapper for an FXMLLoader that uses a spring ApplicationContext and an FXML controller factory to retrieve
 * controllers from Spring beans rather than letting the JavaFX FXMLLoader instantiate them.
 * <p/>
 * If you specify a base location using the constructor
 * {@link #FXMLSpringLoader(java.net.URL, org.springframework.context.ApplicationContext)} or
 * {@link #setLocation(java.net.URL)}, then resources specified in {@link #load(java.net.URL)} will be resolved relative
 * to the base location.
 * <p/>
 * E.g. if your .fxml files are located in a directory in your classpath {@code resources/fxml/}, then you can specify
 * this as a base location and load your resource using only the file name when calling {@link #load(String)}. This also
 * allows you to specify relative paths in {@code fx:include} resource declarations.
 * 
 */
public class FXMLSpringLoader {

    private static final Logger LOG = LoggerFactory.getLogger(FXMLSpringLoader.class);

    private ApplicationContext springContext;
    private URL location;

    private ResourceBundle resources;
    private BuilderFactory builderFactory;

    /**
     * Creates a new FXMLSpringLoader for the given application context.
     * 
     * @param springContext the Spring context to load the controller beans from
     */
    public FXMLSpringLoader(ApplicationContext springContext) {
        this(null, springContext);
    }

    /**
     * Creates a new FXMLSpringLoader for the given application context.
     * 
     * @param location the base location of the {@code *.fxml} files
     * @param springContext the Spring context to load the controller beans from
     */
    public FXMLSpringLoader(URL location, ApplicationContext springContext) {
        setLocation(location);
        setSpringContext(springContext);
    }

    /**
     * Loads an object hierarchy from a FXML document. The resource path may be relative to the base location, if you
     * have specified one.
     * 
     * @param fxml the path to the fxml resource
     * @param <T> the type of the object that is loaded
     * @return the loaded object
     * @throws IOException if a read error occurs while loading the .fxml resource
     */
    public <T> T load(String fxml) throws IOException {
        return load(resolveUrl(fxml));
    }

    /**
     * Loads an object hierarchy from a FXML document. The resource path may be relative to the base location, if you
     * have specified one.
     * 
     * @param fxml the url to the fxml resource
     * @param <T> the type of the object that is loaded
     * @return the loaded object
     * @throws IOException if a read error occurs while loading the .fxml resource
     */
    public <T> T load(URL fxml) throws IOException {
        BuilderFactory bf = (getBuilderFactory() != null) ? getBuilderFactory() : new JavaFXBuilderFactory();

        return FXMLLoader.load(fxml, getResources(), bf, new ControllerFactory());
    }

    public ApplicationContext getSpringContext() {
        return springContext;
    }

    public void setSpringContext(ApplicationContext springContext) {
        this.springContext = springContext;
    }

    public URL getLocation() {
        return location;
    }

    public void setLocation(URL location) {
        this.location = location;
    }

    public ResourceBundle getResources() {
        return resources;
    }

    public void setResources(ResourceBundle resources) {
        this.resources = resources;
    }

    public BuilderFactory getBuilderFactory() {
        return builderFactory;
    }

    public void setBuilderFactory(BuilderFactory builderFactory) {
        this.builderFactory = builderFactory;
    }

    private URL resolveUrl(String url) throws IOException {
        if (getLocation() != null) {
            return new URL(getLocation(), url);
        } else {
            return getClass().getClassLoader().getResource(url);
        }
    }

    private class ControllerFactory implements Callback<Class<?>, Object> {

        @Override
        public Object call(Class<?> clazz) {
            if (getSpringContext() == null) {
                throw new NullPointerException("Spring ApplicationContext is null");
            }

            try {
                return getSpringContext().getBean(clazz);
            } catch (NoSuchBeanDefinitionException e) {
                LOG.debug("No qualifying bean of type [{}] is defined. Trying to autowire manually...",
                    clazz.getName());
            }

            try {
                Object bean = clazz.newInstance();
                getSpringContext().getAutowireCapableBeanFactory().autowireBean(bean);
                return bean;
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new BeanInstantiationException(clazz, "No default constructor available", ex);
            }
        }
    }
}
