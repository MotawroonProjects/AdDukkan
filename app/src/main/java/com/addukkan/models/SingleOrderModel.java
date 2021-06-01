package com.addukkan.models;

import java.io.Serializable;
import java.util.List;

public class SingleOrderModel extends ResponseModel implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data implements Serializable{
        private int id;
        private int user_id;
        private String name;
        private String email;
        private String phone;
        private String phone_code;
        private String country_code;
        private String address;
        private double address_lat;
        private double address_long;
        private String city;
        private String notes;
        private double subtotal;
        private double shipping;
        private double total_payments;
        private String payment_method;
        private String payment_status;
        private String form_type;
        private String coupon_id;
        private String status;
        private String start_shipping_date;
        private String end_shipping_date;
        private String created_at;
        private String updated_at;
        private List<Detials> order_details;

        public class Detials implements Serializable{
            public int id;
            public int cart_id;
            public int product_id;
            public int product_price_id;
            public int vendor_id;
            public double price;
            public double amount;
            public String have_offer;
            public String offer_type;
            public double offer_value;
            public int offer_min;
            public int offer_bonus;
            public double old_price;
            private SingleProductModel product_data;

            public int getId() {
                return id;
            }

            public int getCart_id() {
                return cart_id;
            }

            public int getProduct_id() {
                return product_id;
            }

            public int getProduct_price_id() {
                return product_price_id;
            }

            public int getVendor_id() {
                return vendor_id;
            }

            public double getPrice() {
                return price;
            }

            public double getAmount() {
                return amount;
            }

            public String getHave_offer() {
                return have_offer;
            }

            public String getOffer_type() {
                return offer_type;
            }

            public double getOffer_value() {
                return offer_value;
            }

            public int getOffer_min() {
                return offer_min;
            }

            public int getOffer_bonus() {
                return offer_bonus;
            }

            public double getOld_price() {
                return old_price;
            }

            public SingleProductModel getProduct_data() {
                return product_data;
            }
        }
    }
}
