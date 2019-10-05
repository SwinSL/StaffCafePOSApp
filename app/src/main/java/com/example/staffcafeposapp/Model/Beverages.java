package com.example.staffcafeposapp.Model;


public class Beverages {

    private String beverages_name;
    private int beverages_price;

    public Beverages(String beverages_name, int beverages_price) {
        this.beverages_name = beverages_name;
        this.beverages_price = beverages_price;
    }

    public String getBeverages_name() {
        return beverages_name;
    }

    public int getBeverages_price() {
        return beverages_price;
    }
}
