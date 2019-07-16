package com.example.baselib.http;

import com.example.baselib.BuildConfig;
import com.example.baselib.http.bean.TestBean;
import com.example.baselib.http.bean.UpdateBean;
import com.example.baselib.http.interrceptorebean.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.baselib.http.HttpConstant.DEFAULT_TIME_OUT;

/**
 * createBy ${huanghao}
 * on 2019/6/28
 * data 以后所有请求的调用方法写在这里,然后所有请求路径和方式放到MovieService,由一个MovieService同意调用
 */
public class HttpMethod{
    public static Retrofit mRetrofit;
    //以后所有请求的调用方法写在这里,然后所有请求路径和方式放到MovieService,由一个MovieService同意调用
    private MovieService mMovieService;

    private static HttpMethod httpMethods;

    //获取单例
    public static HttpMethod getInstance() {
        if(httpMethods==null){
            httpMethods=new HttpMethod();
        }
        return httpMethods;
    }


    public HttpMethod() {
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接 超时时间
            builder.writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
            builder.readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//读操作 超时时间
            builder.retryOnConnectionFailure(true);//错误重连
            if (BuildConfig.DEBUG) {
                // Log信息拦截器
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                //设置 Debug Log 模式
                builder.addInterceptor(loggingInterceptor);
                builder.addInterceptor(new LoggingInterceptor());
            }
            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    //设置基础地址
                    .baseUrl(HttpConstant.BASE_URL)
                    //这个把返回的数据转换为gson
                    .addConverterFactory(GsonConverterFactory.create())
                    //这个为了支持rxjva
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();

            mMovieService = mRetrofit.create(MovieService.class);
        }

    }


    //正式更新用的，但是url地址到时候自己改
    public Observable<UpdateBean> checkUpdate() {
        return mMovieService.checkUpdate();
    }

    //测试用的
    public Observable<TestBean> getCityWeather(String cityId){
        return  mMovieService.loadCityDate(cityId);
    }
}
