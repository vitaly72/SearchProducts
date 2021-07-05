package com.project.searchproducts.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.project.searchproducts.domain.models.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Helper {
    public static int getIdFromLink(String link) {
        String idStr = link.split("-")[0].replaceAll("[^0-9]", "");

        return Integer.parseInt(idStr);
    }

    public static List<Double> getPriceRange(List<Product> products) {
        List<Double> priceRange = new ArrayList<>();
        List<Double> prices = new ArrayList<>();

        for (Product product : products) {
            prices.add(Double.parseDouble(product.getPrice().replace
                    (" грн.", "")));
        }

        priceRange.add(Collections.min(prices));
        priceRange.add(Collections.max(prices));

        return priceRange;
    }

    public static void makeSnackBar(View view) {
        Snackbar.make(view, "Відсутнє підключення до інтернету", Snackbar.LENGTH_LONG)
                .show();
    }
}
