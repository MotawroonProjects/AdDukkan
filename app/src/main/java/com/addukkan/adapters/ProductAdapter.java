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
import com.addukkan.databinding.MainCategoryRowBinding;
import com.addukkan.databinding.ProductRowBinding;
import com.addukkan.models.MainCategoryDataModel;
import com.addukkan.models.ProductDataModel;
import com.addukkan.models.SingleProductModel;
import com.addukkan.uis.activity_home.fragments.FragmentHome;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MainCategoryDataModel.ProductData> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private int parent_pos;
    public ProductAdapter(List<MainCategoryDataModel.ProductData> list, Context context, Fragment fragment,int parent_pos) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment=fragment;
        this.parent_pos = parent_pos;
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
        SingleProductModel model = list.get(position).getProduct_data();

        myHolder.binding.setModel(model);
        myHolder.binding.tvOldprice.setPaintFlags(myHolder.binding.tvOldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (model.getFavourite() != null) {
           myHolder.binding.checkbox.setChecked(true);
        }else {
            myHolder.binding.checkbox.setChecked(false);

        }
        myHolder.binding.checkbox.setOnClickListener(v -> {
            SingleProductModel model2 = list.get(myHolder.getAdapterPosition()).getProduct_data();

            if (fragment instanceof FragmentHome) {


                FragmentHome fragment_main = (FragmentHome) fragment;


                    fragment_main.like_dislike(model2, myHolder.getAdapterPosition(),2);

            }




        });

        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentHome) {
                SingleProductModel model2 = list.get(myHolder.getAdapterPosition()).getProduct_data();

                FragmentHome fragment_main = (FragmentHome) fragment;


                fragment_main.showData(model2.getId()+"");

            }
        });
        myHolder.binding.imgIncrease.setOnClickListener(v -> {
            SingleProductModel model2 = list.get(myHolder.getAdapterPosition()).getProduct_data();

            if(fragment instanceof FragmentHome){

                if (!model2.isLoading()){
                    model2.setLoading(true);
                    notifyItemChanged(myHolder.getAdapterPosition());
                    FragmentHome fragmentHome=(FragmentHome)fragment;
                    fragmentHome.additemtoCart2(model2,myHolder.getAdapterPosition(),parent_pos);

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
