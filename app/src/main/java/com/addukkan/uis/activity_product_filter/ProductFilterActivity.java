package com.addukkan.uis.activity_product_filter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.addukkan.R;
import com.addukkan.adapters.DoctorsAdapter;
import com.addukkan.adapters.DuckanCategoryAdapter;
import com.addukkan.adapters.Product2Adapter;
import com.addukkan.adapters.ProductLisProductAdapter;
import com.addukkan.adapters.ProductOfferAdapter;
import com.addukkan.databinding.ActivityProductFilterBinding;
import com.addukkan.interfaces.Listeners;
import com.addukkan.language.Language;
import com.addukkan.models.ALLProductDataModel;
import com.addukkan.models.AppLocalSettings;
import com.addukkan.models.FilterModel;
import com.addukkan.models.MainCategoryDataModel;
import com.addukkan.models.SingleProductModel;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.remote.Api;
import com.addukkan.tags.Tags;
import com.addukkan.uis.activity_filter.FilterActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFilterActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityProductFilterBinding binding;
    private String lang;
    private FilterModel filterModel;
    private Preferences preferences;
    private UserModel userModel;
    private AppLocalSettings settings;
    private MainCategoryDataModel.Data sub_departments;
    private List<Integer> departments;
    private List<Integer> brand_id;
    private List<Integer> product_company_id;
    private List<SingleProductModel> productModelList;
    private ProductOfferAdapter product2Adapter;
    private ProductLisProductAdapter productLisProductAdapter;
    private int pos;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_filter);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            pos = intent.getIntExtra("pos", 0);
            sub_departments = (MainCategoryDataModel.Data) intent.getSerializableExtra("data");

        }
    }

    private void initView() {
        filterModel = new FilterModel();
        departments = new ArrayList<>();
        brand_id = new ArrayList<>();
        product_company_id = new ArrayList<>();
        productModelList = new ArrayList<>();
        product2Adapter = new ProductOfferAdapter(productModelList, this, null);
        productLisProductAdapter = new ProductLisProductAdapter(productModelList, this, null);

        binding.recView.setLayoutManager(new GridLayoutManager(this, 2));
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.anim);

        binding.recView.setLayoutAnimation(controller);
        binding.recView.setAdapter(product2Adapter);
        binding.recView.scheduleLayoutAnimation();
        binding.imagfilteraccord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSheet();
            }
        });
        binding.imfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductFilterActivity.this, FilterActivity.class);
                intent.putExtra("data",sub_departments);
                intent.putExtra("pos",pos);
                startActivityForResult(intent,100);
            }
        });
        departments.add(sub_departments.getSub_departments().get(pos).getId());
        filterModel.setDepartments(departments);
        filterModel.setBrand_id(brand_id);
        filterModel.setProduct_company_id(product_company_id);
        preferences = Preferences.getInstance();
        binding.setTitle(sub_departments.getDepartment_trans_fk().getTitle());
        settings = preferences.isLanguageSelected(this);

        userModel = preferences.getUserData(this);

        if (userModel != null) {
            filterModel.setCountry_code(userModel.getData().getCountry_code());
        } else {
            filterModel.setCountry_code(settings.getCountry_code());
        }
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.flData.setOnClickListener(v -> openSheet());
        binding.cardclose.setOnClickListener(v -> closeSheet());
        //binding.progBar.setVisibility(View.GONE);

       // binding.recView.scheduleLayoutAnimation();
binding.imlist.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        binding.imlist.setColorFilter(ContextCompat.getColor(ProductFilterActivity.this, R.color.colorAccent));
        binding.immenu.setColorFilter(ContextCompat.getColor(ProductFilterActivity.this, R.color.gray11));

        binding.recView.setLayoutManager(new LinearLayoutManager(ProductFilterActivity.this));
  binding.recView.setAdapter(productLisProductAdapter);

    }
});
binding.immenu.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        binding.immenu.setColorFilter(ContextCompat.getColor(ProductFilterActivity.this, R.color.colorAccent));
        binding.imlist.setColorFilter(ContextCompat.getColor(ProductFilterActivity.this, R.color.gray11));
        binding.recView.setLayoutManager(new GridLayoutManager(ProductFilterActivity.this,2));
   binding.recView.setAdapter(product2Adapter);
    }
});
        binding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == binding.rbRate.getId()) {
                    filterModel.setPrice_order("all");
                    filterModel.setProduct_order("all");
                    filterModel.setRate_order("asc");


                } else if (checkedId == binding.rbRecent.getId()) {
                    filterModel.setPrice_order("all");
                    filterModel.setProduct_order("asc");
                    filterModel.setRate_order("all");
                } else if (checkedId == binding.rbLowPrice.getId()) {
                    filterModel.setPrice_order("lowest_price");
                    filterModel.setProduct_order("all");
                    filterModel.setRate_order("all");
                } else if (checkedId == binding.rbHighPrice.getId()) {
                    filterModel.setPrice_order("highest_price");
                    filterModel.setProduct_order("all");
                    filterModel.setRate_order("all");
                }
                closeSheet();
            }
        });
        filterData();

    }

    private void openSheet() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        binding.flData.clearAnimation();
        binding.flData.startAnimation(animation);


        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.flData.setVisibility(View.VISIBLE);


            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void closeSheet() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        binding.flData.clearAnimation();
        binding.flData.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.flData.setVisibility(View.GONE);
                filterData();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public void back() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    private void filterData() {
        productModelList.clear();
        product2Adapter.notifyDataSetChanged();
        productLisProductAdapter.notifyDataSetChanged();
        String user_id = null;
        if (userModel != null) {
            user_id = userModel.getData().getId() + "";
        }
        binding.progBar.setVisibility(View.VISIBLE);
        //   Log.e("sllsks", user_id + lang + country_coude);
        Api.getService(Tags.base_url)
                .Filter(lang, filterModel)
                .enqueue(new Callback<ALLProductDataModel>() {
                    @Override
                    public void onResponse(Call<ALLProductDataModel> call, Response<ALLProductDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        Log.e("Slslls", response.message());
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {

                                productModelList.clear();
                                productModelList.addAll(response.body().getData());

                                if (productModelList.size() > 0) {
                                    product2Adapter.notifyDataSetChanged();
                                    productLisProductAdapter.notifyDataSetChanged();

//                                binding.tvNoDatadepart.setVisibility(View.GONE);
                                    //Log.e(",dkdfkfkkfk", categoryDataModelDataList.get(0).getTitle());
                                } else {
//                                binding.tvNoDatadepart.setVisibility(View.VISIBLE);

                                }

                            }
                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            try {
                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                //Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ALLProductDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //  Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK){
            FilterModel filterModel= (FilterModel) data.getSerializableExtra("data");
            this.filterModel.setProduct_company_id(filterModel.getProduct_company_id());
            this.filterModel.setDepartments(filterModel.getDepartments());
            this.filterModel.setBrand_id(filterModel.getBrand_id());
            filterData();
        }

    }
}
