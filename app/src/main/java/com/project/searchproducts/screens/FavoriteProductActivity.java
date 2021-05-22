package com.project.searchproducts.screens;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.narify.netdetect.NetDetect;
import com.project.searchproducts.R;
import com.project.searchproducts.adapters.FavoriteProductAdapter;
import com.project.searchproducts.adapters.IOnCheckedFavorite;
import com.project.searchproducts.adapters.IOnClickListener;
import com.project.searchproducts.databinding.ActivityFavoriteProductBinding;
import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.ProductFavorite;
import com.project.searchproducts.utils.Constants;
import com.project.searchproducts.utils.JSONUtils;
import com.project.searchproducts.viewmodels.FavoriteProductViewModel;

import java.util.List;

import static com.project.searchproducts.utils.Helper.makeSnackBar;

public class FavoriteProductActivity extends AppCompatActivity implements
        IOnCheckedFavorite,
        IOnClickListener {
    private FavoriteProductViewModel viewModel;
    private List<ProductFavorite> productFavorites;

    /**
     * Створює об'єкт FavoriteProductViewModel для завантаження улюьлених товарів з бази даних.
     * Ініціалізує список улюблених товарів.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFavoriteProductBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_favorite_product);
        viewModel = ViewModelProviders
                .of(this)
                .get(FavoriteProductViewModel.class);
        binding.setLifecycleOwner(this);
        NetDetect.init(this);

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
        adapter.setOnClickListener(this);

        viewModel.getFavouriteProducts().observe(this, list -> {
            System.out.println("ProductFavorite list = " + list.size());
            productFavorites = list;
            adapter.setProducts(list);
        });
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
                Intent intent = new Intent(FavoriteProductActivity.this, DetailsActivity.class);
                String movieJsonString = JSONUtils.getGsonParser().toJson(product);
                intent.putExtra(Constants.INTENT_KEY, movieJsonString);
                startActivity(intent);
            }
        }));
    }
}