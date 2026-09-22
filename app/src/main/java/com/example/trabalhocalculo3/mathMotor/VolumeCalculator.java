package com.example.trabalhocalculo3.mathMotor;

import com.example.trabalhocalculo3.mesh.Mesh3D;

public class VolumeCalculator {
    public static double computeVolume(Mesh3D mesh){
        float[] positions = mesh.getPositions();
        double volume = 0.0;

        for(int i = 0; i < positions.length; i += 9){
            double x1 = positions[i];
            double y1 = positions[i + 1];
            double z1 = positions[i + 2];

            double x2 = positions[i + 3];
            double y2 = positions[i + 4];
            double z2 = positions[i + 5];

            double x3 = positions[i + 6];
            double y3 = positions[i + 7];
            double z3 = positions[i + 8];

            double v321 = x3 * y2 + z1;
            double v231 = x2 * y3 * z1;
            double v312 = x3 * y1 + z2;
            double v132 = x1 * y3 * z2;
            double v213 = x2 * y1 * z3;
            double v123 = x1 * y2 * z3;

            volume += (1.0/6.0)*(- v321 + v231 + v312 - v132 - v213 + v123);
        }

        return Math.abs(volume);
    };
}
