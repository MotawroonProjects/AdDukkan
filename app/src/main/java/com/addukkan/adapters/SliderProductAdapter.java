package com.addukkan.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.addukkan.R;
import com.addukkan.databinding.SliderRowBinding;
import com.addukkan.models.SingleProductModel;
import com.addukkan.models.SliderDataModel;
import com.addukkan.tags.Tags;

import java.util.List;

public class SliderProductAdapter extends PagerAdapter {
    private List<SingleProductModel.ProductImage> list ;
    private Context context;
    private LayoutInflater inflater;

    public SliderProductAdapter(List<SingleProductModel.ProductImage> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SliderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.slider_row,container,false);
        binding.setPhoto(list.get(position).getImage());
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
