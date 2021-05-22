package com.project.searchproducts.adapters;

import android.view.View;

import com.project.searchproducts.models.Product;

public interface IOnClickListener {
    void onClick(Product product);
    void onClick(Product product, View view);
}