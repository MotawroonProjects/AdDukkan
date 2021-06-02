package com.addukkan.models;

import java.io.Serializable;

public class ProductDataModel extends ResponseModel implements Serializable {
    private SingleProductModel product_data;
    private SingleProductModel data;
    private int id;
    private int product_id;
    private int department_id;
    private int level;

    public SingleProductModel getProduct_data() {
        return product_data;
    }

    public SingleProductModel getData() {
        return data;
    }

    public int getId() {
        return id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public int getLevel() {
        return level;
    }
}
