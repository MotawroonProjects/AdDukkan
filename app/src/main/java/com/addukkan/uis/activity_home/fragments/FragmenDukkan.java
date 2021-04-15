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
import com.addukkan.databinding.FragmentDukkansBinding;
import com.addukkan.databinding.FragmentHomeBinding;
import com.addukkan.uis.activity_home.HomeActivity;

public class FragmenDukkan extends Fragment {
    private FragmentDukkansBinding binding;
    private HomeActivity activity;


    public static FragmenDukkan newInstance() {
        return new FragmenDukkan();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dukkans, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();

    }


}
