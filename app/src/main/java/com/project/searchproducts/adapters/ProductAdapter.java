package com.project.searchproducts.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.project.searchproducts.R;
import com.project.searchproducts.databinding.ProductItemBinding;
import com.project.searchproducts.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private IOnClickListener onClickListener;
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
            productItemBinding.likeButton.setOnClickListener(v -> {

            });
        }

        public void bind(Product product) {
            productItemBinding.setProduct(product);
            productItemBinding.executePendingBindings();
        }
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }
//
//    public void addProducts(List<Product> Products) {
//        this.Products.addAll(Products);
//        notifyDataSetChanged();
//    }
//
    public void clear() {
        this.products.clear();
        notifyDataSetChanged();
    }
}
