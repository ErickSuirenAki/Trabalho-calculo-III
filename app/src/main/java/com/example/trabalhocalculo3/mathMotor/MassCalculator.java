package com.example.trabalhocalculo3.mathMotor;

import com.example.trabalhocalculo3.mesh.Mesh3D;

public class MassCalculator {
     // Calcula o Centro de Massa (x, y, z) de um Mesh3D assumindo densidade constante.
     // Recebe uma malaha e retorna um array representando as coordenadas do centro de massa.
    public static double[] computeCenterOfMass(Mesh3D mesh) {
        float[] positions = mesh.getPositions();
        if (positions == null || positions.length == 0) {
            return new double[]{0.0, 0.0, 0.0};
        }

        double totalSignedVolume = 0.0;
        double sumX = 0.0;
        double sumY = 0.0;
        double sumZ = 0.0;

        for (int i = 0; i < positions.length; i += 9) {
            double x1 = positions[i];
            double y1 = positions[i + 1];
            double z1 = positions[i + 2];

            double x2 = positions[i + 3];
            double y2 = positions[i + 4];
            double z2 = positions[i + 5];

            double x3 = positions[i + 6];
            double y3 = positions[i + 7];
            double z3 = positions[i + 8];

            // Volume orientado (com sinal) do tetraedro via produto misto / 6
            double signedVolume = (1.0 / 6.0) * (
                    x1 * (y2 * z3 - y3 * z2) -
                            y1 * (x2 * z3 - x3 * z2) +
                            z1 * (x2 * y3 - x3 * y2)
            );

            // Centróide do tetraedro (média dos 4 vértices, incluindo a origem)
            double cx = (x1 + x2 + x3) / 4.0;
            double cy = (y1 + y2 + y3) / 4.0;
            double cz = (z1 + z2 + z3) / 4.0;

            // Ponderação do centróide pelo volume assinado
            sumX += cx * signedVolume;
            sumY += cy * signedVolume;
            sumZ += cz * signedVolume;

            totalSignedVolume += signedVolume;
        }

        if (Math.abs(totalSignedVolume) < 1e-12) {
            return new double[]{0.0, 0.0, 0.0};
        }

        return new double[]{sumX / totalSignedVolume, sumY / totalSignedVolume, sumZ / totalSignedVolume};
    }
}
