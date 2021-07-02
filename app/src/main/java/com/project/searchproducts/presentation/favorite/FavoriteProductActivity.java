package com.project.searchproducts.presentation.favorite;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.narify.netdetect.NetDetect;
import com.project.searchproducts.R;
import com.project.searchproducts.presentation.home.IOnClickListener;
import com.project.searchproducts.databinding.ActivityFavoriteProductBinding;
import com.project.searchproducts.domain.models.Product;
import com.project.searchproducts.domain.models.ProductFavorite;
import com.project.searchproducts.presentation.details.DetailsActivity;
import com.project.searchproducts.utils.Constants;
import com.project.searchproducts.utils.JSONUtils;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

import static com.project.searchproducts.utils.Helper.makeSnackBar;

@AndroidEntryPoint
public class FavoriteProductActivity extends AppCompatActivity implements
        IOnCheckedFavorite,
        IOnClickListener {
    private ActivityFavoriteProductBinding binding;
    private FavoriteProductAdapter adapter;
    private FavoriteProductViewModel viewModel;
    private List<ProductFavorite> productFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite_product);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(FavoriteProductViewModel.class);

        NetDetect.init(this);
        initRecyclerView();

        adapter.setOnCheckedFavorite(this);
        adapter.setOnClickListener(this);

        viewModel.getFavouriteProducts().observe(this, list -> {
            productFavorites = list;
            adapter.setProducts(list);
        });
    }

    private void initRecyclerView() {
        adapter = new FavoriteProductAdapter();
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
    }

    @Override
    public void onChecked(boolean isFavoriteMovie, int position) {
        viewModel.deleteFavouriteProduct(productFavorites.get(position));
    }

    @Override
    public void onClick(Product product) {
    }

    @Override
    public void onClick(Product product, View view) {
        NetDetect.check((isConnected -> {
            if (!isConnected) {
                makeSnackBar(view);
            } else {
                System.out.println("FavoriteProductActivity.onClick");
                Intent intent = new Intent(FavoriteProductActivity.this, DetailsActivity.class);
                String movieJsonString = JSONUtils.getGsonParser().toJson(product);
                intent.putExtra(Constants.INTENT_KEY, movieJsonString);
                startActivity(intent);
            }
        }));
    }
}