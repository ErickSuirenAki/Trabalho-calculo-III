package com.example.trabalhocalculo3.graphics;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.trabalhocalculo3.mesh.Mesh3D;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GraphRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "GraphRenderer";

    private int shaderProgram;
    private int vertexBufferObject;

    private int vertexCount;

    // Ângulo de rotação do objeto
    private float rotationAngle = 0.0f;

    private Mesh3D mesh;

    private boolean drawLog = false;

    // Matriz final enviada para o shader
    private final float[] mvpMatrix = new float[16];

    // Matriz de projeção
    private final float[] projectionMatrix = new float[16];

    // Matriz da câmera
    private final float[] viewMatrix = new float[16];

    // Matriz do objeto
    private final float[] modelMatrix = new float[16];

    private final String vertexShaderCode =
            "#version 300 es\n" +
            "layout(location = 0) in vec3 aPosition;\n" +
            "\n" +
            "uniform mat4 uMVPMatrix;\n" +
            "\n" +
            "void main() {\n" +
            "    gl_Position = uMVPMatrix * vec4(aPosition, 1.0);\n" +
            "}";

    private final String fragmentShaderCode =
            "#version 300 es\n" +
            "precision mediump float;\n" +
            "\n" +
            "out vec4 fragColor;\n" +
            "\n" +
            "void main() {\n" +
            "    fragColor = vec4(0.2, 0.7, 1.0, 1.0);\n" +
            "}";

    public void setMesh(Mesh3D mesh) {

        this.mesh = mesh;

        if (mesh == null) {
            vertexCount = 0;
            return;
        }

        vertexCount = mesh.getVertexCount();

        Log.d(
                TAG,
                "Mesh recebido: " +
                        vertexCount +
                        " vertices"
        );
    }

    @Override
    public void onSurfaceCreated(
            GL10 gl,
            EGLConfig config
    ) {

        GLES30.glClearColor(
                0.05f,
                0.05f,
                0.05f,
                1.0f
        );

        // Ativa o teste de profundidade.
        // Isso é importante para objetos 3D.
        GLES30.glEnable(
                GLES30.GL_DEPTH_TEST
        );

        int vertexShader = compileShader(
                GLES30.GL_VERTEX_SHADER,
                vertexShaderCode
        );

        int fragmentShader = compileShader(
                GLES30.GL_FRAGMENT_SHADER,
                fragmentShaderCode
        );

        shaderProgram = GLES30.glCreateProgram();

        GLES30.glAttachShader(
                shaderProgram,
                vertexShader
        );

        GLES30.glAttachShader(
                shaderProgram,
                fragmentShader
        );

        GLES30.glLinkProgram(
                shaderProgram
        );

        int[] linkStatus = new int[1];

        GLES30.glGetProgramiv(
                shaderProgram,
                GLES30.GL_LINK_STATUS,
                linkStatus,
                0
        );

        if (linkStatus[0] == 0) {

            Log.e(
                    TAG,
                    "Erro ao linkar programa: " +
                            GLES30.glGetProgramInfoLog(
                                    shaderProgram
                            )
            );

            GLES30.glDeleteProgram(
                    shaderProgram
            );

            shaderProgram = 0;

            return;
        }

        // Cria o VBO
        int[] buffers = new int[1];

        GLES30.glGenBuffers(
                1,
                buffers,
                0
        );

        vertexBufferObject = buffers[0];

        // Inicializa a matriz do objeto
        Matrix.setIdentityM(
                modelMatrix,
                0
        );
    }

    @Override
    public void onSurfaceChanged(
            GL10 gl,
            int width,
            int height
    ) {

        GLES30.glViewport(
                0,
                0,
                width,
                height
        );

        float aspectRatio =
                (float) width / (float) height;

        // Configuração da perspectiva
        Matrix.perspectiveM(
                projectionMatrix,
                0,
                45.0f,
                aspectRatio,
                0.1f,
                100.0f
        );

        // Configuração da câmera
        Matrix.setLookAtM(
                viewMatrix,
                0,

                // posição da câmera
                0.0f,
                0.0f,
                3.0f,

                // ponto para onde olha
                0.0f,
                0.0f,
                0.0f,

                // direção "para cima"
                0.0f,
                1.0f,
                0.0f
        );
    }

    @Override
    public void onDrawFrame(
            GL10 gl
    ) {

        // Limpa tela e profundidade
        GLES30.glClear(
                GLES30.GL_COLOR_BUFFER_BIT |
                        GLES30.GL_DEPTH_BUFFER_BIT
        );

        if (shaderProgram == 0) {
            return;
        }

        if (mesh == null) {
            return;
        }

        if (!drawLog) {

            Log.d(
                    TAG,
                    "Iniciando desenho. VertexCount = " +
                            vertexCount
            );

            drawLog = true;
        }

        float[] positions =
                mesh.getPositions();

        if (positions == null ||
                positions.length == 0) {

            return;
        }

        /*
         * =========================================================
         * ROTAÇÃO DO OBJETO
         * =========================================================
         *
         * Como o GraphSurfaceView está usando
         * RENDERMODE_CONTINUOUSLY, este método é chamado
         * continuamente.
         *
         * Portanto, aumentando o ângulo a cada frame,
         * conseguimos criar uma animação de rotação.
         */

        rotationAngle += 0.5f;
        Matrix.setIdentityM(modelMatrix, 0);

        // Rotação do objeto
        Matrix.rotateM(modelMatrix, 0, rotationAngle, 0.0f, 1.0f, 0.0f);

        // Correção de orientação para OpenGL
        Matrix.rotateM(modelMatrix, 0, -90.0f, 1.0f, 0.0f, 0.0f);

        // Cálculo do fator de escala
        float boundingRadius = mesh.getBoundingRadius();
        float targetSize = 1.5f; // Tamanho para caber na visão da câmera
        float scale = (boundingRadius > 0) ? (targetSize / boundingRadius) : 1.0f;

        // Aplica a escala proporcional no modelo
        Matrix.scaleM(modelMatrix, 0, scale, scale, scale);

        // Centralização do modelo na origem (0,0,0)
        float[] center = mesh.getCenter();
        Matrix.translateM(modelMatrix, 0, -center[0], -center[1], -center[2]);

        /*
         * =========================================================
         * CRIA BUFFER DOS VÉRTICES
         * =========================================================
         */

        FloatBuffer vertexBuffer =
                ByteBuffer
                        .allocateDirect(
                                positions.length * 4
                        )
                        .order(
                                ByteOrder.nativeOrder()
                        )
                        .asFloatBuffer();

        vertexBuffer.put(
                positions
        );

        vertexBuffer.position(0);

        /*
         * =========================================================
         * ENVIA VÉRTICES PARA A GPU
         * =========================================================
         */

        GLES30.glBindBuffer(
                GLES30.GL_ARRAY_BUFFER,
                vertexBufferObject
        );

        GLES30.glBufferData(
                GLES30.GL_ARRAY_BUFFER,
                positions.length * 4,
                vertexBuffer,
                GLES30.GL_DYNAMIC_DRAW
        );

        /*
         * =========================================================
         * CALCULA MATRIZ MVP
         * =========================================================
         */

        float[] tempMatrix =
                new float[16];

        // View × Model
        Matrix.multiplyMM(
                tempMatrix,
                0,
                viewMatrix,
                0,
                modelMatrix,
                0
        );

        // Projection × View × Model
        Matrix.multiplyMM(
                mvpMatrix,
                0,
                projectionMatrix,
                0,
                tempMatrix,
                0
        );

        /*
         * =========================================================
         * ATIVA SHADER
         * =========================================================
         */

        GLES30.glUseProgram(
                shaderProgram
        );

        /*
         * =========================================================
         * ENVIA MATRIZ PARA O SHADER
         * =========================================================
         */

        int matrixHandle =
                GLES30.glGetUniformLocation(
                        shaderProgram,
                        "uMVPMatrix"
                );

        GLES30.glUniformMatrix4fv(
                matrixHandle,
                1,
                false,
                mvpMatrix,
                0
        );

        /*
         * =========================================================
         * CONFIGURA POSIÇÃO DOS VÉRTICES
         * =========================================================
         */

        GLES30.glEnableVertexAttribArray(
                0
        );

        GLES30.glVertexAttribPointer(
                0,
                3,
                GLES30.GL_FLOAT,
                false,
                3 * 4,
                0
        );

        /*
         * =========================================================
         * DESENHA O OBJETO
         * =========================================================
         */

        GLES30.glDrawArrays(
                GLES30.GL_TRIANGLES,
                0,
                vertexCount
        );

        /*
         * =========================================================
         * VERIFICA ERROS OPENGL
         * =========================================================
         */

        int error =
                GLES30.glGetError();

        if (error != GLES30.GL_NO_ERROR) {

            Log.e(
                    TAG,
                    "Erro OpenGL ao desenhar: " +
                            error
            );
        }

        /*
         * =========================================================
         * LIMPEZA
         * =========================================================
         */

        GLES30.glDisableVertexAttribArray(
                0
        );

        GLES30.glBindBuffer(
                GLES30.GL_ARRAY_BUFFER,
                0
        );
    }

    private int compileShader(
            int type,
            String shaderCode
    ) {

        int shader =
                GLES30.glCreateShader(
                        type
                );

        GLES30.glShaderSource(
                shader,
                shaderCode
        );

        GLES30.glCompileShader(
                shader
        );

        int[] compileStatus =
                new int[1];

        GLES30.glGetShaderiv(
                shader,
                GLES30.GL_COMPILE_STATUS,
                compileStatus,
                0
        );

        if (compileStatus[0] == 0) {

            Log.e(
                    TAG,
                    "Erro ao compilar shader: " +
                            GLES30.glGetShaderInfoLog(
                                    shader
                            )
            );

            GLES30.glDeleteShader(
                    shader
            );

            return 0;
        }

        return shader;
    }
}