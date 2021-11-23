package com.addukkanapp.models;

import java.util.List;

public class ScanCart {
    public Data data;
    public String message;
    public int status;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class Data {
        public int id;
        public int prescription_id;
        public String user_id;
        public String session_id;
        public double total_price;
        public String country_code;
        public List<Detail> details;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPrescription_id() {
            return prescription_id;
        }

        public void setPrescription_id(int prescription_id) {
            this.prescription_id = prescription_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }

        public double getTotal_price() {
            return total_price;
        }

        public void setTotal_price(double total_price) {
            this.total_price = total_price;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public List<Detail> getDetails() {
            return details;
        }

        public void setDetails(List<Detail> details) {
            this.details = details;
        }

        public class Detail {
            public int id;
            public int cart_id;
            public String user_id;
            public String session_id;
            public int product_id;
            public int product_price_id;
            public int vendor_id;
            public double price;
            public int amount;
            public String have_offer;
            public String offer_type;
            public int offer_value;
            public int offer_min;
            public int offer_bonus;
            public double old_price;
            public String desc_ar;
            public String desc_en;
            public String description;
            public ProductData product_data;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCart_id() {
                return cart_id;
            }

            public void setCart_id(int cart_id) {
                this.cart_id = cart_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getSession_id() {
                return session_id;
            }

            public void setSession_id(String session_id) {
                this.session_id = session_id;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public int getProduct_price_id() {
                return product_price_id;
            }

            public void setProduct_price_id(int product_price_id) {
                this.product_price_id = product_price_id;
            }

            public int getVendor_id() {
                return vendor_id;
            }

            public void setVendor_id(int vendor_id) {
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

            public int getOffer_value() {
                return offer_value;
            }

            public void setOffer_value(int offer_value) {
                this.offer_value = offer_value;
            }

            public int getOffer_min() {
                return offer_min;
            }

            public void setOffer_min(int offer_min) {
                this.offer_min = offer_min;
            }

            public int getOffer_bonus() {
                return offer_bonus;
            }

            public void setOffer_bonus(int offer_bonus) {
                this.offer_bonus = offer_bonus;
            }

            public double getOld_price() {
                return old_price;
            }

            public void setOld_price(double old_price) {
                this.old_price = old_price;
            }

            public String getDesc_ar() {
                return desc_ar;
            }

            public void setDesc_ar(String desc_ar) {
                this.desc_ar = desc_ar;
            }

            public String getDesc_en() {
                return desc_en;
            }

            public void setDesc_en(String desc_en) {
                this.desc_en = desc_en;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public ProductData getProduct_data() {
                return product_data;
            }

            public void setProduct_data(ProductData product_data) {
                this.product_data = product_data;
            }

            public class ProductData {
                public int id;
                public String product_code;
                public int vendor_id;
                public int brand_id;
                public int product_company_id;
                public String main_image;
                public String product_type;
                public String have_attribute;
                public int rate;
                public int total_stock;
                public int sell_count;
                public int view_count;
                public String have_offer;
                public String offer_type;
                public int offer_value;
                public int offer_min;
                public int offer_bonus;
                public String is_published;
                public double old_price;
                public double price;
                public ProductTransFk product_trans_fk;
                public ProductDefaultPrice product_default_price;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getProduct_code() {
                    return product_code;
                }

                public void setProduct_code(String product_code) {
                    this.product_code = product_code;
                }

                public int getVendor_id() {
                    return vendor_id;
                }

                public void setVendor_id(int vendor_id) {
                    this.vendor_id = vendor_id;
                }

                public int getBrand_id() {
                    return brand_id;
                }

                public void setBrand_id(int brand_id) {
                    this.brand_id = brand_id;
                }

                public int getProduct_company_id() {
                    return product_company_id;
                }

                public void setProduct_company_id(int product_company_id) {
                    this.product_company_id = product_company_id;
                }

                public String getMain_image() {
                    return main_image;
                }

                public void setMain_image(String main_image) {
                    this.main_image = main_image;
                }

                public String getProduct_type() {
                    return product_type;
                }

                public void setProduct_type(String product_type) {
                    this.product_type = product_type;
                }

                public String getHave_attribute() {
                    return have_attribute;
                }

                public void setHave_attribute(String have_attribute) {
                    this.have_attribute = have_attribute;
                }

                public int getRate() {
                    return rate;
                }

                public void setRate(int rate) {
                    this.rate = rate;
                }

                public int getTotal_stock() {
                    return total_stock;
                }

                public void setTotal_stock(int total_stock) {
                    this.total_stock = total_stock;
                }

                public int getSell_count() {
                    return sell_count;
                }

                public void setSell_count(int sell_count) {
                    this.sell_count = sell_count;
                }

                public int getView_count() {
                    return view_count;
                }

                public void setView_count(int view_count) {
                    this.view_count = view_count;
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

                public int getOffer_value() {
                    return offer_value;
                }

                public void setOffer_value(int offer_value) {
                    this.offer_value = offer_value;
                }

                public int getOffer_min() {
                    return offer_min;
                }

                public void setOffer_min(int offer_min) {
                    this.offer_min = offer_min;
                }

                public int getOffer_bonus() {
                    return offer_bonus;
                }

                public void setOffer_bonus(int offer_bonus) {
                    this.offer_bonus = offer_bonus;
                }

                public String getIs_published() {
                    return is_published;
                }

                public void setIs_published(String is_published) {
                    this.is_published = is_published;
                }

                public double getOld_price() {
                    return old_price;
                }

                public void setOld_price(double old_price) {
                    this.old_price = old_price;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public ProductTransFk getProduct_trans_fk() {
                    return product_trans_fk;
                }

                public void setProduct_trans_fk(ProductTransFk product_trans_fk) {
                    this.product_trans_fk = product_trans_fk;
                }

                public ProductDefaultPrice getProduct_default_price() {
                    return product_default_price;
                }

                public void setProduct_default_price(ProductDefaultPrice product_default_price) {
                    this.product_default_price = product_default_price;
                }

                public class ProductDefaultPrice {
                    public int id;
                    public int product_id;
                    public String parent_id;
                    public int vendor_id;
                    public int level;
                    public int attribute_id;
                    public double price;
                    public int stock;
                    public String is_default;
                    public String is_default_price;
                    public String country_code;
                    public String image;
                    public String title;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public int getProduct_id() {
                        return product_id;
                    }

                    public void setProduct_id(int product_id) {
                        this.product_id = product_id;
                    }

                    public String getParent_id() {
                        return parent_id;
                    }

                    public void setParent_id(String parent_id) {
                        this.parent_id = parent_id;
                    }

                    public int getVendor_id() {
                        return vendor_id;
                    }

                    public void setVendor_id(int vendor_id) {
                        this.vendor_id = vendor_id;
                    }

                    public int getLevel() {
                        return level;
                    }

                    public void setLevel(int level) {
                        this.level = level;
                    }

                    public int getAttribute_id() {
                        return attribute_id;
                    }

                    public void setAttribute_id(int attribute_id) {
                        this.attribute_id = attribute_id;
                    }

                    public double getPrice() {
                        return price;
                    }

                    public void setPrice(double price) {
                        this.price = price;
                    }

                    public int getStock() {
                        return stock;
                    }

                    public void setStock(int stock) {
                        this.stock = stock;
                    }

                    public String getIs_default() {
                        return is_default;
                    }

                    public void setIs_default(String is_default) {
                        this.is_default = is_default;
                    }

                    public String getIs_default_price() {
                        return is_default_price;
                    }

                    public void setIs_default_price(String is_default_price) {
                        this.is_default_price = is_default_price;
                    }

                    public String getCountry_code() {
                        return country_code;
                    }

                    public void setCountry_code(String country_code) {
                        this.country_code = country_code;
                    }

                    public String getImage() {
                        return image;
                    }

                    public void setImage(String image) {
                        this.image = image;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }
                }

                public class ProductTransFk {
                    public int id;
                    public int product_id;
                    public String title;
                    public String description;
                    public String details;
                    public String lang;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public int getProduct_id() {
                        return product_id;
                    }

                    public void setProduct_id(int product_id) {
                        this.product_id = product_id;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getDescription() {
                        return description;
                    }

                    public void setDescription(String description) {
                        this.description = description;
                    }

                    public String getDetails() {
                        return details;
                    }

                    public void setDetails(String details) {
                        this.details = details;
                    }

                    public String getLang() {
                        return lang;
                    }

                    public void setLang(String lang) {
                        this.lang = lang;
                    }
                }

            }
        }
    }

}
