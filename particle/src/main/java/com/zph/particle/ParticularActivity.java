package com.zph.particle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zph.particle.view.GLSurfaceView_One;

public class ParticularActivity extends AppCompatActivity {
    private GLSurfaceView_One mGLSurfaceview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceview=new GLSurfaceView_One(this);


        setContentView(mGLSurfaceview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null!=mGLSurfaceview){
            mGLSurfaceview.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(null!=mGLSurfaceview){
            mGLSurfaceview.onResume();
        }
    }
}
