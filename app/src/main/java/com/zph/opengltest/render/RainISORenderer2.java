package com.zph.opengltest.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;


import com.zph.opengltest.obj.TriPrism;
import com.zph.opengltest.util.MatrixState;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.zph.opengltest.view.OpenGLView2.cx;
import static com.zph.opengltest.view.OpenGLView2.cy;
import static com.zph.opengltest.view.OpenGLView2.cz;


/**
 * Created by zph on 2017/10/11.
 */

public class RainISORenderer2  implements GLSurfaceView.Renderer{
    private Context mContext;
    private static final float[] RAIN_LEVEL = { 0, 10, 25, 50, 100, 250, Float.MAX_VALUE };
    private static final float[][] RAIN_COLOR = { { 1.0f, 1.0f, 1.0f, 1.0f }, { 0.608f, 1.000f, 0.529f, 1.0f },
            { 0.196f, 0.667f, 0.000f, 1.0f }, { 0.000f, 0.000f, 1.000f, 1.0f }, { 1.000f, 0.000f, 1.000f, 1.0f },
            { 1.000f, 0.000f, 0.000f, 1.0f }, { 1.000f, 0.000f, 0.000f, 1.0f } };

    private static final float SHOW_MAX_H = 1f;
    private static final float SHOW_MAX_V = 0.5f;

    private ArrayList<TriPrism> mArrPrism;

