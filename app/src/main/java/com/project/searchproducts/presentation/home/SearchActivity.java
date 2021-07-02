package com.project.searchproducts.presentation.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
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
    private final PopUpWindowPrice popUpWindowPrice = new PopUpWindowPrice();
    private final PopUpWindowSort popUpWindowSort = new PopUpWindowSort();

    private List<SeoLinks> seoLinks;
    private SortType sortType = SortType.SCORE;
    private SearchData searchData = null;
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

        initRecyclerView();
        productAdapter.setOnClickListener(this);
        productAdapter.setOnCheckedFavorite(this);

        searchBinding.headerView.searchButton.setOnClickListener(this::searchButtonOnClickListener);

        searchBinding.headerView.likesButton.setOnClickListener(v ->
                startActivity(new Intent(SearchActivity.this, FavoriteProductActivity.class))
        );

        searchBinding.textViewLoadMore.setOnClickListener(v -> loadNextPage(searchData));

        searchBinding.filtersViewGroup.pricesTextView.setOnClickListener(this::pricesTextViewOnClickListener);
        searchBinding.filtersViewGroup.categoryTextView.setOnClickListener(this::categoryTextViewOnClickListener);

        searchBinding.headerView.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            editTextKeyboardEnterPressEvent(v, actionId, event);
            return false;
        });
    }

    private void initRecyclerView() {
        productAdapter = new ProductAdapter();
        searchBinding.recyclerViewMain.setLayoutManager(new GridLayoutManager(this, 2));
        searchBinding.recyclerViewMain.addItemDecoration(
                new GridSpacingItemDecoration(2, 40, false));
        searchBinding.recyclerViewMain.setAdapter(productAdapter);
    }

    private void pricesTextViewOnClickListener(View view) {
        popUpWindowPrice.showPopupWindow(view);
        popUpWindowPrice.setIOnClickPopUpWindow(this);
        popUpWindowPrice.setPriceRange(
                Double.toString(priceRange.get(0)),
                Double.toString(priceRange.get(1))
        );
    }

    private void categoryTextViewOnClickListener(View view) {
        popUpWindowSort.showPopupWindow(view);
        popUpWindowSort.setCurrentType(this.sortType);
        popUpWindowSort.setIOnClickPopUpWindow(this);
    }

    private void searchButtonOnClickListener(View v) {
        if (searchBinding.headerView.searchEditText.getText().toString().isEmpty()) {
            searchBinding.headerView.searchEditText.setHintTextColor(Color.RED);
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager)
                getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

        NetDetect.check((isConnected -> {
            if (!isConnected) {
                makeSnackBar(v);
            } else {
                if (searchData == null) {
                    searchData = new SearchData(
                            searchBinding.headerView.searchEditText.getText().toString(),
                            "", "", sortType, ""
                    );
                } else {
                    searchData.setSearchTerm(searchBinding.headerView.searchEditText.getText().toString());
                }
                loadWithFilters(searchData);
            }
        }));
    }

    private void editTextKeyboardEnterPressEvent(View v, int actionId, KeyEvent event) {
        if ((event != null &&
                (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                (actionId == EditorInfo.IME_ACTION_DONE)) {
            NetDetect.check((isConnected -> {
                if (!isConnected) {
                    makeSnackBar(v);
                } else {
                    if (searchData == null) {
                        searchData = new SearchData(
                                searchBinding.headerView.searchEditText.getText().toString(),
                                "", "", sortType, ""
                        );
                    } else {
                        searchData.setSearchTerm(searchBinding.headerView.searchEditText.getText().toString());
                    }
                    loadWithFilters(searchData);
                }
            }));
        }
    }

    private void loadNextPage(SearchData searchData) {
        searchBinding.progressIndicator.setVisibility(View.VISIBLE);
        searchData.nextPage();
        searchData.setPage(Integer.toString(page));
        productsViewModel.search(searchData);

        productsViewModel.getProductsData().observe(this, products -> {
            searchBinding.progressIndicator.setVisibility(View.GONE);

            productAdapter.addProducts(products);
            priceRange = getPriceRange(products);
        });
    }

    private void loadWithFilters(SearchData searchData) {
        if (searchData.getSearchTerm().equals("")) return;
        searchBinding.progressIndicator.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(Product product) {
        Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
        String productJsonString = JSONUtils.getGsonParser().toJson(product);
        intent.putExtra(Constants.INTENT_KEY, productJsonString);
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

            productAdapter.setProducts(products);
        });
    }

    @Override
    public void onClickPopUpWindow(String minPrice, String maxPrice) {
        searchData.setMinPrice(minPrice);
        searchData.setMaxPrice(maxPrice);
        loadWithFilters(searchData);
    }

    @Override
    public void onClickPopUpWindow(SortType sortBy) {
        searchData.setSort(sortBy);
        loadWithFilters(searchData);
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
