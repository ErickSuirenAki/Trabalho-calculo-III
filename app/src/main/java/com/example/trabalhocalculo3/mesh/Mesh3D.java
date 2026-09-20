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
}
