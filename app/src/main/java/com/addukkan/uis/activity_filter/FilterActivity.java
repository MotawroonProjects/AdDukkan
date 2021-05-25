package com.addukkan.uis.activity_filter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.addukkan.R;
import com.addukkan.adapters.BrandAdapter;
import com.addukkan.adapters.CompanyAdapter;
import com.addukkan.adapters.SubCategoryFilterAdapter;
import com.addukkan.databinding.ActivityFilterBinding;
import com.addukkan.language.Language;
import com.addukkan.models.BrandDataModel;
import com.addukkan.models.CompanyDataModel;
import com.addukkan.models.CompanyModel;
import com.addukkan.models.MainCategoryDataModel;
import com.addukkan.models.SubCategoryDataModel;
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

public class FilterActivity extends AppCompatActivity {
    private ActivityFilterBinding binding;
    private String lang;
    private List<SubCategoryDataModel> subCategoryDataModelList;
    private SubCategoryFilterAdapter adapter;
    private List<CompanyModel> list;
    private CompanyAdapter companyAdapter;
    private List<BrandDataModel.Data> dataList;
    private BrandAdapter brandAdapter;
    private UserModel userModel;
    private Preferences preferences;
    private MainCategoryDataModel.Data sub_departments;
    private List<Integer> departments;
    private List<Integer> brand_id;
    private List<Integer> product_company_id;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            sub_departments = (MainCategoryDataModel.Data) intent.getSerializableExtra("data");

        }
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        subCategoryDataModelList = new ArrayList<>();
        list = new ArrayList<>();
        dataList = new ArrayList<>();
        subCategoryDataModelList.clear();
        departments=new ArrayList<>();
        brand_id=new ArrayList<>();
        product_company_id=new ArrayList<>();
        subCategoryDataModelList.addAll(sub_departments.getSub_departments());
        binding.setLang(lang);
        binding.recViewCountry.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubCategoryFilterAdapter(this, subCategoryDataModelList);
        binding.recViewCountry.setAdapter(adapter);
        companyAdapter = new CompanyAdapter(this, list);
        binding.recViewCompany.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewCompany.setAdapter(companyAdapter);
        brandAdapter = new BrandAdapter(this, dataList);
        binding.recViewBrand.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewBrand.setAdapter(brandAdapter);
        binding.llBack.setOnClickListener(view -> finish());


        binding.llCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.elexpendCompany.isExpanded()) {
                    binding.elexpendCompany.setExpanded(false);
                } else {
                    binding.elexpendCompany.setExpanded(true);
                }
            }
        });
        binding.llBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.elexpendBrand.isExpanded()) {
                    binding.elexpendBrand.setExpanded(false);
                } else {
                    binding.elexpendBrand.setExpanded(true);
                }
            }
        });
        binding.lldepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.elexpendDepart.isExpanded()) {
                    binding.elexpendDepart.setExpanded(false);
                } else {
                    binding.elexpendDepart.setExpanded(true);
                }
            }
        });
        getCompanies();
        getBrands();
    }

    private void getCompanies() {
        list.clear();

        companyAdapter.notifyDataSetChanged();


        Api.getService(Tags.base_url)
                .getCompany("all").enqueue(new Callback<CompanyDataModel>() {
            @Override
            public void onResponse(Call<CompanyDataModel> call, Response<CompanyDataModel> response) {
                //   binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus() == 200) {
                        if (response.body().getData() != null) {
                            if (response.body().getData().size() > 0) {
                                //     binding.tvNoData.setVisibility(View.GONE);
                                list.clear();
                                list.addAll(response.body().getData());
                                companyAdapter.notifyDataSetChanged();
                            } else {
                                // binding.tvNoData.setVisibility(View.VISIBLE);

                            }
                        }
                    } else {
                        //    Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                    }


                } else {
                    //     binding.progBar.setVisibility(View.GONE);

                    switch (response.code()) {
                        case 500:
                            //  Toast.makeText(CountryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            // Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            break;
                    }
                    try {
                        Log.e("error_code", response.code() + "_");
                    } catch (NullPointerException e) {

                    }
                }


            }

            @Override
            public void onFailure(Call<CompanyDataModel> call, Throwable t) {
                try {
                    //   binding.progBar.setVisibility(View.GONE);
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            //Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                        } else {
                            //Toast.makeText(CountryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {

                }
            }
        });

    }

    private void getBrands() {
        dataList.clear();

        brandAdapter.notifyDataSetChanged();


        Api.getService(Tags.base_url)
                .getBrands("all").enqueue(new Callback<BrandDataModel>() {
            @Override
            public void onResponse(Call<BrandDataModel> call, Response<BrandDataModel> response) {
                //   binding.progBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().getStatus() == 200) {
                        if (response.body().getData() != null) {
                            if (response.body().getData().size() > 0) {
                                //     binding.tvNoData.setVisibility(View.GONE);
                                dataList.clear();
                                dataList.addAll(response.body().getData());
                                brandAdapter.notifyDataSetChanged();
                            } else {
                                // binding.tvNoData.setVisibility(View.VISIBLE);

                            }
                        }
                    } else {
                        //    Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                    }


                } else {
                    //     binding.progBar.setVisibility(View.GONE);

                    switch (response.code()) {
                        case 500:
                            //  Toast.makeText(CountryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            // Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            break;
                    }
                    try {
                        Log.e("error_code", response.code() + "_");
                    } catch (NullPointerException e) {

                    }
                }


            }

            @Override
            public void onFailure(Call<BrandDataModel> call, Throwable t) {
                try {
                    //   binding.progBar.setVisibility(View.GONE);
                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            //Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                        } else {
                            //Toast.makeText(CountryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {

                }
            }
        });

    }


}