package com.project.searchproducts.network;

import com.project.searchproducts.models.Product;
import com.project.searchproducts.utils.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class WebParsing {
    public static Observable<List<Product>> getArticles() {
//        WebParsing.getArticles()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<Product>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        System.out.println("SearchActivity.onSubscribe");
//                    }
//
//                    @Override
//                    public void onNext(@NonNull List<Product> products) {
//                        System.out.println("SearchActivity.onNext");
//                        System.out.println(products.size());
//                        searchBinding.backgroundImageView.setVisibility(View.GONE);
//                        searchBinding.progressIndicator.setVisibility(View.GONE);
//                        searchBinding.mainScrollView.setVisibility(View.VISIBLE);
//                        seoLinks = products.get(0).getSeoLinks();
//                        searchBinding.tagsViewGroup.setTags(seoLinks);
//
//                        productAdapter.setProducts(products);
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        System.out.println("SearchActivity.onError");
//                        System.out.println("e = " + e);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        System.out.println("SearchActivity.onComplete");
//                    }
//                });
        return Observable.create(observableEmitter -> {
            List<Product> products;

            NetworkRepository networkRepository = NetworkRepository.getInstance();
            String url = Constants.BASE.URL + "ua/search?search_term=JBL";
            Document doc;
            try {
                doc = Jsoup
                        .connect(url)
                        .userAgent("Chrome/70.0.3538.77")
                        .referrer("http://www.google.com")
                        .get();
                products = networkRepository.parseProducts(doc);
                System.out.println("products = " + products.size());
                System.out.println(doc.body().toString());

                observableEmitter.onNext(products);
            } catch (Exception e) {
                observableEmitter.onError(e);
            } finally {
                observableEmitter.onComplete();
            }
        });
    }
}
