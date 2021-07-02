package com.project.searchproducts.utils;

import com.project.searchproducts.domain.models.Product;
import com.project.searchproducts.domain.models.ProductDetails;
import com.project.searchproducts.domain.models.SeoLinks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Parser {
    public static List<Product> parseProducts(String response) {
        Document document = Jsoup.parse(response);

        return parseProducts(document);
    }

    public static ProductDetails parseDetails(String response, String link) {
        Document document = Jsoup.parse(response);

        return parseDetails(document, link);
    }

    public static List<Product> parseProducts(Document document) {
        List<Product> products = new ArrayList<>();
        List<SeoLinks> seoLinks = new ArrayList<>();

        Elements links = document.getElementsByAttributeValue(
                Constants.ATTRIBUTES.KEYS.DATA_QAID,
                Constants.ATTRIBUTES.VALUES.PRODUCT_LINK);

        Elements prices = document.getElementsByAttributeValue(
                Constants.ATTRIBUTES.KEYS.DATA_QAID,
                Constants.ATTRIBUTES.VALUES.PRODUCT_PRICE);

        Elements presences = document.getElementsByAttributeValue(
                Constants.ATTRIBUTES.KEYS.DATA_QAID,
                Constants.ATTRIBUTES.VALUES.PRODUCT_PRESENCE);

        Elements images = document.getElementsByClass(Constants.CLASSES.IMAGES);

        Elements tags = document.getElementsByAttributeValue(
                Constants.ATTRIBUTES.KEYS.DATA_QAID,
                Constants.ATTRIBUTES.VALUES.SEO_LINKS);

        for (Element item : tags) {
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
        System.out.println("products = " + products.size());

        return products;
    }

    public static ProductDetails parseDetails(Document document, String link) {
        String productLink = link.replace("/ua/", "").split("\\?token")[0];

        Elements imageLinks = document.getElementsByAttributeValue(
                Constants.ATTRIBUTES.KEYS.DATA_QAID,
                Constants.ATTRIBUTES.VALUES.IMAGES_LINKS);
        Elements descriptions = document.getElementsByAttributeValue(
                Constants.ATTRIBUTES.KEYS.DATA_QAID,
                Constants.CLASSES.DESCRIPTIONS);

        List<String> links = new ArrayList<>();
        HashMap<String, String> characteristic = new HashMap<>();

        for (Element element : imageLinks) {
            links.add(element.attr("src"));
        }

        return new ProductDetails(productLink, links, characteristic, descriptions.text());
    }
}
