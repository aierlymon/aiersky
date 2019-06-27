package com.example.baselib.http.cookies;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public class CookieManager implements CookieJar {


    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if(cookies==null)return;
        if(url==null)return;
        if(cookies.size()>0){
            for (Cookie cookie:
                 cookies) {

            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return null;
    }
}
