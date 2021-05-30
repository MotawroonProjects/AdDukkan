package com.addukkan.models;

import java.io.Serializable;
import java.util.List;

public class CartDataModel extends ResponseModel implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data implements Serializable{
        private int id;
        public String prescription_id;
        public int user_id;
        public double total_price;
        private List<Detials> detials;

        public int getId() {
            return id;
        }

        public String getPrescription_id() {
            return prescription_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public double getTotal_price() {
            return total_price;
        }

        public List<Detials> getDetials() {
            return detials;
        }

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
