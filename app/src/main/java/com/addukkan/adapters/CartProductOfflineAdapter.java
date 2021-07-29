package com.addukkan.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.databinding.CartProductOfflineRowBinding;
import com.addukkan.databinding.CartProductRowBinding;
import com.addukkan.models.AddCartDataModel;
import com.addukkan.models.AddCartProductItemModel;
import com.addukkan.models.CartDataModel;
import com.addukkan.uis.activity_cart.CartActivity;

import java.util.List;

public class CartProductOfflineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  CartActivity activity;
    private List<AddCartProductItemModel> list;
    private Context context;
    private LayoutInflater inflater;
    //private Fragment_Main fragment_main;
    public CartProductOfflineAdapter(List<AddCartProductItemModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
      //  this.fragment_main=fragment_main;
        activity = (CartActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CartProductOfflineRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.cart_product_offline_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.tvOldprice.setPaintFlags(myHolder.binding.tvOldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        myHolder.binding.imgIncrease.setOnClickListener(v -> {
            AddCartProductItemModel model2 = list.get(myHolder.getAdapterPosition());

            double amount = model2.getAmount() + 1;
            model2.setAmount(amount);
            activity.increase_decrease(model2, myHolder.getAdapterPosition());
        });

        myHolder.binding.imgDecrease.setOnClickListener(v -> {
            AddCartProductItemModel model2 = list.get(myHolder.getAdapterPosition());

            if (model2.getAmount() > 1) {
                double amount = model2.getAmount() - 1;
                model2.setAmount(amount);
                activity.increase_decrease(model2, myHolder.getAdapterPosition());
            }

        });

        myHolder.binding.imgRemove.setOnClickListener(v -> {
            AddCartProductItemModel model2 = list.get(myHolder.getAdapterPosition());
            activity.deleteItem(model2, myHolder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public CartProductOfflineRowBinding binding;

        public MyHolder(@NonNull CartProductOfflineRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}