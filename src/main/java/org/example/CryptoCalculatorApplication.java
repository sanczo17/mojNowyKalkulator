package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;

@SpringBootApplication
public class CryptoCalculatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CryptoCalculatorApplication.class, args);
        Application.launch(MainApp.class, args);
    }
}
