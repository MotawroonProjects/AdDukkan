package com.addukkan.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.databinding.CartProductRowBinding;
import com.addukkan.databinding.FavouriteProductRowBinding;
import com.addukkan.models.CartDataModel;
import com.addukkan.models.FavouriteProductDataModel;
import com.addukkan.uis.activity_cart.CartActivity;
import com.addukkan.uis.activity_my_favorite.MyFavoriteActivity;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CartDataModel.Data.Detials> list;
    private Context context;
    private LayoutInflater inflater;
    //private Fragment_Main fragment_main;
    public CartProductAdapter(List<CartDataModel.Data.Detials> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
      //  this.fragment_main=fragment_main;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CartProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.cart_product_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.tvOldprice.setPaintFlags(myHolder.binding.tvOldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        myHolder.binding.imgIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof  CartActivity){
                    CartActivity cartActivity=(CartActivity) context;

                    cartActivity.additemtoCart(list.get(holder.getLayoutPosition()),holder.getAdapterPosition(),"increment");
                }
            }
        });
        myHolder.binding.imgDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof  CartActivity){
                    CartActivity cartActivity=(CartActivity) context;
                    CartDataModel.Data.Detials detials = list.get(holder.getLayoutPosition());
                    if (detials.getAmount()>1){
                        cartActivity.additemtoCart(list.get(holder.getAdapterPosition()),holder.getAdapterPosition(),"decrement");

                    }
                }
            }
        });
        myHolder.binding.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof  CartActivity){
                    CartActivity cartActivity=(CartActivity) context;
                    cartActivity.deleteItemFromcart(list.get(holder.getLayoutPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public CartProductRowBinding binding;

        public MyHolder(@NonNull CartProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}
