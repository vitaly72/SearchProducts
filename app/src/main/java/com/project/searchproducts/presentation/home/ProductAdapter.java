package com.project.searchproducts.presentation.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.project.searchproducts.presentation.favorite.IOnCheckedFavorite;
import com.project.searchproducts.databinding.ProductItemBinding;
import com.project.searchproducts.domain.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private IOnClickListener onClickListener;
    private IOnCheckedFavorite onCheckedFavorite;
    private List<Product> products;

    public ProductAdapter() {
        products = new ArrayList<>();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ProductItemBinding productItemBinding = ProductItemBinding.inflate(layoutInflater);
        return new ProductViewHolder(productItemBinding.getRoot());
    }

    public void setOnClickListener(IOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        assert holder.productItemBinding != null;
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private final ProductItemBinding productItemBinding;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productItemBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(v -> {
                if (onClickListener != null && productItemBinding != null) {
                    onClickListener.onClick(productItemBinding.getProduct());
                }
            });
        }

        public void bind(Product product) {
            productItemBinding.setProduct(product);
            productItemBinding.executePendingBindings();
            productItemBinding.likeButton.setOnCheckedChangeListener(
                    (v, isChecked) -> onCheckedFavorite.onChecked(isChecked, getAdapterPosition())
            );
        }
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }

    public void addProducts(List<Product> products) {
        this.products.addAll(products);
        notifyDataSetChanged();
    }

    public void clear() {
        this.products.clear();
        notifyDataSetChanged();
    }

    public void setOnCheckedFavorite(IOnCheckedFavorite onCheckedFavorite) {
        this.onCheckedFavorite = onCheckedFavorite;
    }
}
