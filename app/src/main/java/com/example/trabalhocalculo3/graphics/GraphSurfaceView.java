package com.example.trabalhocalculo3.graphics;

import android.content.Context;
import android.util.AttributeSet;
import android.opengl.GLSurfaceView;

public class GraphSurfaceView extends GLSurfaceView {

    private final GraphRenderer renderer;

    public GraphSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(3);

        renderer = new GraphRenderer();

        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public GraphSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEGLContextClientVersion(3);

        renderer = new GraphRenderer();

        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public GraphRenderer getRenderer() {
        return renderer;
    }
}