package com.project.searchproducts.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.project.searchproducts.R;
import com.project.searchproducts.adapters.FavoriteProductAdapter;
import com.project.searchproducts.adapters.GridSpacingItemDecoration;
import com.project.searchproducts.adapters.IOnCheckedFavorite;
import com.project.searchproducts.adapters.ProductAdapter;
import com.project.searchproducts.databinding.ActivityFavoriteProductBinding;
import com.project.searchproducts.models.ProductFavorite;
import com.project.searchproducts.viewmodels.FavoriteProductViewModel;
import com.project.searchproducts.viewmodels.ProductsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavoriteProductActivity extends AppCompatActivity implements IOnCheckedFavorite {
    private FavoriteProductViewModel viewModel;
    private List<ProductFavorite> productFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFavoriteProductBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_favorite_product);
        viewModel = ViewModelProviders
                .of(this)
                .get(FavoriteProductViewModel.class);
        binding.setLifecycleOwner(this);

        FavoriteProductAdapter adapter = new FavoriteProductAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.recyclerViewFavorite.setLayoutManager(linearLayoutManager);
        binding.recyclerViewFavorite.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect,
                                       @NonNull View view,
                                       @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildAdapterPosition(view) > 0) {
                    outRect.top = 30;
                }
            }
        });
        binding.recyclerViewFavorite.setAdapter(adapter);
        adapter.setOnCheckedFavorite(this);

        viewModel.getFavouriteProducts().observe(this, list -> {
            System.out.println("ProductFavorite list = " + list.size());
            productFavorites = list;
            adapter.setProducts(list);
        });
    }

    @Override
    public void onChecked(boolean isFavoriteMovie, int position) {
        System.out.println("DetailActivity.onClickChangeFavourite " + isFavoriteMovie);

        viewModel.deleteFavouriteProduct(productFavorites.get(position));
    }
}