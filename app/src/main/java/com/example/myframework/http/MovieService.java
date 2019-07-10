package com.example.myframework.http;


import com.example.myframework.http.bean.TestBean;
import com.example.myframework.http.bean.UpdateBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * createBy ${huanghao}
 * on 2019/6/28
 */
public interface MovieService {

    // http://www.weather.com.cn/adat/sk/101190201.html
    //加载天气
    @GET("adat/sk/{cityId}.html")
    Observable<TestBean> loadDataByRetrofit(@Path("cityId") String cityId);

    @GET("adat/sk/")
    Observable<UpdateBean> checkUpdate();

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
