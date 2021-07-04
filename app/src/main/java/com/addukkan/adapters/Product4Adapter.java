package com.addukkan.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.databinding.ProductRow2Binding;
import com.addukkan.databinding.ProductRowBinding;
import com.addukkan.models.SingleProductModel;
import com.addukkan.uis.activity_product_detials.ProductDetialsActivity;
import com.addukkan.uis.activity_products.ProductsActivity;

import java.util.List;

public class Product4Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SingleProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private ProductsActivity activity;

    public Product4Adapter(List<SingleProductModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (ProductsActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ProductRow2Binding binding = DataBindingUtil.inflate(inflater, R.layout.product_row2, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.tvOldprice.setPaintFlags(myHolder.binding.tvOldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (list.get(position).getFavourite() != null) {
            ((MyHolder) holder).binding.checkbox.setChecked(true);
        } else {

            myHolder.binding.checkbox.setChecked(false);

        }
        myHolder.binding.checkbox.setOnClickListener(v -> {
            activity.like_dislike(list.get(myHolder.getAdapterPosition()), myHolder.getLayoutPosition());

        });

        myHolder.itemView.setOnClickListener(view -> {
            activity.showData(list.get(myHolder.getAdapterPosition()).getId() + "");

        });
        myHolder.binding.imgIncrease.setOnClickListener(v -> activity.addToCart(list.get(holder.getAdapterPosition()), myHolder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public ProductRow2Binding binding;

        public MyHolder(@NonNull ProductRow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
