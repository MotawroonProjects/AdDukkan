package com.addukkan.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.databinding.ProductRowBinding;
import com.addukkan.models.ProductDataModel;
import com.addukkan.models.SingleProductModel;
import com.addukkan.uis.activity_home.fragments.FragmentHome;

import java.util.List;

public class Product2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SingleProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private int i;

    //private Fragment_Main fragment_main;
    public Product2Adapter(List<SingleProductModel> list, Context context,Fragment fragment,int i) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment=fragment;
        this.i=i;
      //  this.fragment_main=fragment_main;


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

                if (fragment instanceof FragmentHome) {

                    FragmentHome fragment_main = (FragmentHome) fragment;

                        fragment_main.like_dislike(list.get(myHolder.getLayoutPosition()), myHolder.getLayoutPosition(), i);

                }




        });
        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentHome) {

                FragmentHome fragment_main = (FragmentHome) fragment;


                fragment_main.showData(list.get(myHolder.getLayoutPosition()).getProduct_trans_fk().getProduct_id()+"");

            }
        });

        myHolder.binding.imgIncrease.setOnClickListener(v -> {
            SingleProductModel model = list.get(myHolder.getAdapterPosition());

            if(fragment instanceof FragmentHome){

                if (!model.isLoading()){
                    model.setLoading(true);
                    notifyItemChanged(myHolder.getAdapterPosition());
                    FragmentHome fragmentHome=(FragmentHome)fragment;
                    fragmentHome.additemtoCart(model,myHolder.getAdapterPosition(),i);
                }


            }
        });
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
