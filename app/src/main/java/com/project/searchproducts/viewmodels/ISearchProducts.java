package com.project.searchproducts.viewmodels;

import com.project.searchproducts.models.SearchData;

public interface ISearchProducts {
    void search(SearchData searchData);

    void searchByTag(String tag);
}
