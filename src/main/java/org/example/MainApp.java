package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.example.controller.FXMLController;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.example.util.LanguageManager;
import java.util.ResourceBundle;

public class MainApp extends Application {

    private ConfigurableApplicationContext springContext;
    private LanguageManager languageManager;
    private VBox rootNode;
    private Stage primaryStage;

    @Override
    public void init() throws Exception {
        springContext = new SpringApplicationBuilder(CryptoCalculatorApplication.class).run();
        languageManager = springContext.getBean(LanguageManager.class);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("messages", languageManager.getLocale()));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }
    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("messages", languageManager.getLocale()));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();

        FXMLController controller = fxmlLoader.getController();
        if (controller != null) {
            controller.setPrimaryStage(primaryStage);
        } else {
            throw new RuntimeException("FXMLController could not be loaded.");
        }
        Scene scene = new Scene(rootNode);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        updateTitle();
        primaryStage.show();
    }
    private void updateTitle() {
        if (primaryStage != null && languageManager != null) {
            primaryStage.setTitle(languageManager.getString("title"));
        }
    }
    @Override
    public void stop() throws Exception {
        springContext.close();
    }
    public static void main(String[] args) {
        launch(MainApp.class, args);
    }
}
