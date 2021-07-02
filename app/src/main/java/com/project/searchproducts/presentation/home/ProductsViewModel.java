package com.project.searchproducts.presentation.home;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.searchproducts.domain.models.Product;
import com.project.searchproducts.domain.models.ProductDetails;
import com.project.searchproducts.domain.models.SearchData;
import com.project.searchproducts.domain.models.SeoLinks;
import com.project.searchproducts.data.repository.NetworkRepository;
import com.project.searchproducts.utils.Parser;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.project.searchproducts.utils.Parser.parseProducts;

@HiltViewModel
public class ProductsViewModel extends ViewModel implements Observable, ISearchProducts {
    private final NetworkRepository networkRepository;
    private IOnClickListenerTag OnClickListenerTag;
    private MutableLiveData<List<Product>> productsData;
    private MutableLiveData<ProductDetails> productsDetailsData;
    public ObservableField<List<SeoLinks>> seoLinks = new ObservableField<>();

    @Inject
    public ProductsViewModel(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    @Override
    public void search(SearchData searchData) {
        networkRepository.searchProducts(searchData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(value -> productsData.setValue(parseProducts(value)));
    }

    @Override
    public void searchByTag(String tag) {

    }

    public void detailsProduct(String productId) {

    }

    public MutableLiveData<List<Product>> getProductsData() {
        return productsData;
    }

    public MutableLiveData<ProductDetails> getProductsDetailsData() {
        return productsDetailsData;
    }

    public void setOnClickListenerTag(IOnClickListenerTag OnClickListenerTag) {
        this.OnClickListenerTag = OnClickListenerTag;
    }

    public IOnClickListenerTag getOnClickListenerTag() {
        return this.OnClickListenerTag;
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
    }
}
