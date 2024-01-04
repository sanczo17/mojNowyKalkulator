package org.example.model;

public class CryptoCalculator {
    private double hashRate; // Moc w TH/s
    private double powerConsumption; // Zużycie energii w Watach
    private double electricityCost; // Cena energii za kWh
    private double cryptoPrice; // Cena kryptowaluty
    private double initialCost; // Koszt inwestycji

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

    public void setHashRate(double hashRate) {
        this.hashRate = hashRate;
    }

    public double getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(double powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public double getElectricityCost() {
        return electricityCost;
    }

    public void setElectricityCost(double electricityCost) {
        this.electricityCost = electricityCost;
    }

    public double getCryptoPrice() {
        return cryptoPrice;
    }

    public void setCryptoPrice(double cryptoPrice) {
        this.cryptoPrice = cryptoPrice;
    }

    public double getInitialCost() {
        return initialCost;
    }

    public void setInitialCost(double initialCost) {
        this.initialCost = initialCost;
    }
}
