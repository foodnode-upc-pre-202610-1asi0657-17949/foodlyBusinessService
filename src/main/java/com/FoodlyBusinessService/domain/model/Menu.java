package com.FoodlyBusinessService.domain.model;

import org.springframework.data.mongodb.core.mapping.Field;
import java.util.ArrayList;
import java.util.List;

public class Menu {

    @Field("categories")
    private List<String> categories;

    @Field("products")
    private List<Product> products;

    public Menu() {
        this.categories = new ArrayList<>();
        this.products = new ArrayList<>();
    }

    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
