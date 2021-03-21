package com.project.searchproducts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.searchproducts.R;
import com.project.searchproducts.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private OnPosterClickListener onPosterClickListener;
    Context context;

    List<Product> products;

    public ProductAdapter(Context context, List<Product> ProductList) {
        this.context = context;
        this.products = ProductList;
    }

    public ProductAdapter() {
        products = new ArrayList<>();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    public interface OnPosterClickListener {

        void onPosterClick(int position);
    }

    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.textViewTitle.setText(products.get(position).getTitle());
        holder.textViewPrice.setText(products.get(position).getPrice());
        holder.textViewPresence.setText(products.get(position).getPresence());
        Picasso.get().load(products.get(position).getImageUrl()).into(holder.imageViewSmallPoster);
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewSmallPoster;
        private final TextView textViewTitle;
        private final TextView textViewPrice;
        private final TextView textViewPresence;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewSmallPoster = itemView.findViewById(R.id.imageViewProduct);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewPresence = itemView.findViewById(R.id.textViewPresence);
            itemView.setOnClickListener(v -> {
                if (onPosterClickListener != null) {
                    onPosterClickListener.onPosterClick(getAdapterPosition());
                }
            });
        }

    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }
//    public void setProducts(List<Product> Products) {
//        this.Products = Products;
//        notifyDataSetChanged();
//    }
//
//    public void addProducts(List<Product> Products) {
//        this.Products.addAll(Products);
//        notifyDataSetChanged();
//    }
//
//    public List<Product> getProducts() {
//        return Products;
//    }
//
//    public void clear() {
//        this.Products.clear();
//        notifyDataSetChanged();
//    }
}
