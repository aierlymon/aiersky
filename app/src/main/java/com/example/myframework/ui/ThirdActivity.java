package com.example.myframework.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.myframework.http.bean.UpdateBean;
import com.example.myframework.http.listener.JsDownloadListener;
import com.example.myframework.util.UpdateUtil;
import com.example.mytcpandws.utils.MyLog;

/**
 * createBy ${huanghao}
 * on 2019/7/10
 */
public class ThirdActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UpdateUtil updateUtil = new UpdateUtil(this, new JsDownloadListener() {
            @Override
            public void onStartDownload(long length) {

            }

            @Override
            public void onProgress(int progress) {
                MyLog.i("下载进度为: " + progress);
            }

            @Override
            public void onFail(int errorType,String errorInfo) {

            }

            @Override
            public void onDownSuccess(Intent intent) {
                startActivity(intent);
            }


        });
        UpdateBean updateBean = new UpdateBean();
        updateBean.setApk_name("jiaz.apk");
        updateBean.setApkUrl("http://192.168.43.90:8080/jiaz.apk");
        updateBean.setVersionCode(2);
        updateBean.setNew_md5("aa9df1b8cf3cd44e58465b1d4545360b");
        updateBean.setTarget_size("5M");
        updateBean.setVersionName("1.0.0");
        updateBean.setUpdateLog("测试用例");
        // updateUtil.checkUpdate();
        updateUtil.showDialog(this, updateBean);
    }
}
