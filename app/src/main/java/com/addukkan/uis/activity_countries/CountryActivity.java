package com.addukkan.uis.activity_countries;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.addukkan.R;
import com.addukkan.adapters.CountryAdapter;
import com.addukkan.databinding.ActivityCountryBinding;
import com.addukkan.language.Language;
import com.addukkan.models.AppLocalSettings;
import com.addukkan.models.CountryDataModel;
import com.addukkan.models.CountryModel;
import com.addukkan.models.SettingModel;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.remote.Api;
import com.addukkan.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryActivity extends AppCompatActivity {
    private ActivityCountryBinding binding;
    private String lang;
    private List<CountryModel> countryModelList;
    private CountryAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;
    private AppLocalSettings settings;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_country);
        initView();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        settings = preferences.isLanguageSelected(this);

        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        countryModelList = new ArrayList<>();
        binding.setLang(lang);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CountryAdapter(this, countryModelList);
        binding.recView.setAdapter(adapter);

        binding.llBack.setOnClickListener(view -> finish());

        getCountries();

    }

    private void getCountries() {

        Api.getService(Tags.base_url)
                .getCountries(lang)
                .enqueue(new Callback<CountryDataModel>() {
                    @Override
                    public void onResponse(Call<CountryDataModel> call, Response<CountryDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        binding.tvNoData.setVisibility(View.GONE);
                                        updateCountryData(response.body().getData());
                                    } else {
                                        binding.tvNoData.setVisibility(View.VISIBLE);

                                    }
                                }
                            } else {
                                Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            switch (response.code()) {
                                case 500:
                                    Toast.makeText(CountryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<CountryDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(CountryActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(CountryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void updateCountryData(List<CountryModel> data) {
        countryModelList.clear();
        countryModelList.addAll(data);
        int pos = getMyCountryPos();
        if (pos != -1) {
            CountryModel countryModel = countryModelList.get(pos);
            countryModel.setSelected(true);
            countryModelList.set(pos, countryModel);
        }
        adapter.notifyDataSetChanged();

        binding.recView.postDelayed(() -> {
            if (pos != -1) {
                binding.recView.scrollToPosition(pos);
            }
        }, 1000);
    }

    private int getMyCountryPos() {
        int pos = -1;
        if (userModel.getData().getUser_country() != null) {
            for (int index = 0; index < countryModelList.size(); index++) {
                CountryModel countryModel = countryModelList.get(index);
                if (countryModel.getId() == userModel.getData().getUser_country().getId()) {
                    pos = index;
                    return pos;
                }
            }
        }

        return pos;

    }


    public void setcountry(CountryModel countryModel) {
        settings.setCountry_code(countryModel.getCode());
        settings.setCurrency(countryModel.getCountry_setting_trans_fk().getCurrency());
        preferences.setIsLanguageSelected(this,settings);
        finish();
    }
}