package com.allandroidprojects.payeat;

/**
 * Created by thero on 3/21/2018.
 */


public class Product {
    private String id;
    private String title;
    private String quantity;
    private String price;


    public Product(String id, String title, String quantity,  String price) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getQuantity() {
        return quantity;
    }


    public String getPrice() {
        return price;
    }


}