package com.addukkan.uis.activity_location_detials;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.addukkan.R;
import com.addukkan.databinding.ActivityAddressInformationBinding;
import com.addukkan.language.Language;
import com.addukkan.models.AddOrderModel;
import com.addukkan.models.LocationDetialsModel;
import com.addukkan.models.StatusResponse;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.remote.Api;
import com.addukkan.share.Common;
import com.addukkan.tags.Tags;
import com.addukkan.uis.activity_cart.CartActivity;
import com.addukkan.uis.activity_complete_order_detials.CompleteOrderDetialsActivity;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationDetialsActivity extends AppCompatActivity {
    private ActivityAddressInformationBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private LocationDetialsModel locationDetialsModel;
    private String lang = "ar";
    private AddOrderModel addorderModel;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        addorderModel = (AddOrderModel) intent.getSerializableExtra("data");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address_information);
       getDataFromIntent();
        initView();

    }

    private void initView() {
        binding.setTitle(addorderModel.getAddress());
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        locationDetialsModel = new LocationDetialsModel();
        if (userModel != null) {
            locationDetialsModel.setName(userModel.getData().getName());

            locationDetialsModel.setPhone(userModel.getData().getPhone());
        }

        binding.setModel(locationDetialsModel);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.btnLogin.setOnClickListener(view -> {
            if (locationDetialsModel.isDataValid(this)) {
                addorderModel.setPhone(locationDetialsModel.getPhone());
                addorderModel.setNotes(locationDetialsModel.getDetials());
                addorderModel.setName(locationDetialsModel.getName());
                Intent intent=new Intent(LocationDetialsActivity.this, CompleteOrderDetialsActivity.class);
                intent.putExtra("data",addorderModel);
                startActivity(intent);
                finish();
            }
        });
        binding.llBack.setOnClickListener(view -> finish());
    }

}