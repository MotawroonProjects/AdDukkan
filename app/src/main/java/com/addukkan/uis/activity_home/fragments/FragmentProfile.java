package com.addukkan.uis.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.addukkan.R;
import com.addukkan.databinding.FragmentHomeBinding;
import com.addukkan.databinding.FragmentProfileBinding;
import com.addukkan.uis.activity_home.HomeActivity;
import com.addukkan.uis.activity_login.LoginActivity;

import io.paperdb.Paper;

public class FragmentProfile extends Fragment {
    private FragmentProfileBinding binding;
    private HomeActivity activity;
    private String lang = "ar";


    public static FragmentProfile newInstance() {
        return new FragmentProfile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);

        binding.iconEdit.setOnClickListener(v -> {
            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity(intent);
        });
    }


}
