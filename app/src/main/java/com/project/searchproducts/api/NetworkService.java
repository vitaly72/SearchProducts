package com.project.searchproducts.api;

import com.project.searchproducts.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NetworkService {
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.BASE.URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build();

    public static <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static INetworkApi createService() {
        return retrofit.create(INetworkApi.class);
    }
}
