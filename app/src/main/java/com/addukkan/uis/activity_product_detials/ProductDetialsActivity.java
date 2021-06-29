package com.addukkan.uis.activity_product_detials;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.adapters.PagerAdapter;
import com.addukkan.adapters.ProductAttrAdapter;
import com.addukkan.adapters.ProductChildParentAttrAdapter;
import com.addukkan.adapters.SliderProductAdapter;
import com.addukkan.databinding.ActivityProductDetialsBinding;
import com.addukkan.language.Language;
import com.addukkan.models.AppLocalSettings;
import com.addukkan.models.AttrDataModel;
import com.addukkan.models.ProductDataModel;
import com.addukkan.models.SingleProductModel;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.remote.Api;
import com.addukkan.share.Common;
import com.addukkan.tags.Tags;
import com.addukkan.uis.activity_product_detials.fragmentchild.FragmentDescription;
import com.addukkan.uis.activity_product_detials.fragmentchild.FragmentRate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetialsActivity extends AppCompatActivity {
    private ActivityProductDetialsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang = "ar";
    private List<ProductDataModel.ParentAttributes> list;
    private ProductAttrAdapter parentAdapter;
    private ProductChildParentAttrAdapter childAdapter;
    private List<ProductDataModel.Attribute> childList;
    private String id;
    private AppLocalSettings settings;
    private String country_coude;
    private PagerAdapter pagerAdapter;
    private static List<RecyclerView> recyclerViewList;
    private static List<TextView> textViewList;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detials);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

    }

    private void initView() {
        recyclerViewList = new ArrayList<>();
        textViewList=new ArrayList<>();
        list = new ArrayList<>();
        childList = new ArrayList<>();
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        settings = preferences.isLanguageSelected(this);


        if (userModel != null) {
            country_coude = userModel.getData().getCountry_code();
        } else {
            country_coude = settings.getCountry_code();
        }
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        parentAdapter = new ProductAttrAdapter(this, list);
        binding.recViewParent.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewParent.setAdapter(parentAdapter);

        childAdapter = new ProductChildParentAttrAdapter(this,childList);
        binding.recViewChildren.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewChildren.setAdapter(childAdapter);

        binding.llBack.setOnClickListener(view -> finish());
        binding.tab1.setupWithViewPager(binding.pager1);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(getFragments());
        pagerAdapter.addTitle(getTitles());
        binding.pager1.setAdapter(pagerAdapter);
        getData();

    }

    public void getData() {

        String user_id = null;
        if (userModel != null) {
            user_id = userModel.getData().getId() + "";
        }

        binding.progBar.setVisibility(View.VISIBLE);
        list.clear();
        parentAdapter.notifyDataSetChanged();
        Log.e("Eerr", id);
        Api.getService(Tags.base_url)
                .getSingleProduct(lang, id, user_id, country_coude)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {

                                if (response.body().getData() != null) {
                                    updateData(response.body().getData());
                                } else {
                                    // binding.tvNoData.setVisibility(View.VISIBLE);
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
                    public void onFailure(Call<ProductDataModel> call, Throwable t) {
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

    private void updateData(ProductDataModel.Data body) {
        binding.tvOldprice.setPaintFlags(binding.tvOldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        list.clear();
        binding.nested.setVisibility(View.VISIBLE);
        list.add(body.getParentAttributes());
        parentAdapter.notifyDataSetChanged();

        if (body.getAttributes()!=null){
            childList.clear();
            childList.addAll(body.getAttributes());
            childAdapter.notifyDataSetChanged();
        }


        binding.setModel(body.getProduct());



        if (body.getProduct().getProduct_images() != null && body.getProduct().getProduct_images().size() > 0) {
            SliderProductAdapter sliderProductAdapter = new SliderProductAdapter(body.getProduct().getProduct_images(), this);
            binding.tab.setupWithViewPager(binding.pager);
            binding.pager.setAdapter(sliderProductAdapter);
            binding.flSlider.setVisibility(View.VISIBLE);
            binding.flNoSlider.setVisibility(View.GONE);
        } else {
            binding.flSlider.setVisibility(View.GONE);
            binding.flNoSlider.setVisibility(View.VISIBLE);
        }
        //FragmentDescription fragmentDescription = (FragmentDescription) pagerAdapter.getItem(0);
        //fragmentDescription.setDesc(body.getProduct().getProduct_trans_fk().getDescription());
    }
    public void getFeatures(int attribute_id){
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .getFeature(lang, id,attribute_id+"")
                .enqueue(new Callback<AttrDataModel>() {
                    @Override
                    public void onResponse(Call<AttrDataModel> call, Response<AttrDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {

                                if (response.body().getData().getAttributes().size()>0) {
                                    childList.clear();
                                    childList.addAll(response.body().getData().getAttributes());
                                    childAdapter.notifyDataSetChanged();
                                } else {
                                    childList.clear();
                                    childAdapter.notifyDataSetChanged();

                                    // binding.tvNoData.setVisibility(View.VISIBLE);
                                }


                            }
                        } else {
                            dialog.dismiss();

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
                    public void onFailure(Call<AttrDataModel> call, Throwable t) {
                        try {
                            dialog.dismiss();

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
    private List<Fragment> getFragments() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(FragmentDescription.newInstance());
        fragmentList.add(FragmentRate.newInstance());


        return fragmentList;

    }

    private List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.description));
        titles.add(getString(R.string.reviews));

        return titles;

    }


}