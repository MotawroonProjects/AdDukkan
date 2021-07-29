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
import com.addukkan.databinding.OfferProductRowBinding;
import com.addukkan.databinding.ProductRowBinding;
import com.addukkan.models.SingleProductModel;
import com.addukkan.uis.activity_home.fragments.FragmentHome;
import com.addukkan.uis.activity_home.fragments.FragmentOffer;
import com.addukkan.uis.activity_product_filter.ProductFilterActivity;
import com.addukkan.uis.activity_search.SearchActivity;

import java.util.List;

public class ProductOfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SingleProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public ProductOfferAdapter(List<SingleProductModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        OfferProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.offer_product_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.tvOldprice.setPaintFlags(myHolder.binding.tvOldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (list.get(position).getFavourite() != null) {
            ((MyHolder) holder).binding.checkbox.setChecked(true);
        }
        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentOffer) {

                FragmentOffer fragmentOffer = (FragmentOffer) fragment;

                fragmentOffer.setItemData(list.get(myHolder.getAdapterPosition()).getId()+"");

            }

        });
        myHolder.binding.checkbox.setOnClickListener(v -> {

            if (fragment instanceof FragmentOffer) {

                FragmentOffer fragment_main = (FragmentOffer) fragment;

                fragment_main.like_dislike(list.get(myHolder.getLayoutPosition()), myHolder.getLayoutPosition(), 0);

            } else if (context instanceof ProductFilterActivity) {
                ProductFilterActivity productFilterActivity = (ProductFilterActivity) context;

                productFilterActivity.like_dislike(list.get(myHolder.getLayoutPosition()), myHolder.getLayoutPosition(), 0);

            }
            else if (context instanceof SearchActivity) {
                SearchActivity productFilterActivity = (SearchActivity) context;

                productFilterActivity.like_dislike(list.get(myHolder.getLayoutPosition()), myHolder.getLayoutPosition(), 0);

            }


        });
        myHolder.binding.imgIncrease.setOnClickListener(v -> {
            SingleProductModel model = list.get(myHolder.getAdapterPosition());
            if (!model.isLoading()) {
                model.setLoading(true);
                notifyItemChanged(myHolder.getAdapterPosition());}

            if (fragment instanceof FragmentOffer) {

                FragmentOffer fragment_main = (FragmentOffer) fragment;

                fragment_main.additemtoCart(model, myHolder.getAdapterPosition(), 0);

            } else if (context instanceof ProductFilterActivity) {
                ProductFilterActivity productFilterActivity = (ProductFilterActivity) context;
                productFilterActivity.additemtoCart(model, myHolder.getAdapterPosition(), 0);

            } else if (context instanceof SearchActivity) {
                SearchActivity productFilterActivity = (SearchActivity) context;
                productFilterActivity.additemtoCart(model, myHolder.getAdapterPosition(), 0);

            }


        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public OfferProductRowBinding binding;

        public MyHolder(@NonNull OfferProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