    public RainISORenderer2(Context mContext){
        this.mContext=mContext;
        TriPrism.ISOSHOW_LEVEL = new float[] { 10, 25, 50, 100, 250 };
        TriPrism.ISOSHOW_COLOR = new float[][] { { 1.000f, 1.000f, 1.000f, 1.0f }, { 1.000f, 1.000f, 1.000f, 1.0f },
                { 1.000f, 1.000f, 1.000f, 1.0f }, { 1.000f, 1.000f, 1.000f, 1.0f }, { 1.000f, 1.000f, 1.000f, 1.0f } };

        mArrPrism = new ArrayList<>();
    }



    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        //打开深度检测
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //打开背面剪裁 打开会出现断续
//        GLES20.glEnable(GLES20.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 1f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        //初始化变换矩阵
        MatrixState.setInitStack();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        //设置视窗大小及位置
        GLES20.glViewport(0,0,width,height);
        //计算GLSurfaceView的宽高比
        float ratio=(float)width/height;
        //调用此方法计算产生透视投影矩阵
        MatrixState.setProjectFrustum(-ratio,ratio,-1,1,2,1000);
    }
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        /**
         * 改变眼睛所处的位置，眼睛看向的点 是物体所处坐标的中心点
         * 第一个三个参数：cx cy  cz  是相机的位置  坐标系是眼坐标系 也就是变换后的世界坐标系
         * 第二行三个参数：是物体的坐标位置   坐标系是物体坐标系
         * 第三行三个参数： 010  说明是头部朝向  没有加倾斜角度
         * */

        GLU.gluLookAt(gl,
                cx/100,cy/100,cz/100,//除100保证在视线范围内
                0,0,0,   //默认视角移动时，物体在原点
                0, 1, 0);


        TriPrism prism;
        for (int i = 0; i < mArrPrism.size(); i++) {
            prism = mArrPrism.get(i);
            prism.onDraws(gl);
        }
    }





    public void showData(float[][] mDataGrid, float mDataMax, int col, int row) {
        TriPrism prism;

        int col_cen = (col % 2 == 0 ? col / 2 : col / 2 + 1);
        int row_cen = (row % 2 == 0 ? row / 2 : row / 2 + 1);

        TriPrism.SCALESHOW_COORD = RainISORenderer2.SHOW_MAX_H / (col_cen >= row_cen ? col_cen : row_cen);
        TriPrism.SCALESHOW_VALUE = RainISORenderer2.SHOW_MAX_V / 250;

        float v00, v10, v01, v11;
        //都加+0.1f
        for (int i = 0; i < mDataGrid.length - 1; i++) {
            for (int j = 0; j < mDataGrid[0].length - 1; j++) {
                mDataGrid[i][j] = mDataGrid[i][j] + 0.1f;
                mDataGrid[i + 1][j] = mDataGrid[i + 1][j] + 0.1f;
                mDataGrid[i][j + 1] = mDataGrid[i][j + 1] + 0.1f;
                mDataGrid[i + 1][j + 1] = mDataGrid[i + 1][j + 1] + 0.1f;

            }
        }

        for (int i = 0; i < mDataGrid.length - 1; i++) {
            for (int j = 0; j < mDataGrid[0].length - 1; j++) {

                v00 = mDataGrid[i][j];
                v10 = mDataGrid[i + 1][j];
                v01 = mDataGrid[i][j + 1];
                v11 = mDataGrid[i + 1][j + 1];
                /**
                 *     v00      v10
                 *
                 *
                 *     v01      v11
                 * */


                if (v00 >= -0.1) {
                    if (v11 < 0) {
                        if (v01 >= -0.1 && v10 >= -0.1) {
                            float[][] points = { { (col_cen - i), (j - row_cen), getShowData(v00) },
                                    { (col_cen - i), (j + 1 - row_cen), getShowData(v01) },
                                    { (col_cen - (i + 1)), (j - row_cen), getShowData(v10) } };
                            float[][] colors = new float[][] { this.getRainISOColor(v00), this.getRainISOColor(v01),
                                    this.getRainISOColor(v10) };
                            int[][] indexs = { { i, j }, { i, j + 1 }, { i + 1, j } };
                            prism = new TriPrism(mContext,points, colors, indexs, mDataGrid);
                            mArrPrism.add(prism);
                        }
                    } else {
                        if (v10 >= -0.1) {
                            float[][] points = { { (col_cen - i), (j - row_cen), getShowData(v00) },
                                    { (col_cen - (i + 1)), (j + 1 - row_cen), getShowData(v11) },
                                    { (col_cen - (i + 1)), (j - row_cen), getShowData(v10) } };
                            float[][] colors = new float[][] { this.getRainISOColor(v00), this.getRainISOColor(v11),
                                    this.getRainISOColor(v10) };
                            int[][] indexs = { { i, j }, { i + 1, j + 1 }, { i + 1, j } };
                            prism = new TriPrism(mContext,points, colors, indexs, mDataGrid);
                            mArrPrism.add(prism);
                        }
                        if (v01 >= -0.1) {
                            float[][] points = { { (col_cen - i), (j - row_cen), getShowData(v00) },
                                    { (col_cen - i), (j + 1 - row_cen), getShowData(v01) },
                                    { (col_cen - (i + 1)), (j + 1 - row_cen), getShowData(v11) } };
                            float[][] colors = new float[][] { this.getRainISOColor(v00), this.getRainISOColor(v01),
                                    this.getRainISOColor(v11) };
                            int[][] indexs = { { i, j }, { i, j + 1 }, { i + 1, j + 1 } };
                            prism = new TriPrism(mContext,points, colors, indexs, mDataGrid);
                            mArrPrism.add(prism);
                        }
                    }
                }
            }
        }
    }

    private float getShowData(float v) {
        if (v < 1) {
            return 2 + 30;
        } else {
            return v * 2 + 30;
        }
    }

    public float[] getRainISOColor(float v) {
        int index = -1;
        for (int i = 0; i < RAIN_LEVEL.length; i++) {
            if (v >= RAIN_LEVEL[i] && v < RAIN_LEVEL[i + 1]) {
                index = i;
                break;
            }
        }

        if (index == RAIN_LEVEL.length - 1) {
            return RAIN_COLOR[RAIN_LEVEL.length - 1];
        } else if (index >= 0) {
            float rain_value_0 = RAIN_LEVEL[index];
            float rain_value_1 = RAIN_LEVEL[index + 1];
            float[] rain_color_0 = RAIN_COLOR[index];
            float[] rain_color_1 = RAIN_COLOR[index + 1];

            float rain_r = rain_color_0[0]
                    + ((rain_color_1[0] - rain_color_0[0]) / (rain_value_1 - rain_value_0)) * (v - rain_value_0);
            float rain_g = rain_color_0[1]
                    + ((rain_color_1[1] - rain_color_0[1]) / (rain_value_1 - rain_value_0)) * (v - rain_value_0);
            float rain_b = rain_color_0[2]
                    + ((rain_color_1[2] - rain_color_0[2]) / (rain_value_1 - rain_value_0)) * (v - rain_value_0);

            return new float[] { rain_r, rain_g, rain_b, 1f };
        }

        return RAIN_COLOR[0];
    }


}
