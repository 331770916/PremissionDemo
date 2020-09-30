package com.networklib;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HttpManager {

    private static String BASE_URL;

    private static long TIMEOUT;

    private static Context mContext;
    private  static HttpManager mHttpManager;

    private static OkHttpClient httpClient;

    private static Object retrofitService;

    private static Object generalRetrofitService;

    private static Object stringRetrofitService;


    public static ErrorTransformer transformer = new ErrorTransformer();


    private HttpManager(Context context){
        mContext= context;

        httpClient = new OkHttpClient.Builder()
                .addInterceptor(new HeaderIntercepter())
//                .addInterceptor(new HebeiParamsIntercepter(mContext))
                .addInterceptor(new LoggerIntercepter())
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public static HttpManager getInstance(Context context) {

        if (mHttpManager == null) {

            synchronized (HttpManager.class) {
                mHttpManager = new HttpManager(context);
            }

        }

        return mHttpManager;
    }

    /**
     * @param baseUrl
     * @param timeout
     * 连接超时时间 秒
     * @param service
     * 服务端通信接口
     * **/
    public <T> void init(String baseUrl, long timeout, Class<T> service) {
        BASE_URL = baseUrl;
        TIMEOUT = timeout;
        //初始化Retrofit
        retrofitService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                // 添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                // 添加Retrofit到RxJava的转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(service);

        generalRetrofitService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                // Gson转换器
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                // 添加Retrofit到RxJava的转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(service);

        stringRetrofitService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //字符串转换器
                .addConverterFactory(ScalarsConverterFactory.create())
                // 添加Retrofit到RxJava的转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(service);
    }


    @SuppressWarnings("unchecked")
    public  <T> T getRetrofitService(Class<T> tClass) {
        return  (T)  retrofitService;
    }

    @SuppressWarnings("unchecked")
    public <T> T getCustomRetrofitService(Class<T> tClass) {
        return (T) generalRetrofitService;
    }
    @SuppressWarnings("unchecked")
    public  <T> T getStringRetrofitService(Class<T> tClass) {
        return (T) stringRetrofitService;
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> tClass){
        return (T) generalRetrofitService;
    }

    /**
     * 线程调度
     */
    public <T> ObservableTransformer<T, T> defaultScheduleTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if(disposable.isDisposed())
                                    return;
//                                // 可添加网络连接判断等

                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    private Gson buildGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                // 此处可以添加Gson 自定义TypeAdapter
                .create();
    }

    //处理错误的变换
    @SuppressWarnings("unchecked")
    private static class ErrorTransformer<T> implements ObservableTransformer {

        @Override
        public ObservableSource apply(Observable upstream) {
            //onErrorResumeNext当发生错误的时候，由另外一个Observable来代替当前的Observable并继续发射数据


            return (Observable<T>) upstream.map(new HandleFuc<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
        }
    }


    public static class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(Throwable throwable) throws Exception {
            return Observable.error(ExceptionHandle.handleException(throwable));
        }
    }

    @SuppressWarnings("unchecked")
    public static class HandleFuc<T> implements Function<ResponseBody, BaseResponse<T>> {
        @Override
        @SuppressWarnings("unchecked")
        public BaseResponse<T> apply(ResponseBody response) throws Exception {
            //response中code码不会0 出现错误


            String result = response.string();

            BaseResponse baseResponse = new BaseResponse();

            Object objson = new JSONTokener(result).nextValue();

            if (objson instanceof JSONObject) {

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    baseResponse.setData(jsonObject);


                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    code = TextUtils.isEmpty(code) ? "1" : code;

                    if ("0".equals(code) || "200".equals(code)) {
                        baseResponse.setCode(code);
                        baseResponse.setMsg(msg);
                        baseResponse.setData(jsonObject);
                    } else {
                        baseResponse.setCode(code);
                        baseResponse.setMsg(msg);
                        baseResponse.setData(jsonObject);
                    }

                } catch (Exception e) {
                    throw new RuntimeException("解析异常");
                }

            } else if (objson instanceof JSONArray) {
                baseResponse.setCode("0");
                JSONArray jsonArray = new JSONArray(result);
                baseResponse.setData(jsonArray);
            }



            if (!baseResponse.isOk())
                throw new RuntimeException(baseResponse.getCode() + "" + baseResponse.getMsg() != null ? baseResponse.getMsg() : "");
            return baseResponse;
        }
    }

}
