package com.zph.particle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zph.particle.render.MyRenderer_One;
import com.zph.particle.view.GLSurfaceView_One;

public class ParticularActivity extends AppCompatActivity {
    private GLSurfaceView_One mGLSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurface =new GLSurfaceView_One(this);
        MyRenderer_One myRendererOne = new MyRenderer_One(this);
        mGLSurface.setRenderers(myRendererOne);
        setContentView(mGLSurface);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null!= mGLSurface){
            mGLSurface.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(null!= mGLSurface){
            mGLSurface.onResume();
        }
    }
}
