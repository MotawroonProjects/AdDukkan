package com.addukkan.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
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

    private List<ProductDataModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    //private Fragment_Main fragment_main;
    public ProductAdapter(List<ProductDataModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment=fragment;
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
        myHolder.binding.setModel(list.get(position).getProduct_data());
        myHolder.binding.tvOldprice.setPaintFlags(myHolder.binding.tvOldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (list.get(position).getProduct_data().getFavourite() != null) {
            ((MyHolder) holder).binding.checkbox.setChecked(true);
        }
        myHolder.binding.checkbox.setOnClickListener(v -> {

            if (fragment instanceof FragmentHome) {

                FragmentHome fragment_main = (FragmentHome) fragment;


                    fragment_main.like_dislike(list.get(myHolder.getAdapterPosition()).getProduct_data(), myHolder.getAdapterPosition(),0);

            }




        });

        myHolder.itemView.setOnClickListener(view -> {
           // Log.e("sssss",list.get(holder.getLayoutPosition()).getId()+"");

           // fragment_main.setitemData(list.get(holder.getLayoutPosition()).getId()+"");
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
