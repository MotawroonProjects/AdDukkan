package com.addukkan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.databinding.BrandRowBinding;
import com.addukkan.databinding.ChildRowBinding;
import com.addukkan.models.BrandDataModel;
import com.addukkan.models.ProductDataModel;
import com.addukkan.uis.activity_filter.FilterActivity;
import com.addukkan.uis.activity_product_detials.ProductDetialsActivity;

import java.util.List;


public class ChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<ProductDataModel.Attribute> list;
    private ProductDetialsActivity activity;

    public ChildAdapter(Context context, List<ProductDataModel.Attribute> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        activity = (ProductDetialsActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ChildRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.child_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            activity.getFeatures(list.get(position).getId());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ChildRowBinding binding;

        public MyHolder(ChildRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }
}
