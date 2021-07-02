package com.project.searchproducts.data.network;

import com.project.searchproducts.utils.SortType;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface INetworkApi {
    @GET("ua/search")
    Observable<String> search(@Query("search_term") String searchTerm,
                              @Query("price_local__gte") String minPrice,
                              @Query("price_local__lte") String maxPrice,
                              @Query("sort") SortType sort,
                              @Query("page") String page);

    @GET("ua/{tag}")
    Observable<String> searchByTag(@Path("tag") String tag);

    @GET("ua/{id}")
    Observable<String> details(@Path("id") String productId);
}
