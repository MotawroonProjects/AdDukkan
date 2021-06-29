package com.addukkan.uis.activity_product_detials.fragmentchild;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.addukkan.R;
import com.addukkan.databinding.FragmentDecriptionBinding;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDescription extends Fragment {
    private FragmentDecriptionBinding binding;

    private String query;
    private String desc;

    public static FragmentDescription newInstance() {
        return new FragmentDescription();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_decription, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
       //binding.setDesc(desc);

    }


    public void setDesc(String desc) {
        this.desc = desc;
        //binding.setDesc(desc);
    }


}
