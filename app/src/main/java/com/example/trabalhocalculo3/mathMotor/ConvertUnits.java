package com.example.trabalhocalculo3.mathMotor;

import com.example.trabalhocalculo3.util.Unit;

public class ConvertUnits {
    public static double convertVolume(double value, Unit from, Unit to) {
        double factor = from.getMetersPerUnit() / to.getMetersPerUnit();
        return value * factor * factor * factor;
    }

   public static double convertDensity(double value, Unit from, Unit to) {
        double factor = to.getMetersPerUnit() / from.getMetersPerUnit();
        return value * factor * factor * factor;
    }
}
