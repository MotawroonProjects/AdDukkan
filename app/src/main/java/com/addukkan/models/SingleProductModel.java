package com.addukkan.models;

import java.io.Serializable;

public class SingleProductModel implements Serializable {
    private int id;
    private int vendor_id;
    private String brand_id;
    private int product_company_id;
    private String main_image;
    private double rate;
    private double total_stock;
    private double sell_count;
    private double view_count;
    private String have_offer;
    private String offer_type;
    private int offer_value;
    private int offer_min;
    private int offer_bonus;
    private ProductTransFk product_trans_fk;
    private ProductDefaultPrice product_default_price;
    private Favourite favourite;

    public int getId() {
        return id;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public int getProduct_company_id() {
        return product_company_id;
    }

    public String getMain_image() {
        return main_image;
    }

    public double getRate() {
        return rate;
    }

    public double getTotal_stock() {
        return total_stock;
    }

    public double getSell_count() {
        return sell_count;
    }

    public double getView_count() {
        return view_count;
    }

    public String getHave_offer() {
        return have_offer;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public int getOffer_value() {
        return offer_value;
    }

    public int getOffer_min() {
        return offer_min;
    }

    public int getOffer_bonus() {
        return offer_bonus;
    }

    public ProductTransFk getProduct_trans_fk() {
        return product_trans_fk;
    }

    public ProductDefaultPrice getProduct_default_price() {
        return product_default_price;
    }

    public Favourite getFavourite() {
        return favourite;
    }

    public class ProductTransFk implements Serializable {
        private int id;
        private int product_id;
        private String title;
        private String description;
        private String details;
        private String lang;

        public int getId() {
            return id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getDetails() {
            return details;
        }

        public String getLang() {
            return lang;
        }
    }

    public class ProductDefaultPrice implements Serializable {
        private int id;
        private int product_id;
        private int product_set_id;
        private int vendor_id;
        private double price;
        private int stock;
        private String is_default;
        private String country_code;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public int getProduct_set_id() {
            return product_set_id;
        }

        public int getVendor_id() {
            return vendor_id;
        }

        public double getPrice() {
            return price;
        }

        public int getStock() {
            return stock;
        }

        public String getIs_default() {
            return is_default;
        }

        public String getCountry_code() {
            return country_code;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }

    public class Favourite implements Serializable {
        private int id;
        private int product_id;
        private int user_id;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
