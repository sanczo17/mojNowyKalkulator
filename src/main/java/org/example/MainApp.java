package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.example.util.LanguageManager;
import java.util.ResourceBundle;  // Dodaj ten import

public class MainApp extends Application {

    private ConfigurableApplicationContext springContext;
    private LanguageManager languageManager;
    private VBox rootNode;

    @Override
    public void init() throws Exception {
        springContext = new SpringApplicationBuilder(CryptoCalculatorApplication.class).run();
        languageManager = springContext.getBean(LanguageManager.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));

        // Ustawienie zasobów dla FXMLLoader
        fxmlLoader.setResources(ResourceBundle.getBundle("messages", languageManager.getLocale()));

        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(rootNode);
        stage.setTitle(languageManager.getString("title"));
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }

    public static void main(String[] args) {
        launch(MainApp.class, args);
    }
}
