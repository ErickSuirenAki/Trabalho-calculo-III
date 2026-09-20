package com.example.trabalhocalculo3.mesh;

import android.graphics.Mesh;
import android.os.Build;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.StringTokenizer;

// melhorar o tratamento de excessões
public class MeshImporter {
    public static Mesh3D importStl(InputStream inputStream){
        Mesh3D mesh = new Mesh3D(null, null);

        if(inputStream == null){
            return mesh;
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                byte[] data = inputStream.readAllBytes();

                if(data.length == 0){// arquivo vazio
                    return mesh;
                }

                StlFormat format = detectFormat(data);

                if (format == StlFormat.BINARY) {
                    return parseBinary(data);
                }

                if (format == StlFormat.ASCII) {
                    return parseAscii(data);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return mesh;
    }
    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];

        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
        return output.toByteArray();
    }

    private static StlFormat detectFormat(byte[] data) {
        if (data.length >= 84) {
            ByteBuffer header = ByteBuffer.wrap(data, 80, 4).order(ByteOrder.LITTLE_ENDIAN);

            long triangleCount = Integer.toUnsignedLong(header.getInt());
            long expectedSize = 84L + triangleCount * 50L;

            if (expectedSize == data.length) {
                return StlFormat.BINARY;
            }

            if (expectedSize >= 84 && expectedSize < data.length && !looksLikeAscii(data)) {
                return StlFormat.BINARY;
            }
        }

        if (looksLikeAscii(data)) {
            return StlFormat.ASCII;
        }

        return StlFormat.INVALID;
    }

    private static boolean looksLikeAscii(byte[] data) {
        int checkLength = Math.min(data.length, 2048);

        String beginning = new String(
                data,
                0,
                checkLength,
                StandardCharsets.US_ASCII
        ).trim().toLowerCase();

        return beginning.startsWith("solid") && (beginning.contains("facet") || beginning.contains("vertex"));
    }

    private static Mesh3D parseBinary(byte[] data) {
        Mesh3D mesh = new Mesh3D(null, null);

        if (data.length < 84) {// stl menor que 84 bytes
            return mesh;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        buffer.position(80);

        long triangleCountLong = Integer.toUnsignedLong(buffer.getInt());

        if (triangleCountLong == 0) {// STL binário possui zero triângulos
            return mesh;
        }

        long expectedSize = 84L + triangleCountLong * 50L;
        if (expectedSize > data.length) {// Arquivo STL binário incompleto
            return mesh;
        }

        if (triangleCountLong > Integer.MAX_VALUE / 9L) {//STL grande demais
            return mesh;
        }

        int triangleCount = (int) triangleCountLong;

        float[] positions = new float[triangleCount * 9];
        float[] normals = new float[triangleCount * 9];

        int positionIndex = 0;
        for (int triangle = 0; triangle < triangleCount; triangle++) {
            buffer.getFloat();
            buffer.getFloat();
            buffer.getFloat();

            float x1 = buffer.getFloat();
            float y1 = buffer.getFloat();
            float z1 = buffer.getFloat();

            float x2 = buffer.getFloat();
            float y2 = buffer.getFloat();
            float z2 = buffer.getFloat();

            float x3 = buffer.getFloat();
            float y3 = buffer.getFloat();
            float z3 = buffer.getFloat();

            if(!validateFinite(x1, y1, z1)
                    || !validateFinite(x2, y2, z2)
                    || !validateFinite(x3, y3, z3))
                return mesh;

            positions[positionIndex] = x1;
            positions[positionIndex + 1] = y1;
            positions[positionIndex + 2] = z1;

            positions[positionIndex + 3] = x2;
            positions[positionIndex + 4] = y2;
            positions[positionIndex + 5] = z2;

            positions[positionIndex + 6] = x3;
            positions[positionIndex + 7] = y3;
            positions[positionIndex + 8] = z3;

            writeNormal(
                    normals,
                    positionIndex,
                    x1, y1, z1,
                    x2, y2, z2,
                    x3, y3, z3
            );

            positionIndex += 9;
            buffer.getShort();
        }
        return new Mesh3D(positions, normals);
    }

    private static Mesh3D parseAscii(byte[] data) {
        Mesh3D mesh = new Mesh3D(null, null);
        FloatArrayBuilder positions = new FloatArrayBuilder(1024);

        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new ByteArrayInputStream(data),
                        StandardCharsets.US_ASCII
                    )
                )
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("vertex")) {
                    continue;
                }

                StringTokenizer tokenizer = new StringTokenizer(line);
                tokenizer.nextToken();

                if (tokenizer.countTokens() < 3) {
                    return mesh;
                }

                float x = Float.parseFloat(tokenizer.nextToken());
                float y = Float.parseFloat(tokenizer.nextToken());
                float z = Float.parseFloat(tokenizer.nextToken());

                if(!validateFinite(x, y, z)) return mesh;

                positions.add(x);
                positions.add(y);
                positions.add(z);
            }

        } catch (NumberFormatException | IOException e) {
            return mesh;
        }

        float[] vertexData = positions.toArray();

        if (vertexData.length == 0) {
            // Nenhum vértice foi encontrado
            return mesh;
        }


        if (vertexData.length % 9 != 0) {
            // Quantidade de vértices inválida no STL ASCII
            return mesh;
        }

        float[] normals = new float[vertexData.length];

        for (int i = 0;
             i < vertexData.length;
             i += 9) {

            float x1 = vertexData[i];
            float y1 = vertexData[i + 1];
            float z1 = vertexData[i + 2];

            float x2 = vertexData[i + 3];
            float y2 = vertexData[i + 4];
            float z2 = vertexData[i + 5];

            float x3 = vertexData[i + 6];
            float y3 = vertexData[i + 7];
            float z3 = vertexData[i + 8];

            writeNormal(
                    normals,
                    i,
                    x1, y1, z1,
                    x2, y2, z2,
                    x3, y3, z3
            );
        }

        return new Mesh3D(vertexData, normals);
    }

    private static void writeNormal(
            float[] normals,
            int index,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3) {

        float ux = x2 - x1;
        float uy = y2 - y1;
        float uz = z2 - z1;

        float vx = x3 - x1;
        float vy = y3 - y1;
        float vz = z3 - z1;

        float nx = uy * vz - uz * vy;
        float ny = uz * vx - ux * vz;
        float nz = ux * vy - uy * vx;

        float length = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);

        if (length != 0.0f) {
            nx /= length;
            ny /= length;
            nz /= length;
        }


        for (int i = 0; i < 3; i++) {
            int n = index + i * 3;

            normals[n] = nx;
            normals[n + 1] = ny;
            normals[n + 2] = nz;
        }
    }

    private static boolean validateFinite(
            float x,
            float y,
            float z) {
        return Float.isFinite(x)
                && Float.isFinite(y)
                && Float.isFinite(z);
    }

    private static class FloatArrayBuilder {
        private float[] data;
        private int size = 0;

        FloatArrayBuilder(int initialCapacity) {
            data = new float[initialCapacity];
        }
        void add(float value) {

            if (size == data.length) {
                data = Arrays.copyOf(
                        data,
                        data.length * 2
                );
            }

            data[size++] = value;
        }

        float[] toArray() {
            return Arrays.copyOf(data, size);
        }
    }
}

