package com.project.searchproducts.presentation.home;

import com.project.searchproducts.domain.models.SearchData;

public interface ISearchProducts {
    void search(SearchData searchData);

    void searchByTag(String tag);
}
