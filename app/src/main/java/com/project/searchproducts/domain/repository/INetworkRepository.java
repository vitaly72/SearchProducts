package com.project.searchproducts.domain.repository;

import com.project.searchproducts.domain.models.SearchData;

import io.reactivex.rxjava3.core.Observable;

public interface INetworkRepository {
    Observable<String> searchProducts(SearchData searchData);

    Observable<String> searchProductByTag(String tag);

    Observable<String> detailsProduct(String productId);
}
