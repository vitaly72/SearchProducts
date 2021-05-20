package com.project.searchproducts.viewmodels;

import com.project.searchproducts.network.SortType;

public interface ISearchProducts {
    void search(String str, String minPrice, String maxPrice, SortType sort);
    void searchByTag(String tag);
}
