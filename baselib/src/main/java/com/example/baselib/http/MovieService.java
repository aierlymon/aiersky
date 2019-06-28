package com.example.baselib.http;

import com.example.baselib.http.bean.TestBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * createBy ${huanghao}
 * on 2019/6/28
 */
public interface MovieService {

    //baseUrl
    String BASE_URL = "http://www.weather.com.cn/";

    int DEFAULT_TIME_OUT=8;//超时时间 单位（s）秒

    //加载天气
    @GET("adat/sk/{cityId}.html")
    Observable<TestBean> loadDataByRetrofit(@Path("cityId") String cityId);

}
