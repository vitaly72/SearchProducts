package com.project.searchproducts.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.project.searchproducts.R;
import com.project.searchproducts.databinding.ActivityMainBinding;
import com.project.searchproducts.models.Product;
import com.project.searchproducts.utils.JSONUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_main);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product")) {
            String movieJsonString = intent.getStringExtra("product");
            Product product = JSONUtils.getGsonParser().fromJson(movieJsonString, Product.class);
            mainBinding.textView.setText(product.getTitle());
        } else finish();
    }
}