package com.example.trabalhocalculo3.mesh;

public class Mesh3D {
    private final float[] positions;
    private final float[] normals;

    public Mesh3D(float[] positions, float[] normals) {
        this.positions = positions;
        this.normals = normals;
    }

    public float[] getPositions() {
        return positions;
    }

    public float[] getNormals() {
        return normals;
    }

    public int getVertexCount() {
        return positions.length / 3;
    }

    public int getTriangleCount() {
        return positions.length / 9;
    }

    public float getBoundingRadius() {
        if (positions == null || positions.length == 0) return 1.0f;

        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;

        for (int i = 0; i < positions.length; i += 3) {
            float x = positions[i];
            float y = positions[i + 1];
            float z = positions[i + 2];

            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
            if (z < minZ) minZ = z;
            if (z > maxZ) maxZ = z;
        }

        // Calcula as dimensões do objeto
        float sizeX = maxX - minX;
        float sizeY = maxY - minY;
        float sizeZ = maxZ - minZ;

        // Retorna a maior dimensão do objeto
        return Math.max(sizeX, Math.max(sizeY, sizeZ));
    }

    public float[] getCenter() {
        if (positions == null || positions.length == 0) return new float[]{0, 0, 0};

        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;

        for (int i = 0; i < positions.length; i += 3) {
            float x = positions[i];
            float y = positions[i + 1];
            float z = positions[i + 2];

            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
            if (z < minZ) minZ = z;
            if (z > maxZ) maxZ = z;
        }

        return new float[]{
                (minX + maxX) / 2.0f,
                (minY + maxY) / 2.0f,
                (minZ + maxZ) / 2.0f
        };
    }
}
