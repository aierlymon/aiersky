package com.example.myframework.http.listener;

/**
 * createBy ${huanghao}
 * on 2019/7/10
 */
public interface JsDownloadListener {
    void onStartDownload(long length);
    void onProgress(int progress);
    void onFail(String errorInfo);
    void onDownSuccess(String apkPath,String apkName);
}
