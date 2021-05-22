package com.project.searchproducts.utils;

import android.content.res.Resources;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.project.searchproducts.R;
import com.project.searchproducts.models.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Helper {
    /**
     * Повертає ідекст товару з його детальної інформації
     * @param link
     * @return
     */
    public static int getIdFromLink(String link) {
        String idStr = link.split("-")[0].replaceAll("[^0-9]", "");

        return Integer.parseInt(idStr);
    }

    /**
     * Повертає діапазон цін
     * @param products
     * @return
     */
    public static List<Double> getPriceRange(List<Product> products) {
        List<Double> priceRange = new ArrayList<>();
        List<Double> prices = new ArrayList<>();

        for (Product product : products) {
            prices.add(Double.parseDouble(product.getPrice().replace
                    (Resources.getSystem().getString(R.string.grn), "")));
        }

        priceRange.add(Collections.min(prices));
        priceRange.add(Collections.max(prices));

        return priceRange;
    }

    /**
     * Виводить повідомлення про відсутність підключення до інтернету
     * @param view
     */
    public static void makeSnackBar(View view) {
        Snackbar.make(view, Resources.getSystem().getString(R.string.noConnect), Snackbar.LENGTH_LONG)
                .show();
    }
}