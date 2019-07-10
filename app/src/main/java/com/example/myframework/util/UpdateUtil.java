package com.example.myframework.util;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.example.baselib.utils.CipherUtils;
import com.example.baselib.utils.MyLog;
import com.example.baselib.utils.PackageUtils;
import com.example.myframework.http.HttpConstant;
import com.example.myframework.http.HttpMethod;
import com.example.myframework.http.MovieService;
import com.example.myframework.http.bean.UpdateBean;
import com.example.myframework.http.interrceptorebean.JsDownloadInterceptor;
import com.example.myframework.http.listener.JsDownloadListener;
import com.example.myframework.ui.widgets.UpdateDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * createBy ${huanghao}
 * on 2019/7/10
 */
public class UpdateUtil implements LifecycleObserver {

    private Context context;
    private Retrofit retrofit;
    private JsDownloadListener jsDownloadListener;
    public UpdateUtil(Context context, JsDownloadListener jsDownloadListener) {
        this.jsDownloadListener=jsDownloadListener;
        this.context = context.getApplicationContext();
        //定义下载client
        JsDownloadInterceptor mInterceptor = new JsDownloadInterceptor(jsDownloadListener);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(HttpConstant.DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(HttpConstant.BASE_URL)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }


    private UpdateDialog builder;

    private List<Disposable> disposableList = new ArrayList<>();

    public boolean checkUpdate() {
        HttpMethod.getInstance().checkUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableList.add(d);
                    }

                    @Override
                    public void onNext(UpdateBean updateBean) {
                        //拿到新的版本号
                        int newVersionCode = updateBean.getVersionCode();
                        int versionCode = PackageUtils.getVersionCode(context);
                        if (newVersionCode > versionCode) {
                            //弹出更新框
                            showDialog(context, updateBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return false;
    }

    /**
     * 开始下载
     * @param url
     * @param file
     * @param subscriber
     */
    private void download(@NonNull String url, final File file, Observer subscriber) {
        retrofit.create(MovieService.class)
                .download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(responseBody -> responseBody.byteStream())
                .observeOn(Schedulers.computation()) // 用于计算任务
                .doOnNext(inputStream -> writeFile(inputStream, file))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    private void writeFile(InputStream inputString, File file) {
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            byte[] b = new byte[1024];

            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b,0,len);
            }
            inputString.close();
            fos.close();

        } catch (FileNotFoundException e) {
            jsDownloadListener.onFail("FileNotFoundException");
        } catch (IOException e) {
            jsDownloadListener.onFail("IOException");
        }

    }





    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestory() {
        for (Disposable d :
                disposableList) {
            if (!d.isDisposed())
                d.dispose();
        }
        disposableList.clear();
        disposableList = null;
        context = null;
    }

    public void showDialog(Context context, UpdateBean updateBean) {
        builder = UpdateDialog.Builder(context)
                .setTitle("是否更新到版本: " + updateBean.getVersionName())
                .setMessage("更新内容为" + updateBean.getUpdateLog() + "\r\n" + "更新大小为: " + updateBean.getTarget_size())
                .setOnConfirmClickListener("确定", view -> {
                    //开始下载文件
                    File file = new File(getApkPath(),updateBean.getApk_name());
                    download(updateBean.getApkUrl(), file, new Observer() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Object o) {
                            MyLog.i("完成了下载");
                            //md5比较，比较一致的情况下触发安装apk
                            File file1=new File(getApkPath(),updateBean.getApk_name());
                            String down_md5=CipherUtils.getFileMD5(file1);
                            if(updateBean.getNew_md5().equals(down_md5)){
                                //跳转安装apk
                                MyLog.i("md5校验成功");
                                jsDownloadListener.onDownSuccess(getApkPath(),updateBean.getApk_name());
                            }else{
                                jsDownloadListener.onFail("下载的文件的md5值不一样，请重新下载");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            jsDownloadListener.onFail(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
                    builder.dismiss();
                })
                .setOnCancelClickListener("取消", view -> {

                    if (builder != null) {
                        builder.dismiss();
                        this.context = null;
                    }
                })
                .build();
        builder.shown();
    }

    private String getApkPath() {
        String directoryPath="";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ) {//判断外部存储是否可用
            MyLog.i("路径: "+Environment.getExternalStorageDirectory().getPath());
            directoryPath =Environment.getExternalStorageDirectory().getPath();
        }else{//没外部存储就使用内部存储
            MyLog.i("路径: "+context.getFilesDir());
            directoryPath=context.getFilesDir()+File.separator+"apk";
        }
        File file = new File(directoryPath);
        if(!file.exists()){//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }
}
