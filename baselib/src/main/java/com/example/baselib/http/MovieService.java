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

    //加载天气
    @GET("adat/sk/{cityId}.html")
    Observable<TestBean> loadDataByRetrofit(@Path("cityId") String cityId);

}
