package com.example.myframework.http;

import com.example.baselib.http.HttpMethod;
import com.example.baselib.http.bean.TestBean;

import io.reactivex.Observable;

/**
 * createBy ${huanghao}
 * on 2019/7/14
 */
public class MyHttpMethods extends HttpMethod {
    private static MyHttpMethods httpMethods;
    
    //获取单例
    public static MyHttpMethods getInstance() {
        if(httpMethods==null){
            httpMethods=new MyHttpMethods();
        }
        return httpMethods;
    }

    //测试用的
    public Observable<TestBean> getCityWeather(String cityId){
        return  getmMovieService().loadDataByRetrofit(cityId);
    }

}
