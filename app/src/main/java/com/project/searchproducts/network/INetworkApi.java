package com.project.searchproducts.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface INetworkApi {
    @GET("ua/search")
    Call<String> search(@Query("search_term") String searchTerm,
                        @Query("price_local__gte") String minPrice,
                        @Query("price_local__lte") String maxPrice,
                        @Query("sort") SortType sort,
                        @Query("page") String page);

    @GET("ua/{tag}")
    Call<String> searchByTag(@Path("tag") String tag);


    @GET("ua/{id}")
    Call<String> details(@Path("id") String productId);
}
