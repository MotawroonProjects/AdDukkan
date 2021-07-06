package com.addukkan.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.databinding.ProductRowBinding;
import com.addukkan.models.SingleProductModel;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.uis.activity_home.fragments.FragmentHome;
import com.addukkan.uis.activity_product_detials.ProductDetialsActivity;

import java.util.List;

public class Product3Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SingleProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private ProductDetialsActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    public Product3Adapter(List<SingleProductModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        activity = (ProductDetialsActivity) context;
        preferences = Preferences.getInstance();


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.product_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.tvOldprice.setPaintFlags(myHolder.binding.tvOldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (list.get(position).getFavourite() != null) {
            ((MyHolder) holder).binding.checkbox.setChecked(true);
        }else {
            ((MyHolder) holder).binding.checkbox.setChecked(false);
        }
        myHolder.binding.checkbox.setOnClickListener(v -> {
            userModel = preferences.getUserData(context);
            boolean checked = myHolder.binding.checkbox.isChecked();
            if (userModel==null){
                myHolder.binding.checkbox.setChecked(!checked);
            }else {
                activity.like_dislike(list.get(myHolder.getAdapterPosition()), myHolder.getLayoutPosition(),2);

            }

        });

        myHolder.itemView.setOnClickListener(view -> {
            activity.showData(list.get(myHolder.getAdapterPosition()).getId()+"");

        });
        myHolder.binding.imgIncrease.setOnClickListener(v -> activity.additemtoCart2(list.get(holder.getAdapterPosition()),((MyHolder) holder).binding));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public ProductRowBinding binding;

        public MyHolder(@NonNull ProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }




}