package com.project.searchproducts.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.ProductDetails;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class NetworkRepository {
    private static NetworkRepository instance;
    private LiveData<List<Product>> productList;

    private NetworkRepository() {
    }

    public static NetworkRepository getInstance() {
        if (instance == null) instance = new NetworkRepository();
        return instance;
    }

    public MutableLiveData<List<Product>> searchProducts(String searchTerm) {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();

        NetworkService.createService()
                .search(searchTerm)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NotNull Call<String> call,
                                           @NotNull Response<String> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Product> products = new ArrayList<>();
                            Document document = Jsoup.parse(response.body());
                            Elements titles = document.getElementsByClass("ek-text ek-text_color_black-800 ek-text_wrap_two-line");
                            Elements links = document.getElementsByClass("productTile__tileLink--204An");
                            Elements prices = document.getElementsByAttributeValue("data-qaid", "product_price");
                            Elements presences = document.getElementsByAttributeValue("data-qaid", "product_presence");
                            Elements images = document.getElementsByClass("lazyImage__image--APkHl");

                            System.out.println("titles = " + titles.size());
                            System.out.println("prices = " + prices.size());
                            System.out.println("presences = " + presences.size());
                            System.out.println("images = " + images.size());

                            for (int i = 0; i < titles.size(); i++) {
//                                System.out.println("title = " + title);
//                                System.out.println("link = " + Constants.BASE.URL + links.get(i).attr("href").replace("/ua/", ""));
                                Product product = new Product(titles.get(i).text(),
                                        images.get(i).absUrl("src"),
                                        prices.get(i).attr("data-qaprice") + " грн.",
                                        0,
                                        presences.get(i).text(),
                                        links.get(i).attr("href"));
                                products.add(product);
                            }
                            System.out.println("products = " + products.size());
                            data.setValue(products);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call,
                                          @NotNull Throwable throwable) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public MutableLiveData<ProductDetails> detailsProduct(String productId) {
        MutableLiveData<ProductDetails> data = new MutableLiveData<>();
        String product = productId.replace("/ua/", "").split("\\?token")[0];
        System.out.println("productID = " + product);
        NetworkService.createService()
                .details(product)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Document document = Jsoup.parse(response.body());
                            Elements imageLinks = document.getElementsByAttributeValue("data-qaid", "image_thumb");
                            System.out.println("imageLinks.size() = " + imageLinks.size());
                            List<String> links = new ArrayList<>();
                            for (Element element : imageLinks) {
                                links.add(element.attr("src"));
                                System.out.println("link: " + element.attr("src"));
                            }
                            data.setValue(new ProductDetails(product, links));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call, @NotNull Throwable throwable) {
                        data.setValue(null);
                    }
                });

        return data;
    }

    public LiveData<List<Product>> getProductList() {
        return productList;
    }
}
