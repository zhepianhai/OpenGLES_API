package com.zph.opengltest.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.zph.opengltest.render.RainISORenderer2;

/**
 * Created by zph on 2017/10/11.
 */

public class OpenGLView2  extends GLSurfaceView{
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//角度缩放比例
    private RainISORenderer2 mRenderer;//场景渲染器

    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标

    //摄像机的位置角度  xyz
    public static float cx=0;
    public static float cy=2;
    public static float cz=24;
    public static float cr=24;//摄像机半径
    public static float cAngle=0;

    private Context mContext;

    public OpenGLView2(Context context) {
        super(context);
        //设置为2.0的
//        this.setEGLContextClientVersion(2);
        this.mContext=context;
        this.mRenderer =new RainISORenderer2(mContext);
//        setEGLConfigChooser(8, 8, 8, 8, 16, 0);//在setRenderer之前调用的
        setRenderer(mRenderer);

    }


    //触摸事件回调，改变摄像机位置
    @Override
    public boolean onTouchEvent(MotionEvent e){
        float y=e.getY();
        float x=e.getX();
        switch (e.getAction()){
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;//计算触控笔Y位移
                float dx = x - mPreviousX;//计算触控笔X位移
                cAngle+=dx * TOUCH_SCALE_FACTOR;
                cx=(float) (Math.sin(Math.toRadians(cAngle))*cr);
                cz=(float) (Math.cos(Math.toRadians(cAngle))*cr);
                cy+=dy/10.0f;
        }
        mPreviousY = y;//记录触控笔位置
        mPreviousX = x;//记录触控笔位置
        return  true;
    }

    public void showData(float[][] mDataGrid, float mDataMax, int mDataCol, int mDataRow) {
        this.mRenderer.showData(mDataGrid, mDataMax,mDataCol,mDataRow);
        this.requestRender();
    }


}
