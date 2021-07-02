package com.project.searchproducts.presentation.home;

import android.view.View;

import com.project.searchproducts.domain.models.Product;

public interface IOnClickListener {
    void onClick(Product product);
    void onClick(Product product, View view);
}