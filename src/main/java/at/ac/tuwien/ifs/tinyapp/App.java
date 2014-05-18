package at.ac.tuwien.ifs.tinyapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The main application entry point.
 */
public class App extends javafx.application.Application {

    public static final String[] CONFIG_LOCATIONS = { "applicationContext.xml" };

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(CONFIG_LOCATIONS);

        BeanFactoryCallback controllerFactory = context.getBean(BeanFactoryCallback.class);

        FXMLLoader loader = new FXMLLoader();

        // instances of controllers declared by fx:controller attributes are now retrieved using the BeanFactoryCallback
        loader.setControllerFactory(controllerFactory);

        loader.setLocation(getClass().getClassLoader().getResource("fxml/MainWindow.fxml"));

        stage.setScene(new Scene((Pane) loader.load()));
        stage.setTitle("Tinyapp");
        stage.show();
    }

}
