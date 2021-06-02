package com.addukkan.uis.activity_product_detials.fragmentchild;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;


import com.addukkan.R;
import com.addukkan.databinding.FragmentRateBinding;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.uis.activity_product_detials.ProductDetialsActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRate extends Fragment {
    private FragmentRateBinding binding;
    private ProductDetialsActivity activity;

    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";



    public static FragmentRate newInstance() {
        return new FragmentRate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rate, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (ProductDetialsActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

    }




}
