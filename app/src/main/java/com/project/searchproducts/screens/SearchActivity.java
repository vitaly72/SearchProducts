package com.project.searchproducts.screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.searchproducts.R;
import com.project.searchproducts.adapters.GridSpacingItemDecoration;
import com.project.searchproducts.adapters.IOnCheckedFavorite;
import com.project.searchproducts.adapters.IOnClickListener;
import com.project.searchproducts.adapters.ProductAdapter;
import com.project.searchproducts.databinding.ActivitySearchBinding;
import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.ProductFavorite;
import com.project.searchproducts.models.SearchData;
import com.project.searchproducts.models.SeoLinks;
import com.project.searchproducts.network.SortType;
import com.project.searchproducts.utils.Constants;
import com.project.searchproducts.utils.JSONUtils;
import com.project.searchproducts.viewmodels.FavoriteProductViewModel;
import com.project.searchproducts.viewmodels.IOnClickListenerTag;
import com.project.searchproducts.viewmodels.ProductsViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements IOnClickListenerTag,
        IOnClickListener,
        IOnCheckedFavorite,
        IOnClickPopUpWindowPrice,
        IOnClickPopUpWindowSort {
    private ProductsViewModel productsViewModel;
    private FavoriteProductViewModel favoriteProductViewModel;
    private ActivitySearchBinding searchBinding;
    private ProductAdapter productAdapter;
    private PopUpWindowPrice popUpWindowPrice;
    private PopUpWindowSort popUpWindowSort;
    private List<SeoLinks> seoLinks;
    private SortType sortType = SortType.SCORE;
    private String minPrice = "";
    private String maxPrice = "";
    private int page = 1;
    private List<Double> priceRange = new ArrayList<>();

    @SuppressLint({"ResourceAsColor", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        favoriteProductViewModel = ViewModelProviders.of(this).get(FavoriteProductViewModel.class);
        searchBinding.setViewModel(productsViewModel);
        searchBinding.setLifecycleOwner(this);
        productsViewModel.setOnClickListenerTag(this);

        popUpWindowPrice = new PopUpWindowPrice();
        popUpWindowSort = new PopUpWindowSort();

        productAdapter = new ProductAdapter();
        productAdapter.setOnClickListener(this);
        searchBinding.recyclerViewMain.setLayoutManager(new GridLayoutManager(this, 2));
        searchBinding.recyclerViewMain.addItemDecoration(
                new GridSpacingItemDecoration(2, 40, false));
        searchBinding.recyclerViewMain.setAdapter(productAdapter);

        searchBinding.headerView.searchButton.setOnClickListener(v ->
                loadWithFilters(sortType, minPrice, maxPrice)
        );

        searchBinding.headerView.likesButton.setOnClickListener(v ->
                startActivity(new Intent(SearchActivity.this, FavoriteProductActivity.class))
        );

        searchBinding.textViewLoadMore.setOnClickListener(v -> loadNextPage());

        searchBinding.filtersViewGroup.pricesTextView.setOnClickListener(v -> {
            popUpWindowPrice.showPopupWindow(v);
            popUpWindowPrice.setIOnClickPopUpWindow(this);
            popUpWindowPrice.setPriceRange(Double.toString(priceRange.get(0)),
                    Double.toString(priceRange.get(1)));
        });

        searchBinding.filtersViewGroup.categoryTextView.setOnClickListener(v -> {
            popUpWindowSort.showPopupWindow(v);
            popUpWindowSort.setCurrentType(this.sortType);
            popUpWindowSort.setIOnClickPopUpWindow(this);
        });

        productAdapter.setOnCheckedFavorite(this);
    }

    private void loadNextPage() {
        searchBinding.progressIndicator.setVisibility(View.VISIBLE);
        page++;
        SearchData searchData = new SearchData("JBL", minPrice,
                maxPrice, sortType, Integer.toString(page));
        productsViewModel.search(searchData);

        productsViewModel.getProductsData().observe(this, products -> {
            searchBinding.progressIndicator.setVisibility(View.GONE);

            productAdapter.addProducts(products);
            priceRange = getPriceRange(products);
        });
    }

    private void loadWithFilters(SortType sortBy, String minPrice, String maxPrice) {
        searchBinding.progressIndicator.setVisibility(View.VISIBLE);
        String searchTerm = "JBL";
        this.sortType = sortBy;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        SearchData searchData = new SearchData(searchTerm, minPrice, maxPrice, sortBy, "");
        productsViewModel.search(searchData);

        productsViewModel.getProductsData().observe(this, products -> {
            searchBinding.backgroundImageView.setVisibility(View.GONE);
            searchBinding.progressIndicator.setVisibility(View.GONE);
            searchBinding.mainScrollView.setVisibility(View.VISIBLE);

            seoLinks = products.get(0).getSeoLinks();
            searchBinding.tagsViewGroup.setTags(seoLinks);
            productAdapter.setProducts(products);
            priceRange = getPriceRange(products);
        });
    }

    private List<Double> getPriceRange(List<Product> products) {
        List<Double> priceRange = new ArrayList<>();
        List<Double> prices = new ArrayList<>();

        for (Product product : products) {
            prices.add(Double.parseDouble(product.getPrice().replace(" грн.", "")));
        }

        priceRange.add(Collections.min(prices));
        priceRange.add(Collections.max(prices));

        return priceRange;
    }

    @Override
    public void onClick(Product product) {
        Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
        String movieJsonString = JSONUtils.getGsonParser().toJson(product);
        intent.putExtra(Constants.INTENT_KEY, movieJsonString);
        startActivity(intent);
    }

    @Override
    public void OnClickTag(int id) {
        searchBinding.progressIndicator.setVisibility(View.VISIBLE);
        productsViewModel.searchByTag(seoLinks.get(id).getLink());
        productAdapter.clear();
        seoLinks.clear();

        productsViewModel.getProductsData().observe(this, products -> {
            searchBinding.progressIndicator.setVisibility(View.GONE);
            seoLinks = products.get(0).getSeoLinks();
            searchBinding.tagsViewGroup.setTags(seoLinks);

            productAdapter.setProducts(products);
        });
    }

    @Override
    public void onClickPopUpWindow(String minPrice, String maxPrice) {
        loadWithFilters(sortType, minPrice, maxPrice);
    }

    @Override
    public void onClickPopUpWindow(SortType sortBy) {
        loadWithFilters(sortBy, minPrice, maxPrice);
    }

    @Override
    public void onChecked(boolean isCheck, int position) {
        Product product = productAdapter.getProducts().get(position);
        int id = getIdFromLink(product.getDetailsLink());
        System.out.println("isCheck = " + isCheck);
        if (isCheck) {
            ProductFavorite productFavorite = new ProductFavorite(
                    id,
                    product.getTitle(),
                    product.getPrice(),
                    product.getPresence()
            );
            favoriteProductViewModel.insertFavouriteProduct(productFavorite);
        } else {
            favoriteProductViewModel.deleteFavouriteProduct(id);
        }
    }

    private int getIdFromLink(String link) {
        String idStr = link.split("-")[0].replaceAll("[^0-9]", "");

        return Integer.parseInt(idStr);
    }
}