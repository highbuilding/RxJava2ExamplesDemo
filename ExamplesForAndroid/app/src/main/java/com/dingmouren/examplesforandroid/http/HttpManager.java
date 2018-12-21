package com.dingmouren.examplesforandroid.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author dingmouren
 */
public class HttpManager {

    private volatile static HttpManager sHttpManager = null;

    private static final int READ_TIME_OUT = 10;

    private static final int CONNECT_TIME_OUT = 10;

    private static final int WRITE_TIME_OUT = 10;

    private static final String BASE_URL_1 = "http://localhost:8080/";

    private static final String BASE_URL_2 = "http://10.0.2.2:8080/";

    /*OkHttp的构建者对象*/
    private static OkHttpClient.Builder sOkHttpBuilder = new OkHttpClient.Builder()
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIME_OUT,TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT,TimeUnit.SECONDS)
            .addInterceptor(HttpLogInterceptor.getInstance());


    /*Retrofit的构建者对象*/
    private static Retrofit.Builder sRetrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL_2)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());


    private HttpManager() {}//私有化构造函数

    /**
     * 单例模式获取实例对象
     * @return
     */
    public static HttpManager getInstance(){
        if (sHttpManager == null){
            synchronized (HttpManager.class){
                if (sHttpManager == null) sHttpManager = new HttpManager();
            }
        }
        return sHttpManager;
    }

    /**
     * 创建Api的service
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass){
        OkHttpClient okHttpClient = sOkHttpBuilder.build();
        Retrofit retrofit = sRetrofitBuilder.client(okHttpClient).build();
        return retrofit.create(serviceClass);
    }

}
