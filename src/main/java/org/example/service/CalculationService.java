package org.example.service;

import org.example.model.CryptoCalculator;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class CalculationService {
    private final Random random = new Random();


    public double calculateDailyMining(CryptoCalculator calculator) {
        double baseHashRate = 8.0;
        double baseCoinsPerDay = 700.0;

        double miningEfficiency = calculator.getHashRate() / baseHashRate;
        double estimatedMining = miningEfficiency * baseCoinsPerDay;

        //double fluctuation = (random.nextDouble() - 0.5) * 2 * 0.1;
        return estimatedMining;// * (1 + fluctuation);
    }
    public double calculateDailyEnergyCost(CryptoCalculator calculator) {
        double dailyEnergyConsumption = calculateDailyEnergyConsumption(calculator);
        return dailyEnergyConsumption * calculator.getElectricityCost();
    }
    public double calculateDailyEnergyConsumption(CryptoCalculator calculator) {
        return (calculator.getPowerConsumption() / 1000.0) * 24; // Przelicznik na kWh i 24h pracy
    }
    public double calculateDaysToBreakEven(CryptoCalculator calculator) {
        double dailyProfit = calculateDailyMiningValue(calculator) - calculateDailyEnergyCost(calculator);
        if (dailyProfit <= 0) return Double.POSITIVE_INFINITY;
        return calculator.getInitialCost() / dailyProfit;
    }
    public double calculateDailyMiningValue(CryptoCalculator calculator) {
        double dailyMiningCoins = calculateDailyMining(calculator);
        return dailyMiningCoins * calculator.getCryptoPrice();
    }
    public double calculateDailyProfit(CryptoCalculator calculator) {
        double dailyMiningValue = calculateDailyMiningValue(calculator);
        double dailyEnergyCost = calculateDailyEnergyCost(calculator);

        double dailyProfit = dailyMiningValue - dailyEnergyCost;

        return dailyProfit;
    }

}
