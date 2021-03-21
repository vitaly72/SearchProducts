package com.project.searchproducts.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface INetworkApi {
    @GET("search")
    Call<String> search(@Query("search_term") String searchTerm);
//    Call<String> search(@Query("search_term") String searchTerm,
//                        @Query("price_local__gte") String priceMin
//                        @Query("page") String page
//                        @Query("category") String category
//                        );
}
