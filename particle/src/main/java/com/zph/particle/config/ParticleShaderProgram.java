package com.zph.particle.config;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.zph.particle.R;
import com.zph.particle.util.ShaderUtil;
import com.zph.particle.util.TextResourceReader;

/**
 * Created by zph on 2017/10/13.
 */

public class ParticleShaderProgram {
    private Context mContext;

    protected static final String U_TIME = "u_Time";
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_DIRECTION_VECTOR = "a_DirectionVector";
    protected static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";

    // Uniformlocations
    private int uMatrixLocation;
    private int uTimeLocation;
    // Attributelocations
    private int aPositionLocation;
    private int aColorLocation;
    private int aDirectionVectorLocation;
    private int aParticleStartTimeLocation;

    // Shader着色器的代码
    private String mVertexShader;
    private String mFragmentShader;
    // program ID；
    int mProgram;

    /**
     * @param mv GLSurfaceView子类对象, 显示3D画面的载体
     */
    public ParticleShaderProgram(Context context, GLSurfaceView mv) {
        mContext = context;
        initShader(mv);
    }

    /**
     * 初始化着色器
     * 流程 :
     * ① 从资源中获取顶点 和 片元着色器脚本
     * ② 根据获取的顶点 片元着色器脚本创建着色程序
     * ③ 从着色程序中获取顶点位置引用 ,
     * 顶点颜色引用, 总变换矩阵引用
     *
     * @param mv MyTDView对象, 是GLSurfaceView对象
     */
    private void initShader(GLSurfaceView mv) {
    /*mVertextShader是顶点着色器脚本代码 调用工具类方法获取着色器脚本代码, 着色器脚本代码放在assets目录中
         * 传入的两个参数是 脚本名称 和 应用的资源 应用资源Resources就是res目录下的那写文件
		 */
        mVertexShader = TextResourceReader
                .readTextFileFromResource(mContext, R.raw.particle_vertex_sharde);
        mFragmentShader = TextResourceReader
                .readTextFileFromResource(mContext, R.raw.particle_fragment_sharde);

		/*
		 * 创建着色器程序, 传入顶点着色器脚本 和 片元着色器脚本 注意顺序不要错
		 */
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

		/*
		 * 从着色程序中获取一致变量
		 */
        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, U_MATRIX);
        uTimeLocation = GLES20.glGetUniformLocation(mProgram, U_TIME);
		/*
		 * 从着色程序中获取 属性变量 ,数据的引用 其中的"aPosition"是顶点着色器中的顶点位置信息
		 * 其中的"aColor"是顶点着色器的颜色信息
		 */
        aPositionLocation = GLES20.glGetAttribLocation(mProgram, A_POSITION);
        aColorLocation = GLES20.glGetAttribLocation(mProgram, A_COLOR);
        aDirectionVectorLocation = GLES20.glGetAttribLocation(mProgram,
                A_DIRECTION_VECTOR);
        aParticleStartTimeLocation = GLES20.glGetAttribLocation(mProgram,
                A_PARTICLE_START_TIME);
    }


}
