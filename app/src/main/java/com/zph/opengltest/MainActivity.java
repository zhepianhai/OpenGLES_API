package com.zph.opengltest;

import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.huitu.app.engine.HTMainNet;
import com.huitu.app.util.HTUtilData;
import com.huitu.app.util.HTUtilNet;
import com.zph.opengltest.render.RainISORenderer2;
import com.zph.opengltest.view.OpenGLView2;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private OpenGLView2 openGLView;
    private ProgressDialog myDialog;
    private int mDataCol;
    private int mDataRow;
    private static final String DATA_SPLIT = "-9999";
    private float[][] mDataGrid;
    private float mDataMax;
    private RainISORenderer2 mRender;
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            try {
                switch(msg.what){
                    case HTMainNet.MSG_WEB_DOWNLOAD_UPDATE:
                        MainActivity.this.showData();
                        if (myDialog != null && myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                        break;

                }
            } catch (Exception ignored) {
            }
        }
    };
    private void showData() {
        new Thread() {
            public void run() {
//                Log.i("TAG","mDataGrid:"+ Arrays.deepToString(mDataGrid));
                openGLView.showData(mDataGrid, mDataMax, mDataCol, mDataRow);
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
        initView();
        loadData();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(openGLView);

    }

    private void initVar() {
        mDataGrid = new float[0][0];
    }

    private void loadData() {
        myDialog=new ProgressDialog(this);
        myDialog.setTitle("Loading....");
        myDialog.setIcon(getResources().getDrawable(R.mipmap.ic_launcher_round));
        myDialog.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    String strUrlGL = "http://47.94.237.158:31700/RainISO/RainISO.aspx?stm=1506556800000&etm=1506567600000&kind=3";
                    String str_data = HTUtilNet.GetWebCont(strUrlGL, "UTF-8");
                    // Head
                    String str_data_head = str_data.substring(0, str_data.indexOf(DATA_SPLIT));
                    str_data_head = str_data_head.replace("ncols", "").replace("nrows", "").replace("xllcorner", "").replace("yllcorner", "")
                            .replace("cellsize", "").replace("NODATA_value", "").trim();
                    String[] arrDataHead = str_data_head.split("\t");

                    mDataCol = HTUtilData.GetDataInt(arrDataHead[0]);
                    mDataRow = HTUtilData.GetDataInt(arrDataHead[1]);
                    // Data
                    String str_data_body = str_data.substring(str_data.indexOf(DATA_SPLIT) + DATA_SPLIT.length());
                    String[] arrData = str_data_body.split(" ");
                    mDataGrid = new float[mDataCol][mDataRow];
                    mDataMax = 0;
                    for (int j = 0; j < mDataRow; j++) {
                        for (int i = 0; i < mDataCol; i++) {
                            mDataGrid[i][j] = HTUtilData.GetDataFloat(arrData[j * mDataCol + i]);
                            if (mDataMax < mDataGrid[i][j]) {
                                mDataMax = mDataGrid[i][j];
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                myHandler.sendEmptyMessage(HTMainNet.MSG_WEB_DOWNLOAD_UPDATE);
            }
        }.start();
    }

    private void initView() {
        openGLView=new OpenGLView2(this);
        openGLView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        openGLView.setZOrderOnTop(true);
        openGLView.requestFocus();//获取焦点
        openGLView.setFocusableInTouchMode(true);//设置为可触控
    }
}
