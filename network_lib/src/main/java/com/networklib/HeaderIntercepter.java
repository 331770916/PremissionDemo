package com.networklib;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderIntercepter implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        // 添加header
        builder.addHeader("Charset", "UTF-8");
        builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
        return chain.proceed(builder.build());
    }
}
