package com.addukkan.uis.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.addukkan.R;
import com.addukkan.databinding.FragmentOffersBinding;
import com.addukkan.uis.activity_home.HomeActivity;

public class FragmentOffer extends Fragment {
    private FragmentOffersBinding binding;
    private HomeActivity activity;


    public static FragmentOffer newInstance() {
        return new FragmentOffer();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();

    }


}
