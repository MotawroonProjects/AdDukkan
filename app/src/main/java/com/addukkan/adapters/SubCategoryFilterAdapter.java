package com.addukkan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.databinding.SubCategoryRow3Binding;
import com.addukkan.databinding.SubCategoryrowBinding;
import com.addukkan.models.CountryModel;
import com.addukkan.models.SubCategoryDataModel;
import com.addukkan.uis.activity_login.LoginActivity;
import com.addukkan.uis.activity_sign_up.SignUpActivity;

import java.util.List;

public class SubCategoryFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SubCategoryDataModel> list;
    private LayoutInflater inflater;
    private Context context;
    private AppCompatActivity activity;

    public SubCategoryFilterAdapter(Context context, List<SubCategoryDataModel> list) {
        inflater=LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        activity = (AppCompatActivity) context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SubCategoryRow3Binding binding = DataBindingUtil.inflate(inflater, R.layout.sub_category_row3, parent, false);
            return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private SubCategoryRow3Binding binding;

        public MyHolder(SubCategoryRow3Binding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

}
