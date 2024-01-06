package org.example.service;

import org.example.model.CryptoCalculator;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {
    public double calculateDailyMining(CryptoCalculator calculator) {
        double baseHashRate = 8.0;
        double baseCoinsPerDay = 700.0;
        double miningEfficiency = calculator.hashRate() / baseHashRate;
        return miningEfficiency * baseCoinsPerDay;
    }

public double calculateDailyEnergyCost(CryptoCalculator calculator) {
    return calculateDailyEnergyConsumption(calculator) * calculator.electricityCost();
}

    public double calculateDailyEnergyConsumption(CryptoCalculator calculator) {
        return (calculator.powerConsumption() / 1000.0) * 24;
    }

    public double calculateDaysToBreakEven(CryptoCalculator calculator) {
        double dailyProfit = calculateDailyMiningValue(calculator) - calculateDailyEnergyCost(calculator);
        return dailyProfit <= 0 ? Double.POSITIVE_INFINITY : calculator.initialCost() / dailyProfit;
    }

    public double calculateDailyMiningValue(CryptoCalculator calculator) {
        return calculateDailyMining(calculator) * calculator.cryptoPrice();
    }

    public double calculateDailyProfit(CryptoCalculator calculator) {
        return calculateDailyMiningValue(calculator) - calculateDailyEnergyCost(calculator);
    }
}
