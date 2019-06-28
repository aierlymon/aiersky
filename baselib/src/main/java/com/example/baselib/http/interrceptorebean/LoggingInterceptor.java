package com.example.baselib.http.interrceptorebean;

import com.example.baselib.utils.MyLog;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * createBy ${huanghao}
 * on 2019/6/28
 * data 可以再请求时拦截，而且在拿到数据之后拦截
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        MyLog.i("request: "+chain.request().url().host()+"  thread: "+Thread.currentThread().getName());
        MyLog.i("response: "+chain.proceed(chain.request()).body().string()+" thread: "+Thread.currentThread().getName());
        return null;
    }
}
