package com.example.baselib.http;


import com.example.baselib.http.bean.TestBean;
import com.example.baselib.http.bean.UpdateBean;

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

    //这个是以后也不能删除的
    @GET("adat/sk/")
    Observable<UpdateBean> checkUpdate();

    //这个是以后都不能删除的
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);




    // http://www.weather.com.cn/adat/sk/101190201.html
    //加载天气
    @GET("adat/sk/{cityId}.html")
    Observable<TestBean> loadCityDate(@Path("cityId") String cityId);

}
