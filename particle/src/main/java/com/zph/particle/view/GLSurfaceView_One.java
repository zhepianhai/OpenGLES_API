package com.zph.particle.view;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.zph.particle.render.MyRenderer_One;

/**
 * Created by zph on 2017/10/13.
 */

public class GLSurfaceView_One extends GLSurfaceView{
    private MyRenderer_One myRendererOne;
    private Context mContext;
    public GLSurfaceView_One(Context context) {
        super(context);
        this.mContext=context;
        initvar();
    }

    private void initvar() {
    }

    public void setRenderers(MyRenderer_One myRendererOne){
        this.myRendererOne=myRendererOne;
        setRenderer(this.myRendererOne);
    }


}
