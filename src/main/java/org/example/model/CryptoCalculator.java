package org.example.model;

public class CryptoCalculator {
    private final double hashRate; // Moc w TH/s
    private final double powerConsumption; // Zużycie energii w Watach
    private final double electricityCost; // Cena energii za kWh
    private final double cryptoPrice; // Cena kryptowaluty
    private final double initialCost; // Koszt inwestycji

    public CryptoCalculator(double hashRate, double powerConsumption, double electricityCost, double cryptoPrice, double initialCost) {
        this.hashRate = hashRate;
        this.powerConsumption = powerConsumption;
        this.electricityCost = electricityCost;
        this.cryptoPrice = cryptoPrice;
        this.initialCost = initialCost;
    }

    public double getHashRate() {
        return hashRate;
    }


    public double getPowerConsumption() {
        return powerConsumption;
    }


    public double getElectricityCost() {
        return electricityCost;
    }


    public double getCryptoPrice() {
        return cryptoPrice;
    }


    public double getInitialCost() {
        return initialCost;
    }

}
