package com.project.searchproducts.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.project.searchproducts.databinding.FavoriteProductItemBinding;
import com.project.searchproducts.databinding.ProductItemBinding;
import com.project.searchproducts.models.ProductFavorite;

import java.util.ArrayList;
import java.util.List;

public class FavoriteProductAdapter extends RecyclerView.Adapter<FavoriteProductAdapter.ProductViewHolder> {
    private List<ProductFavorite> products;
    private IOnCheckedFavorite onCheckedFavorite;

    public FavoriteProductAdapter() {
        products = new ArrayList<>();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FavoriteProductItemBinding productItemBinding = FavoriteProductItemBinding.inflate(layoutInflater);
        return new ProductViewHolder(productItemBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        assert holder.itemBinding != null;
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    public List<ProductFavorite> getProducts() {
        return products;
    }

    public void setProducts(List<ProductFavorite> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private final FavoriteProductItemBinding itemBinding;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(ProductFavorite product) {
            itemBinding.setProduct(product);
            itemBinding.executePendingBindings();
            itemBinding.likeButtonFavorite.setOnCheckedChangeListener(
                    (buttonView, isChecked) -> onCheckedFavorite.onChecked(isChecked, getAdapterPosition())
            );
        }

    }

    public void clear() {
        this.products.clear();
        notifyDataSetChanged();
    }

    public void setOnCheckedFavorite(IOnCheckedFavorite onCheckedFavorite) {
        this.onCheckedFavorite = onCheckedFavorite;
    }
}