package com.example.myframework.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import com.example.myframework.http.bean.UpdateBean;
import com.example.myframework.http.listener.JsDownloadListener;
import com.example.myframework.util.UpdateUtil;
import com.example.mytcpandws.utils.MyLog;

import java.io.File;

/**
 * createBy ${huanghao}
 * on 2019/7/10
 */
public class ThirdActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UpdateUtil updateUtil=new UpdateUtil(this, new JsDownloadListener() {
            @Override
            public void onStartDownload(long length) {

            }

            @Override
            public void onProgress(int progress) {
                MyLog.i("下载进度为: "+progress);
            }

            @Override
            public void onFail(String errorInfo) {

            }

            @Override
            public void onDownSuccess(String apkPath, String apkName) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                MyLog.i("name: "+apkName+"  path: "+apkPath);
                //跳转启动apk有问题
           /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //android N的权限问题
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//授权读权限
                    Uri contentUri = FileProvider.getUriForFile(ThirdActivity.this, "com.cmic.family.guardian", new File(apkName, apkName));//注意修改
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(new File(apkPath, apkName)), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }*/
                startActivity(intent);
            }


        });
        UpdateBean updateBean=new UpdateBean();
        updateBean.setApk_name("jiaz.apk");
        updateBean.setApkUrl("http://192.168.43.31:8080/jiaz.apk");
        updateBean.setVersionCode(2);
        updateBean.setNew_md5("aa9df1b8cf3cd44e58465b1d4545360b");
        updateBean.setTarget_size("5M");
        updateBean.setVersionName("1.0.0");
        updateBean.setUpdateLog("测试用例");
       // updateUtil.checkUpdate();
        updateUtil.showDialog(this,updateBean);
    }
}
