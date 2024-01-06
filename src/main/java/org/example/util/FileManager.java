package org.example.util;

import org.example.model.CryptoCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import org.springframework.stereotype.Component;

@Component
public class FileManager {
    private final Logger logger = LoggerFactory.getLogger(FileManager.class);
    private final LanguageManager languageManager;
    private String lastUsedFilePath;

    public FileManager(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    private String ensureTxtExtension(String filePath) {
        if (!filePath.endsWith(".txt")) {
            return filePath + ".txt";
        }
        return filePath;
    }

    private void updateLastUsedFilePath(String filePath) {
        this.lastUsedFilePath = ensureTxtExtension(filePath);
    }

    public void saveData(CryptoCalculator calculator, String filePath) {
        filePath = ensureTxtExtension(filePath);
        updateLastUsedFilePath(filePath);
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            out.println(languageManager.getString("hash_rate") + ": = " + calculator.hashRate() + " TH/s");
            out.println(languageManager.getString("power_consumption") + ": = " + calculator.powerConsumption() + " Watts");
            out.println(languageManager.getString("initial_cost") + ": = " + calculator.initialCost() + " PLN");
            out.println(languageManager.getString("energy_cost") + ": = " + calculator.electricityCost() + " PLN/kWh");
            out.println(languageManager.getString("crypto_price") + ": = " + calculator.cryptoPrice() + " PLN");
        } catch (IOException e) {
            logger.error("Error saving data to file: {}", filePath, e);
        }
    }

    public CryptoCalculator loadData(String filePath) {
        filePath = ensureTxtExtension(filePath);
        updateLastUsedFilePath(filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            double hashRate = 0.0;
            double powerConsumption = 0.0;
            double initialCost = 0.0;
            double electricityCost = 0.0;
            double cryptoPrice = 0.0;

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(": = ");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    double value = Double.parseDouble(parts[1].split(" ")[0].trim().replace(',', '.'));

                    if (key.equals(languageManager.getString("hash_rate"))) {
                        hashRate = value;
                    } else if (key.equals(languageManager.getString("power_consumption"))) {
                        powerConsumption = value;
                    } else if (key.equals(languageManager.getString("initial_cost"))) {
                        initialCost = value;
                    } else if (key.equals(languageManager.getString("energy_cost"))) {
                        electricityCost = value;
                    } else if (key.equals(languageManager.getString("crypto_price"))) {
                        cryptoPrice = value;
                    }
                }
            }
            return new CryptoCalculator(hashRate, powerConsumption, electricityCost, cryptoPrice, initialCost);
        } catch (IOException e) {
            logger.error("Error loading data from file: {}", filePath, e);
            return null;
        }
    }

    public String getLastUsedFilePath() {
        return lastUsedFilePath;
    }
}
