package com.phoenix.base.network;

import com.phoenix.base.network.converter.CustomGsonConverterFactory;
import com.phoenix.base.network.request.RequestApi;
import com.phoenix.base.network.response.ResponseTransformer;
import com.phoenix.base.network.schedulers.SchedulerProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class NetworkManager {
    private static volatile NetworkManager instance;
    private static Retrofit retrofit;
    private static volatile RequestApi request = null;

    private NetworkManager() {
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }
        return instance;
    }

    public void init() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(RequestApi.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .build();
    }

    public static RequestApi getRequest() {
        if (request == null) {
            synchronized (RequestApi.class) {
                request = retrofit.create(RequestApi.class);
            }
        }
        return request;
    }

    public <T> Observable<T> handle(Observable<Response<T>> observable) {
        return observable.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers());
    }
}
