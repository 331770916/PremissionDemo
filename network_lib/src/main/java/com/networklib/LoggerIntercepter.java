package com.networklib;

import java.io.IOException;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by zwb on 20/1/2.
 * 配置http请求日志
 */
public class LoggerIntercepter implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();

        String url = originRequest.url().toString();
        String logMessage = "request url--->" + url + "\n" + "request headers" + "\n";

        Headers requestHeaders = originRequest.headers();
        Set<String> names = requestHeaders.names();
        for(String name : names){
            logMessage = logMessage + requestHeaders.values(name) + "\n";
        }
        RequestBody requestBody = originRequest.body();
        if(requestBody != null && requestBody instanceof FormBody){
            FormBody formBody = (FormBody)requestBody;
            logMessage = logMessage + "parameters:\n";
            for (int i = 0; i < formBody.size(); i++) {
                logMessage = logMessage + "name:" + formBody.name(i) + "   value:" + formBody.value(i) + "\n";
            }
        }

        Response response = chain.proceed(originRequest);
        String responseBodyStr = response.body().string();

        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), responseBodyStr))
                .build();
    }
}
