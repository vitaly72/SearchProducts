package com.project.searchproducts.api;

import androidx.lifecycle.MutableLiveData;

import com.project.searchproducts.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class NetworkRepository {
    private static NetworkRepository instance;
//    private LiveData<List<Movie>> movieList;

    private NetworkRepository() {
    }

    public static NetworkRepository getInstance() {
        if (instance == null) instance = new NetworkRepository();
        return instance;
    }

    public MutableLiveData<String> searchProducts(String searchTerm) {
        MutableLiveData<String> data = new MutableLiveData<>();

        NetworkService.createService()
                .search(searchTerm)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NotNull Call<String> call,
                                           @NotNull Response<String> response) {
                        System.out.println("response = " + response.toString());
                        data.setValue(response.toString());
                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call,
                                          @NotNull Throwable throwable) {
                        data.setValue(null);
                    }
                });
        return data;
    }

//    public LiveData<List<Movie>> getMovieList() {
//        return movieList;
//    }
}
