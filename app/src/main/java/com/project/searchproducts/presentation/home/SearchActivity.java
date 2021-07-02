package com.project.searchproducts.presentation.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.narify.netdetect.NetDetect;
import com.project.searchproducts.R;
import com.project.searchproducts.databinding.ActivitySearchBinding;
import com.project.searchproducts.domain.models.Product;
import com.project.searchproducts.domain.models.ProductFavorite;
import com.project.searchproducts.domain.models.SearchData;
import com.project.searchproducts.domain.models.SeoLinks;
import com.project.searchproducts.presentation.details.DetailsActivity;
import com.project.searchproducts.presentation.favorite.FavoriteProductActivity;
import com.project.searchproducts.presentation.favorite.FavoriteProductViewModel;
import com.project.searchproducts.presentation.favorite.IOnCheckedFavorite;
import com.project.searchproducts.presentation.home.popupwindows.IOnClickPopUpWindowPrice;
import com.project.searchproducts.presentation.home.popupwindows.IOnClickPopUpWindowSort;
import com.project.searchproducts.presentation.home.popupwindows.PopUpWindowPrice;
import com.project.searchproducts.presentation.home.popupwindows.PopUpWindowSort;
import com.project.searchproducts.utils.Constants;
import com.project.searchproducts.utils.JSONUtils;
import com.project.searchproducts.utils.SortType;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

import static com.project.searchproducts.utils.Helper.getIdFromLink;
import static com.project.searchproducts.utils.Helper.getPriceRange;
import static com.project.searchproducts.utils.Helper.makeSnackBar;

@AndroidEntryPoint
public class SearchActivity extends AppCompatActivity implements IOnClickListenerTag,
        IOnClickListener,
        IOnCheckedFavorite,
        IOnClickPopUpWindowPrice,
        IOnClickPopUpWindowSort {
    private ProductsViewModel productsViewModel;
    private FavoriteProductViewModel favoriteProductViewModel;
    private ActivitySearchBinding searchBinding;
    private ProductAdapter productAdapter;
    private PopUpWindowPrice popUpWindowPrice = new PopUpWindowPrice();
    private PopUpWindowSort popUpWindowSort = new PopUpWindowSort();
    private List<SeoLinks> seoLinks;
    private SortType sortType = SortType.SCORE;
    private String searchTerm = "";
    private String minPrice = "";
    private String maxPrice = "";
    private int page = 1;
    private List<Double> priceRange = new ArrayList<>();

    @SuppressLint({"ResourceAsColor", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        productsViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
        favoriteProductViewModel = new ViewModelProvider(this).get(FavoriteProductViewModel.class);
        NetDetect.init(this);

        searchBinding.setViewModel(productsViewModel);
        searchBinding.setLifecycleOwner(this);
        productsViewModel.setOnClickListenerTag(this);

        productAdapter = new ProductAdapter();
        productAdapter.setOnClickListener(this);
        productAdapter.setOnCheckedFavorite(this);
        searchBinding.recyclerViewMain.setLayoutManager(new GridLayoutManager(this, 2));
        searchBinding.recyclerViewMain.addItemDecoration(
                new GridSpacingItemDecoration(2, 40, false));
        searchBinding.recyclerViewMain.setAdapter(productAdapter);

        searchBinding.headerView.searchButton.setOnClickListener(v ->
                NetDetect.check((isConnected -> {
                    if (!isConnected) {
                        makeSnackBar(v);
                    } else {
                        searchTerm = searchBinding.headerView.searchEditText.getText().toString();
                        loadWithFilters(searchTerm, sortType, minPrice, maxPrice);
                    }
                }))
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

        searchBinding.headerView.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                    || (actionId == EditorInfo.IME_ACTION_DONE)) {
                NetDetect.check((isConnected -> {
                    if (!isConnected) {
                        makeSnackBar(v);
                    } else {
                        searchTerm = searchBinding.headerView.searchEditText.getText().toString();
                        loadWithFilters(searchTerm, sortType, minPrice, maxPrice);
                    }
                }));
            }
            return false;
        });
    }

    private void loadNextPage() {
        searchBinding.progressIndicator.setVisibility(View.VISIBLE);
        page++;
        SearchData searchData = new SearchData(searchTerm, minPrice,
                maxPrice, sortType, Integer.toString(page));
        productsViewModel.search(searchData);

        productsViewModel.getProductsData().observe(this, products -> {
            searchBinding.progressIndicator.setVisibility(View.GONE);

            productAdapter.addProducts(products);
            priceRange = getPriceRange(products);
        });
    }

    private void loadWithFilters(String searchTerm, SortType sortBy, String minPrice, String maxPrice) {
        if (searchTerm.equals("")) return;
        searchBinding.progressIndicator.setVisibility(View.VISIBLE);
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
//            searchBinding.tagsViewGroup.setTags(seoLinks.get(0));
            productAdapter.setProducts(products);
            priceRange = getPriceRange(products);
        });
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
        searchBinding.mainScrollView.setVisibility(View.GONE);
        productsViewModel.searchByTag(seoLinks.get(id).getLink());
        productAdapter.clear();
        seoLinks.clear();

        productsViewModel.getProductsData().observe(this, products -> {
            searchBinding.progressIndicator.setVisibility(View.GONE);
            searchBinding.mainScrollView.setVisibility(View.VISIBLE);

            seoLinks = products.get(0).getSeoLinks();
//            searchBinding.tagsViewGroup.setTags(seoLinks);

            productAdapter.setProducts(products);
        });
    }

    @Override
    public void onClickPopUpWindow(String minPrice, String maxPrice) {
        loadWithFilters(searchTerm, sortType, minPrice, maxPrice);
    }

    @Override
    public void onClickPopUpWindow(SortType sortBy) {
        loadWithFilters(searchTerm, sortBy, minPrice, maxPrice);
    }

    @Override
    public void onChecked(boolean isCheck, int position) {
        Product product = productAdapter.getProducts().get(position);
        int id = getIdFromLink(product.getDetailsLink());
        if (isCheck) {
            ProductFavorite productFavorite = new ProductFavorite(
                    id,
                    product.getTitle(),
                    product.getPrice(),
                    product.getPresence(),
                    product.getDetailsLink()
            );
            favoriteProductViewModel.insertFavouriteProduct(productFavorite);
        } else {
            favoriteProductViewModel.deleteFavouriteProduct(id);
        }
    }

    @Override
    public void onClick(Product product, View view) {
    }
}
