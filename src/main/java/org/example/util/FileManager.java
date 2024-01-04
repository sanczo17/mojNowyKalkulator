package org.example.util;

import org.example.model.CryptoCalculator;
import org.example.service.CalculationService;
import java.io.*;
import org.springframework.stereotype.Component;

@Component
public class FileManager {
    private final LanguageManager languageManager;
    public FileManager(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }
    public void saveData(CryptoCalculator calculator, String filePath, CalculationService calculationService) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            out.println(languageManager.getString("hashrate") + ": = " + calculator.getHashRate() + " TH/s");
            out.println(languageManager.getString("power_consumption") + ": = " + calculator.getPowerConsumption() + " Watts");
            out.println(languageManager.getString("initial_cost") + ": = " + calculator.getInitialCost() + " PLN");
            out.println(languageManager.getString("energy_cost") + ": = " + calculator.getElectricityCost() + " PLN/kWh");
            out.println(languageManager.getString("crypto_price") + ": = " + calculator.getCryptoPrice() + " PLN");

//            if (calculationService.calculateDailyMining(calculator) != 0){
//            out.println(languageManager.getString("daily_mining") + ": = " + calculationService.calculateDailyMining(calculator) + " " + languageManager.getString("coins"));
//            }
//            if (calculationService.calculateDailyEnergyCost(calculator)!= 0){
//            out.println(languageManager.getString("energy_cost") + ": = " + calculationService.calculateDailyEnergyCost(calculator) + " PLN");
//            }
//            if (calculationService.calculateDailyProfit(calculator)!= 0) {
//                out.println(languageManager.getString("daily_profit") + ": = " + calculationService.calculateDailyProfit(calculator) + " PLN");
//            }
//            if (calculationService.calculateDaysToBreakEven(calculator)!= 0) {
//                out.println(languageManager.getString("break_even_days") + ": = " + calculationService.calculateDaysToBreakEven(calculator) + " " + languageManager.getString("days"));
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CryptoCalculator loadData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            double hashRate = 0.0;
            double powerconsumption = 0.0;
            double initialCost = 0.0;
            double electricityCost = 0.0;
            double cryptoPrice = 0.0;

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(": = ");
                String key = parts[0].trim();
                double value = Double.parseDouble(parts[1].split(" ")[0]);

                if (key.equals(languageManager.getString("power"))) {
                    hashRate = value;
                } else if (key.equals(languageManager.getString("energy_consumption"))) {
                    powerconsumption = value;
                } else if (key.equals(languageManager.getString("initial_cost"))) {
                    initialCost = value;
                } else if (key.equals(languageManager.getString("energy_cost"))) {
                    electricityCost = value;
                } else if (key.equals(languageManager.getString("crypto_price"))) {
                    cryptoPrice = value;
                }
            }

            return new CryptoCalculator(hashRate, powerconsumption, electricityCost, cryptoPrice, initialCost);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
