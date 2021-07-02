package com.project.searchproducts.data.repository;

import com.project.searchproducts.data.network.INetworkApi;
import com.project.searchproducts.domain.models.SearchData;
import com.project.searchproducts.domain.repository.INetworkRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class NetworkRepository implements INetworkRepository {
    private final INetworkApi networkApi;

    @Inject
    public NetworkRepository(INetworkApi networkApi) {
        this.networkApi = networkApi;
    }

    public Single<String> searchProducts(SearchData searchData) {
        return networkApi.search(searchData.getSearchTerm(),
                searchData.getMinPrice(),
                searchData.getMaxPrice(),
                searchData.getSort(),
                searchData.getPage());
    }

    public Single<String> searchProductByTag(String tag) {
        String tagLink = tag.replace("/ua/", "");

        return networkApi.searchByTag(tagLink);
    }

    public Single<String> detailsProduct(String productId) {
        String product = productId.replace("/ua/", "").split("\\?token")[0];

        return networkApi.details(product);
    }
}