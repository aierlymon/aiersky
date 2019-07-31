package com.example.myframework.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baselib.http.listener.JsDownloadListener;
import com.example.baselib.utils.UpdateUtil;
import com.example.model.bean.UpdateBean;
import com.example.mytcpandws.utils.MyLog;

/**
 * createBy ${huanghao}
 * on 2019/7/10
 */
public class ThirdActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private UpdateUtil updateUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUtil = new UpdateUtil(this, new JsDownloadListener() {
            @Override
            public void onStartDownload(long length) {
                MyLog.i("我已经来到了开始下载了");
                runOnUiThread(() -> {
                    pd = new ProgressDialog(ThirdActivity.this);
                    pd.setOnKeyListener((dialog, keyCode, event) -> {
                        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                            return true;
                        } else {
                            return false; //默认返回 false
                        }
                    });
                    pd.setTitle("请稍等");
                    //设置对话进度条样式为水平
                    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    //设置提示信息
                    pd.setMessage("正在玩命下载中......");
                    //设置对话进度条显示在屏幕顶部（方便截图）
                    pd.getWindow().setGravity(Gravity.CENTER);
                    pd.setCancelable(false);
                    pd.setMax(100);
                    pd.show();//调用show方法显示进度条对话框
                });

            }

            @Override
            public void onProgress(int progress) {
                pd.setProgress(progress);
                MyLog.i("下载进度为: " + progress);
            }

            @Override
            public void onFail(int errorType, String errorInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ThirdActivity.this, "检测下载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDownSuccess(Intent intent) {
                pd.dismiss();
                startActivity(intent);
            }


        });

        UpdateBean updateBean = new UpdateBean();
        updateBean.setApk_name("jiaz.apk");
        updateBean.setApkUrl("http://192.168.1.100:8080/jiaz.apk");
        updateBean.setVersionCode(2);
        updateBean.setNew_md5("aa9df1b8cf3cd44e58465b1d4545360b");
        updateBean.setTarget_size("5M");
        updateBean.setVersionName("1.0.0");
        updateBean.setUpdateLog("测试用例");

        updateUtil.testUpdate(this,updateBean);
       // updateUtil.checkUpdate(MyHttpMethods.getInstance());

        //测试的时候把方法设置为public
        //updateUtil.showDialog(this, updateBean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateUtil.clearDisposable();
    }
}
