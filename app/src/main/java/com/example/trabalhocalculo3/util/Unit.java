package com.example.trabalhocalculo3.util;

public enum Unit {
    MILLIMETER(0.001, "mm"),
    CENTIMETER(0.01, "cm"),
    METER(1.0, "m");

    private final double metersPerUnit;
    private final String symbol;

    Unit(double metersPerUnit, String symbol) {
        this.metersPerUnit = metersPerUnit;
        this.symbol = symbol;
    }

    public double getMetersPerUnit() {
        return metersPerUnit;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getVolumeSymbol() {
        return symbol + "³";
    }
}