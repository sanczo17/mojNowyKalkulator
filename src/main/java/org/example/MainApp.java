package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.example.controller.FXMLController;
import java.util.ResourceBundle;
import org.example.util.LanguageManager;

public class MainApp extends Application {
    private ConfigurableApplicationContext springContext;
    private VBox rootNode;

    @Override
    public void init() throws Exception {
        springContext = new SpringApplicationBuilder(CryptoCalculatorApplication.class).run();

        LanguageManager languageManager = springContext.getBean(LanguageManager.class);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("messages", languageManager.getLocale()));
        fxmlLoader.setControllerFactory(springContext::getBean);

        rootNode = fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) {

        FXMLController controller = springContext.getBean(FXMLController.class);
        controller.setPrimaryStage(stage);


        Scene scene = new Scene(rootNode);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        if(springContext != null) {
            springContext.close();
        }
    }

    public static void main(String[] args) {
        launch(MainApp.class, args);
    }
}
