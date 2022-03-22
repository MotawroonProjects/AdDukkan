package com.addukkanapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkanapp.R;
import com.addukkanapp.databinding.SubCategoryrow2Binding;
import com.addukkanapp.databinding.SubCategoryrowBinding;
import com.addukkanapp.models.MainCategoryDataModel;
import com.addukkanapp.models.SubCategoryDataModel;
import com.addukkanapp.uis.activity_home.fragments.FragmenDukkan;
import com.addukkanapp.uis.activity_home.fragments.FragmentHome;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DATA = 1;
    private final int DATA2 = 2;
    private List<SubCategoryDataModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private MainCategoryDataModel.Data data;


    public SubCategoryAdapter(List<SubCategoryDataModel> list, Context context, Fragment fragment, MainCategoryDataModel.Data data) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
        this.data = data;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == DATA) {
            SubCategoryrowBinding binding = DataBindingUtil.inflate(inflater, R.layout.sub_categoryrow, parent, false);
            return new MyHolder(binding);
        } else {
            SubCategoryrow2Binding binding = DataBindingUtil.inflate(inflater, R.layout.sub_categoryrow2, parent, false);
            return new MyHolder2(binding);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            myHolder.binding.setModel(list.get(position));

            myHolder.itemView.setOnClickListener(view -> {
                if (fragment instanceof FragmenDukkan) {
                    FragmenDukkan fragmenDukkan = (FragmenDukkan) fragment;
                    fragmenDukkan.filter(holder.getLayoutPosition());
                } else if (fragment instanceof FragmentHome) {
                    FragmentHome fragmentHome = (FragmentHome) fragment;
                    fragmentHome.filter(holder.getLayoutPosition(), data);
                }

            });
        } else {
            MyHolder2 myHolder = (MyHolder2) holder;
            myHolder.binding.setModel(list.get(position));

            myHolder.itemView.setOnClickListener(view -> {
                if (fragment instanceof FragmenDukkan) {
                    FragmenDukkan fragmenDukkan = (FragmenDukkan) fragment;
                    fragmenDukkan.filter(holder.getLayoutPosition());
                } else if (fragment instanceof FragmentHome) {
                    FragmentHome fragmentHome = (FragmentHome) fragment;
                    fragmentHome.filter(holder.getLayoutPosition(), data);
                }

            });
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public SubCategoryrowBinding binding;

        public MyHolder(@NonNull SubCategoryrowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public class MyHolder2 extends RecyclerView.ViewHolder {
        public SubCategoryrow2Binding binding;

        public MyHolder2(@NonNull SubCategoryrow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


    @Override
    public int getItemViewType(int position) {
        if (fragment instanceof FragmentHome) {
            return DATA;
        } else {
            return DATA2;
        }
    }
}
