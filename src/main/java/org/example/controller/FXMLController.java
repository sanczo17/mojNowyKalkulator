package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.example.model.CryptoCalculator;
import org.example.service.CalculationService;
import org.example.util.LanguageManager;
import org.example.util.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Locale;
import java.io.File;
import javafx.stage.Stage;


@Component
public class FXMLController {

    @Autowired
    private CalculationService calculationService;

    @Autowired
    private LanguageManager languageManager;

    @Autowired
    private FileManager fileManager;

    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem menuItemLoad, menuItemSave, menuItemSwitchLanguage;
    @FXML
    private TextField hashRateField, powerConsumptionField, initialCostField, electricityCostField, cryptoPriceField;
    @FXML
    private Label hashRateLabel, powerConsumptionLabel, initialCostLabel, electricityCostLabel, cryptoPriceLabel, dailyMiningLabel, dailyProfitLabel,  energyCostLabel, breakEvenDaysLabel;
    @FXML
    private Button calculateButton;
    private Double lastDailyMiningValue = null;
    private Double lastDailyEnergyCost = null;
    private Double lastDailyProfit = null;
    private Double lastBreakEvenDays = null;
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        updateTitle();
        }
    @FXML
    public void initialize() {
        switchLanguage(Locale.getDefault());
        setupMenu();
        calculateButton.setOnAction(e -> calculateAction());
    }
    private void updateTitle() {
        if (primaryStage != null && languageManager != null) {
            primaryStage.setTitle(languageManager.getString("title"));
        }
    }
    private void setupMenu() {
        menuBar.getMenus().clear();
        Menu menu = new Menu(languageManager.getString("menu"));

        MenuItem menuItemLoad = new MenuItem(languageManager.getString("load"));
        menuItemLoad.setOnAction(e -> loadAction());

        MenuItem menuItemSave = new MenuItem(languageManager.getString("save"));
        menuItemSave.setOnAction(e -> saveAction());

        MenuItem menuItemSwitchLanguage = new MenuItem(languageManager.getString("switch_language"));
        menuItemSwitchLanguage.setOnAction(e -> switchLanguageAction());

        menu.getItems().addAll(menuItemLoad, menuItemSave, menuItemSwitchLanguage);
        menuBar.getMenus().add(menu);
    }
    private void calculateAction() {
        try {
            double hashRate = parseDouble(hashRateField.getText());
            double powerConsumption = parseDouble(powerConsumptionField.getText());
            double electricityCost = parseDouble(electricityCostField.getText());
            double cryptoPrice = parseDouble(cryptoPriceField.getText());
            double initialCost = parseDouble(initialCostField.getText());

            CryptoCalculator calculator = new CryptoCalculator(hashRate, powerConsumption, electricityCost, cryptoPrice, initialCost);
            lastDailyMiningValue = calculationService.calculateDailyMiningValue(calculator);
            lastDailyProfit = calculationService.calculateDailyProfit(calculator);
            lastDailyEnergyCost = calculationService.calculateDailyEnergyCost(calculator);
            lastBreakEvenDays = calculationService.calculateDaysToBreakEven(calculator);

            updateLabels();
        } catch (NumberFormatException e) {
            showAlert("Error", languageManager.getString("invalidInput"));
        }
    }
    private double parseDouble(String value) throws NumberFormatException {
        if (value == null) {
            throw new NumberFormatException("Value is null");
        }
        value = value.replace(',', '.').trim();
        return Double.parseDouble(value);
    }
    private void switchLanguageAction() {
        Locale currentLocale = languageManager.getLocale();
        Locale newLocale = "pl".equals(currentLocale.getLanguage()) ? new Locale("en") : new Locale("pl");
        switchLanguage(newLocale);
    }
    public void switchLanguage(Locale locale) {
        languageManager.setLocale(locale);
        updateTexts();
        updateTitle();
    }
    private void updateTexts() {
        menuBar.getMenus().get(0).setText(languageManager.getString("menu"));
        menuItemLoad.setText(languageManager.getString("load"));
        menuItemSave.setText(languageManager.getString("save"));
        menuItemSwitchLanguage.setText(languageManager.getString("switch_language"));

        calculateButton.setText(languageManager.getString("calculate"));
        hashRateLabel.setText(languageManager.getString("hashrate"));
        powerConsumptionLabel.setText(languageManager.getString("power_consumption"));
        initialCostLabel.setText(languageManager.getString("initial_cost"));
        electricityCostLabel.setText(languageManager.getString("energy_cost"));
        cryptoPriceLabel.setText(languageManager.getString("crypto_price"));
        updateLabels();
        setupMenu();
    }
    private void updateLabels() {
        if (lastDailyMiningValue != null) {
            dailyMiningLabel.setText(String.format(languageManager.getString("daily_mining") + ": %.2f PLN", lastDailyMiningValue));
            dailyMiningLabel.setVisible(true);
        } else {
            dailyMiningLabel.setVisible(false);
        }

        if (lastDailyProfit != null) {
            dailyProfitLabel.setText(String.format(languageManager.getString("daily_profit") + ": %.2f PLN", lastDailyProfit));
            dailyProfitLabel.setVisible(true);
        } else {
            dailyProfitLabel.setVisible(false);
        }

        if (lastDailyEnergyCost != null) {
            energyCostLabel.setText(String.format(languageManager.getString("energy_cost") + ": %.2f PLN", lastDailyEnergyCost));
            energyCostLabel.setVisible(true);
        } else {
            energyCostLabel.setVisible(false);
        }

        if (lastBreakEvenDays != null) {
            breakEvenDaysLabel.setText(String.format("%s: %.0f %s", languageManager.getString("break_even_days"), lastBreakEvenDays, languageManager.getString("days")));
            breakEvenDaysLabel.setVisible(true);
        } else {
            breakEvenDaysLabel.setVisible(false);
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void loadAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Data File");

        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);

        String lastPath = fileManager.getLastUsedFilePath();
        if (lastPath != null) {
            File defaultDirectory = new File(lastPath).getParentFile();
            if(defaultDirectory != null && defaultDirectory.exists()) {
                fileChooser.setInitialDirectory(defaultDirectory);
            }
        }

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            CryptoCalculator calculator = fileManager.loadData(file.getPath());
            if (calculator != null) {
                hashRateField.setText(String.valueOf(calculator.getHashRate()));
                powerConsumptionField.setText(String.valueOf(calculator.getPowerConsumption()));
                electricityCostField.setText(String.valueOf(calculator.getElectricityCost()));
                cryptoPriceField.setText(String.valueOf(calculator.getCryptoPrice()));
                initialCostField.setText(String.valueOf(calculator.getInitialCost()));
                lastDailyMiningValue = null;
                lastDailyEnergyCost = null;
                lastDailyProfit = null;
                lastBreakEvenDays = null;
                updateLabels();
            } else {
                showAlert("Error", languageManager.getString("failedToLoad"));
            }
        }
    }
    private void saveAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Data File");

        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);

        String lastPath = fileManager.getLastUsedFilePath();
        if (lastPath != null) {
            File defaultDirectory = new File(lastPath).getParentFile();
            if(defaultDirectory != null && defaultDirectory.exists()) {
                fileChooser.setInitialDirectory(defaultDirectory);
            }
        }

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                CryptoCalculator calculator = new CryptoCalculator(
                        Double.parseDouble(hashRateField.getText()),
                        Double.parseDouble(powerConsumptionField.getText()),
                        Double.parseDouble(electricityCostField.getText()),
                        Double.parseDouble(cryptoPriceField.getText()),
                        Double.parseDouble(initialCostField.getText()));

                fileManager.saveData(calculator, file.getPath());
            } catch (NumberFormatException e) {
                showAlert("Error", languageManager.getString("invalidInput"));
            }
        }
    }
}