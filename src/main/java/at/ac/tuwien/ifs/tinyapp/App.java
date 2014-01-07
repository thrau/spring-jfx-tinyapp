package at.ac.tuwien.ifs.tinyapp;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The main application entry point.
 */
public class App extends javafx.application.Application {

    public static final String[] CONFIG_LOCATIONS = {
        "applicationContext.xml"
    };

    public static final ApplicationContext APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(CONFIG_LOCATIONS);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLSpringLoader loader = new FXMLSpringLoader(APPLICATION_CONTEXT);

        stage.setScene(new Scene((Pane) loader.load("fxml/MainWindow.fxml")));

        // Alternatively you could use
        // loader.setLocation(getClass().getClassLoader().getResource("fxml/"));
        // loader.load("MainWindow.fxml")));

        stage.setTitle("Tinyapp");
        stage.show();
    }

}
