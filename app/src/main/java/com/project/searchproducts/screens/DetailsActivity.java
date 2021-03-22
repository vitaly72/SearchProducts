package com.project.searchproducts.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.project.searchproducts.R;
import com.project.searchproducts.adapters.SliderAdapter;
import com.project.searchproducts.adapters.SliderItem;
import com.project.searchproducts.adapters.ViewPagerAdapter;
import com.project.searchproducts.databinding.ActivityProductBinding;
import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.ProductDetails;
import com.project.searchproducts.utils.JSONUtils;
import com.project.searchproducts.viewmodels.ProductsViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    SliderView sliderView;
    private SliderAdapter adapter = new SliderAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProductBinding productBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_product);
        ProductsViewModel productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);

        Intent intent = getIntent();
        String movieJsonString = intent.getStringExtra("product");
        Product product = JSONUtils.getGsonParser().fromJson(movieJsonString, Product.class);

        productBinding.textView.setText(product.getTitle());

        productsViewModel.detailsProduct(product.getDetailsLink());
        productsViewModel.getProductsDetailsData().observe(this, response -> {
            if (response != null) {
                System.out.println("response.getImageLinks().size() = " + response.getImageLinks().size());

//                if (response.getImageLinks() != null) {
//                    for (int i = 0; i < response.getImageLinks().size(); i++) {
//                        System.out.println("link = " + response.getImageLinks().get(i));
//
//                        SliderItem sliderItem = new SliderItem();
//                        sliderItem.setImageUrl(response.getImageLinks().get(i));
//                        sliderItem.setDescription("");
//
//                        adapter.addItem(sliderItem);
//                    }
//                }

                ViewPagerAdapter adapter = new ViewPagerAdapter(this, response.getImageLinks());
                productBinding.viewPager.setAdapter(adapter);
            }
        });
//
//        productBinding.imageSlider.setSliderAdapter(adapter);
//        productBinding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
//        productBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//        productBinding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//        productBinding.imageSlider.startAutoCycle();
    }
}