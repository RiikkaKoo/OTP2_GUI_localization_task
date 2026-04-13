package model;

public class CalculationRecord {

    private double distance;
    private double consumption;
    private double price;
    private double totalFuel;
    private double totalCost;
    private String language;

    public CalculationRecord(double distance, double consumption, double price, double totalFuel, double totalCost, String lang) {
        this.distance = distance;
        this.consumption = consumption;
        this.price = price;
        this.totalFuel = totalFuel;
        this.totalCost = totalCost;
        this.language = lang;
    }

    public double getDistance() {
        return distance;
    }

    public double getConsumption() {
        return consumption;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalFuel() {
        return totalFuel;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getLanguage() {
        return language;
    }
}
