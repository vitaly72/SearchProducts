package com.project.searchproducts.network;

import com.project.searchproducts.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
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

    /**
     * Повертає об'єкт класу INetworkApi для запитів
     * @return
     */
    public static INetworkApi createService() {
        return retrofit.create(INetworkApi.class);
    }
}
