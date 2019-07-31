package com.example.baselib.http.interrceptorebean;

import com.example.baselib.broadcast.NetWorkStateBroadcast;
import com.example.baselib.utils.MyLog;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * createBy ${huanghao}
 * on 2019/6/28
 * data 可以再请求时拦截，而且在拿到数据之后拦截
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        MyLog.i("request: "+request.toString());
        //当没有网络时
        if (!NetWorkStateBroadcast.isOnline.get()) {
            MyLog.i("我进来了没有网络");
            request = request.newBuilder()
                    //CacheControl.FORCE_CACHE; //仅仅使用缓存
                    //CacheControl.FORCE_NETWORK;// 仅仅使用网络
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response proceed = chain.proceed(request);

        if (NetWorkStateBroadcast.isOnline.get()) {
            MyLog.i("我进来了有网络");
            //有网络时
            return proceed.newBuilder()
                    //清除头信息
                    .removeHeader("Pragma")
                    //设置在线缓存时间为60秒，
                    .header("Cache-Control", "public, max-age=" + 60)
                    .build();
        } else {
            MyLog.i("我进来了无网络是偶");
            //没网络时
            int maxTime =  24 * 60 * 60;//离线缓存时间：1天
            return proceed.newBuilder()
                    .removeHeader("Pragma")
                    //设置离线缓存时间为4周，
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxTime)
                    .build();
        }
    }

}
