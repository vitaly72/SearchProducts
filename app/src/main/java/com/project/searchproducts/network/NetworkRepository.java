package com.project.searchproducts.network;

import android.content.res.Resources;

import androidx.lifecycle.MutableLiveData;

import com.project.searchproducts.R;
import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.ProductDetails;
import com.project.searchproducts.models.SearchData;
import com.project.searchproducts.models.SeoLinks;
import com.project.searchproducts.utils.Constants.ATTRIBUTES.KEYS;
import com.project.searchproducts.utils.Constants.ATTRIBUTES.VALUES;
import com.project.searchproducts.utils.Constants.CLASSES;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkRepository {
    private static NetworkRepository instance;

    private NetworkRepository() {
    }

    /**
     * Сторює або повертає об'єкт класу
     * @return
     */
    public static NetworkRepository getInstance() {
        if (instance == null) instance = new NetworkRepository();
        return instance;
    }

    /**
     * Виконує запит для пошуку
     * @param searchData
     * @return
     */
    public MutableLiveData<List<Product>> searchProducts(SearchData searchData) {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();
        NetworkService.createService()
                .search(searchData.getSearchTerm(),
                        searchData.getMinPrice(),
                        searchData.getMaxPrice(),
                        searchData.getSort(),
                        searchData.getPage())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NotNull Call<String> call,
                                           @NotNull Response<String> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            data.setValue(parseProducts(response.body()));
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

    /**
     * Виконує запит для пошуку по тегам
     * @param tag
     * @return
     */
    public MutableLiveData<List<Product>> searchProductByTag(String tag) {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();
        String tagLink = tag.replace("/ua/", "");

        NetworkService.createService()
                .searchByTag(tagLink)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NotNull Call<String> call,
                                           @NotNull Response<String> response) {
                        System.out.println("response tag = " + response);
                        if (response.isSuccessful() && response.body() != null) {
                            data.setValue(parseProducts(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call,
                                          @NotNull Throwable throwable) {
                        throwable.printStackTrace();
                        data.setValue(null);
                    }
                });

        return data;
    }

    /**
     * Виконує запит для детальної інформації товару
     * @param productId
     * @return
     */
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
                            Elements imageLinks = document.getElementsByAttributeValue(KEYS.DATA_QAID, VALUES.IMAGES_LINKS);
                            Elements descriptions = document.getElementsByAttributeValue(KEYS.DATA_QAID, CLASSES.DESCRIPTIONS);

                            List<String> links = new ArrayList<>();
                            HashMap<String, String> characteristic = new HashMap<>();

                            for (Element element : imageLinks) {
                                links.add(element.attr("src"));
                            }

                            ProductDetails productDetails = new ProductDetails(product, links, characteristic, descriptions.text());
                            data.setValue(productDetails);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<String> call, @NotNull Throwable throwable) {
                        data.setValue(null);
                    }
                });

        return data;
    }

    public List<Product> parseProducts(String response) {
        Document document = Jsoup.parse(response);

        return parseProducts(document);
    }

    /**
     * Парсинг результатів запиту
     * @param document
     * @return
     */
    public List<Product> parseProducts(Document document) {
        List<Product> products = new ArrayList<>();
        List<SeoLinks> seoLinks = new ArrayList<>();

        Elements links = document.getElementsByAttributeValue(KEYS.DATA_QAID, VALUES.PRODUCT_LINK);
        Elements prices = document.getElementsByAttributeValue(KEYS.DATA_QAID, VALUES.PRODUCT_PRICE);
        Elements presences = document.getElementsByAttributeValue(KEYS.DATA_QAID, VALUES.PRODUCT_PRESENCE);
        Elements images = document.getElementsByClass(CLASSES.IMAGES);
        Elements tags = document.getElementsByAttributeValue(KEYS.DATA_QAID, VALUES.SEO_LINKS);

        System.out.println("prices = " + prices.size());
        System.out.println("presences = " + presences.size());
        System.out.println("images = " + images.size());
        System.out.println("links = " + links.size());
        System.out.println("tags = " + tags.size());

        for (Element item : tags) {
            System.out.println("tags: " + item.text() + " " + item.attr("href"));
            seoLinks.add(new SeoLinks(item.text(), item.attr("href")));
        }

        for (int i = 0; i < links.size(); i++) {
            Product product;
            try {
                product = new Product(links.get(i).attr("title"),
                        images.get(i).absUrl("src"),
                        prices.get(i).attr("data-qaprice") + Resources.getSystem().getString(R.string.grn),
                        0,
                        presences.get(i).text(),
                        links.get(i).attr("href"),
                        seoLinks);
            } catch (Exception e) {
                continue;
            }
            products.add(product);
        }
        System.out.println("products = " + products.size());

        return products;
    }
}