package com.project.searchproducts.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.project.searchproducts.R;
import com.project.searchproducts.adapters.ViewPagerAdapter;
import com.project.searchproducts.databinding.ActivityProductBinding;
import com.project.searchproducts.models.Product;
import com.project.searchproducts.utils.JSONUtils;
import com.project.searchproducts.viewmodels.ProductsViewModel;

public class DetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProductBinding productBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_product);
        ProductsViewModel productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);

        Intent intent = getIntent();
        String movieJsonString = intent.getStringExtra("product");
        Product product = JSONUtils.getGsonParser().fromJson(movieJsonString, Product.class);

        productBinding.textViewName.setText(product.getTitle());
        productBinding.textViewPrice.setText(product.getPrice());

        productsViewModel.detailsProduct(product.getDetailsLink());
        productsViewModel.getProductsDetailsData().observe(this, response -> {
            if (response != null) {
                System.out.println("response.getImageLinks().size() = " + response.getImageLinks().size());
                ViewPagerAdapter adapter = new ViewPagerAdapter(this, response.getImageLinks());
                productBinding.viewPager.setAdapter(adapter);

                String characteristics = "";
                for (String key : response.getCharacteristic().keySet()) {
                    characteristics += key + "\t" + response.getCharacteristic().get(key) + "\n";
                    System.out.println(key);
                }
                productBinding.textViewCharacteristics.setText(characteristics);
            }
        });
    }
}