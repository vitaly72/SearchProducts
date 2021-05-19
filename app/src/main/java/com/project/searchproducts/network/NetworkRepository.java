package com.project.searchproducts.network;

import androidx.lifecycle.MutableLiveData;

import com.project.searchproducts.models.Product;
import com.project.searchproducts.models.ProductDetails;
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
                            Elements characteristicName = document.getElementsByClass(CLASSES.CHARACTERISTIC_NAME);
                            Elements characteristicValue = document.getElementsByClass(CLASSES.CHARACTERISTIC_VALUE);
                            Elements descriptions = document.getElementsByAttributeValue(KEYS.DATA_QAID, CLASSES.DESCRIPTIONS);
                            String divClass = "ek-box ek-box_padding-bottom_s ek-box_margin-bottom_s ek-box_border-bottom_black-40";
//                            Elements div = document.select("div" + divClass + "::after");
//                            System.out.println(div.first().toString());

                            System.out.println("imageLinks.size() = " + imageLinks.size());
                            List<String> links = new ArrayList<>();
                            HashMap<String, String> characteristic = new HashMap<>();

                            System.out.println("characteristicName = " + characteristicName.size());
                            System.out.println("characteristicValue = " + characteristicValue.size());

                            List<TextNode> textNodes = descriptions.get(0).textNodes();

                            for (TextNode node : textNodes) {
                                System.out.println("node.text() = " + node.text());
                            }


                            for (Element element : imageLinks) {
                                links.add(element.attr("src"));
//                                System.out.println("link: " + element.attr("src"));
                            }

                            for (int i = 0; i < characteristicName.size(); i++) {
//                                System.out.println("characteristicName.get(i) = " + characteristicName.get(i).text());
//                                System.out.println("characteristicName.get(i) = " + characteristicValue.get(i).text());
                                characteristic.put(
                                        characteristicName.get(i).text(),
                                        characteristicValue.get(i).text());
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

    public List<Product> parseProducts(Document document) {
        List<Product> products = new ArrayList<>();
        List<SeoLinks> seoLinks = new ArrayList<>();

        Elements links = document.getElementsByAttributeValue(KEYS.DATA_QAID, VALUES.PRODUCT_LINK);
//        Elements titles = document.getElementsByClass(CLASSES.TITLES);
        Elements prices = document.getElementsByAttributeValue(KEYS.DATA_QAID, VALUES.PRODUCT_PRICE);
        Elements presences = document.getElementsByAttributeValue(KEYS.DATA_QAID, VALUES.PRODUCT_PRESENCE);
        Elements images = document.getElementsByClass(CLASSES.IMAGES);
        Elements tags = document.getElementsByAttributeValue(KEYS.DATA_QAID, VALUES.SEO_LINKS);

//        System.out.println("titles = " + titles.size());
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
                        prices.get(i).attr("data-qaprice") + " грн.",
                        0,
                        presences.get(i).text(),
                        links.get(i).attr("href"),
                        seoLinks);
            } catch (Exception e) {
                continue;
            }
            products.add(product);
        }
        System.out.println("NetworkRepository.parseProducts");
        System.out.println("products = " + products.size());

        return products;
    }
}
