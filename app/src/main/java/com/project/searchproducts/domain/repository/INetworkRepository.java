package com.project.searchproducts.domain.repository;

import com.project.searchproducts.domain.models.SearchData;

import io.reactivex.rxjava3.core.Single;

public interface INetworkRepository {
    Single<String> searchProducts(SearchData searchData);

    Single<String> searchProductByTag(String tag);

    Single<String> detailsProduct(String productId);
}
