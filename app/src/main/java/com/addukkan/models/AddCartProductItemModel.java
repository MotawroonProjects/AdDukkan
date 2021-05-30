package com.addukkan.models;

import java.io.Serializable;

public class AddCartProductItemModel implements Serializable {
    private String product_id;
    private String product_price_id;
    private String vendor_id;
    private double price;
    private int amount;
    private String have_offer;
    private String offer_type;
    private double offer_value;
    private double offer_min;
    private double offer_bonus;
    private double old_price;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_price_id() {
        return product_price_id;
    }

    public void setProduct_price_id(String product_price_id) {
        this.product_price_id = product_price_id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getHave_offer() {
        return have_offer;
    }

    public void setHave_offer(String have_offer) {
        this.have_offer = have_offer;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public void setOffer_type(String offer_type) {
        this.offer_type = offer_type;
    }

    public double getOffer_value() {
        return offer_value;
    }

    public void setOffer_value(double offer_value) {
        this.offer_value = offer_value;
    }

    public double getOffer_min() {
        return offer_min;
    }

    public void setOffer_min(double offer_min) {
        this.offer_min = offer_min;
    }

    public double getOffer_bonus() {
        return offer_bonus;
    }

    public void setOffer_bonus(double offer_bonus) {
        this.offer_bonus = offer_bonus;
    }

    public double getOld_price() {
        return old_price;
    }

    public void setOld_price(double old_price) {
        this.old_price = old_price;
    }
}
