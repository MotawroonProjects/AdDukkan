package com.addukkan.uis.activity_home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.addukkan.R;
import com.addukkan.adapters.ProductOfferAdapter;
import com.addukkan.adapters.SliderAdapter;
import com.addukkan.databinding.FragmentOffersBinding;
import com.addukkan.databinding.OfferProductRowBinding;
import com.addukkan.databinding.ProductRowBinding;
import com.addukkan.models.ALLProductDataModel;
import com.addukkan.models.AddCartDataModel;
import com.addukkan.models.AddCartProductItemModel;
import com.addukkan.models.AppLocalSettings;
import com.addukkan.models.CartDataModel;
import com.addukkan.models.ResponseModel;
import com.addukkan.models.SingleProductModel;
import com.addukkan.models.SliderDataModel;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.remote.Api;
import com.addukkan.tags.Tags;
import com.addukkan.uis.activity_home.HomeActivity;
import com.addukkan.uis.activity_my_favorite.MyFavoriteActivity;
import com.addukkan.uis.activity_product_detials.ProductDetialsActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentOffer extends Fragment {
    private FragmentOffersBinding binding;
    private HomeActivity activity;
    private SliderAdapter sliderAdapter;
    private Timer timer;
    private TimerTask timerTask;
    private Preferences preferences;
    private UserModel userModel;
    private AppLocalSettings settings;
    private List<SingleProductModel> productModelList;
    private ProductOfferAdapter productOfferAdapter;
    private String lang;
    private String country_coude;

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
        productModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        settings = preferences.isLanguageSelected(activity);
        userModel = preferences.getUserData(activity);
        if (userModel != null) {
            country_coude = userModel.getData().getCountry_code();
        } else {
            country_coude = settings.getCountry_code();
        }
        binding.progBarSlider.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        productOfferAdapter = new ProductOfferAdapter(productModelList, activity, this);
        binding.recView.setLayoutManager(new GridLayoutManager(activity, 2));
        binding.recView.setAdapter(productOfferAdapter);
        //binding.tab.setupWithViewPager(binding.pager);
        get_slider();
        getOffer();
        binding.swipeRefresh.setOnRefreshListener(() -> {
            get_slider();
            getOffer();
        });

    }

    private void get_slider() {
        String country_coude;
        if (userModel != null) {
            country_coude = userModel.getData().getCountry_code();
        } else {
            country_coude = settings.getCountry_code();
        }
        binding.progBarSlider.setVisibility(View.VISIBLE);
        binding.pager.setVisibility(View.GONE);
        Api.getService(Tags.base_url).get_slider(lang, "offer", country_coude).enqueue(new Callback<SliderDataModel>() {
            @Override
            public void onResponse(Call<SliderDataModel> call, Response<SliderDataModel> response) {
                binding.swipeRefresh.setRefreshing(false);
                binding.progBarSlider.setVisibility(View.GONE);
                binding.pager.setVisibility(View.VISIBLE);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    if (response.body() != null && response.body().getStatus() == 200 && response.body().getData() != null) {
                        // binding.flslider.setVisibility(View.VISIBLE);
                        sliderAdapter = new SliderAdapter(response.body().getData(), activity);
                        binding.pager.setAdapter(sliderAdapter);
                        if (response.body().getData().size() > 1) {
                            Log.e("ldkdkdkjk", "lkjjdjjd");
                            timer = new Timer();
                            timerTask = new MyTask();
                            timer.scheduleAtFixedRate(timerTask, 6000, 6000);
                        } else {
                            //  binding.flslider.setVisibility(View.GONE);
                        }


                    } else {

                        binding.pager.setVisibility(View.GONE);
                    }
                } else if (response.code() == 404) {
                    binding.pager.setVisibility(View.GONE);
                } else {
                    binding.pager.setVisibility(View.GONE);
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SliderDataModel> call, Throwable t) {
                try {
                    binding.swipeRefresh.setRefreshing(false);
                    binding.progBarSlider.setVisibility(View.GONE);
                    binding.pager.setVisibility(View.GONE);

                    Log.e("Error", t.getMessage());

                } catch (Exception e) {
                    Log.e("Error", e.toString());

                }

            }
        });

    }

    public int like_dislike(SingleProductModel productModel, int pos, int i) {
        // Log.e("lslsl",productModel.getMain_image());
        if (userModel != null) {
            try {
                // Log.e("llll", userModel.getUser().getToken());

                Api.getService(Tags.base_url)
                        .addFavoriteProduct("Bearer " + userModel.getData().getToken(), userModel.getData().getId() + "", productModel.getId() + "")
                        .enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                //  Log.e("dlldl",response.body().getStatus()+"");
                                if (response.isSuccessful() && response.body().getStatus() == 200) {

                                    getOffer();
                                } else {


                                    if (response.code() == 500) {
                                        //      Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                    } else {
                                        //       Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                        try {

                                            Log.e("error", response.code() + "_" + response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                try {

                                    if (t.getMessage() != null) {
                                        Log.e("error", t.getMessage());
                                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                            //            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                        } else {
                                            //          Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                } catch (Exception e) {
                                }
                            }
                        });
            } catch (Exception e) {
            }
            return 1;
        } else {
            activity.navigateToSignInActivity();
            // Common.CreateDialogAlert(activity, getString(R.string.please_sign_in_or_sign_up));
            return 0;

        }
    }

    public class MyTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                int current_page = binding.pager.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);

                }
            });

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }

    }

    private void getOffer() {
        String user_id = null;
        if (userModel != null) {
            user_id = userModel.getData().getId() + "";
        }
        binding.progBar.setVisibility(View.VISIBLE);
        productModelList.clear();
        productOfferAdapter.notifyDataSetChanged();
        // Log.e("sllsks", user_id + lang + country_coude);
        Api.getService(Tags.base_url)
                .getOffers(lang, user_id, country_coude, "off")
                .enqueue(new Callback<ALLProductDataModel>() {
                    @Override
                    public void onResponse(Call<ALLProductDataModel> call, Response<ALLProductDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {

                                productModelList.clear();
                                productModelList.addAll(response.body().getData());


                                if (productModelList.size() > 0) {
                                    productOfferAdapter.notifyDataSetChanged();

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

    public void additemtoCart(SingleProductModel data, OfferProductRowBinding binding) {
        if (userModel != null) {
            binding.progBar.setVisibility(View.VISIBLE);
            AddCartDataModel addCartDataModel = new AddCartDataModel();
            List<AddCartProductItemModel> addCartProductItemModelList = new ArrayList<>();
            AddCartProductItemModel addCartProductItemModel = new AddCartProductItemModel();
            addCartDataModel.setCountry_code(country_coude);
            addCartDataModel.setUser_id(userModel.getData().getId());
            double totalprice = 0;
            if (data.getHave_offer().equals("yes")) {
                if (data.getOffer_type().equals("value")) {
                    totalprice = data.getProduct_default_price().getPrice() - data.getOffer_value();
                } else if (data.getOffer_type().equals("per")) {
                    totalprice = (data.getProduct_default_price().getPrice()) - ((data.getProduct_default_price().getPrice() * data.getOffer_value()) / 100);
                } else {
                    totalprice = data.getProduct_default_price().getPrice();

                }
            } else {
                totalprice = data.getProduct_default_price().getPrice();
            }
            addCartDataModel.setTotal_price(totalprice);
            addCartProductItemModel.setAmount(1);
            addCartProductItemModel.setHave_offer(data.getHave_offer());
            addCartProductItemModel.setOffer_bonus(data.getOffer_bonus());
            addCartProductItemModel.setOffer_min(data.getOffer_min());
            addCartProductItemModel.setOffer_type(data.getOffer_type());
            addCartProductItemModel.setOld_price(data.getProduct_default_price().getPrice());
            addCartProductItemModel.setPrice(totalprice);
            addCartProductItemModel.setProduct_id(data.getId() + "");
            addCartProductItemModel.setOffer_value(data.getOffer_value());
            addCartProductItemModel.setProduct_price_id(data.getProduct_default_price().getId() + "");
            addCartProductItemModel.setVendor_id(data.getVendor_id() + "");
            addCartProductItemModelList.add(addCartProductItemModel);
            addCartDataModel.setCart_products(addCartProductItemModelList);
            addTocart(addCartDataModel, binding);
        } else {
            activity.navigateToSignInActivity();
        }
    }

    private void addTocart(AddCartDataModel addCartDataModel, OfferProductRowBinding binding) {
        binding.imgIncrease.setClickable(false);

        //   Log.e("sllsks", user_id + lang + country_coude);
        Api.getService(Tags.base_url)
                .createCart("Bearer " + userModel.getData().getToken(), addCartDataModel)
                .enqueue(new Callback<CartDataModel>() {
                    @Override
                    public void onResponse(Call<CartDataModel> call, Response<CartDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.imgIncrease.setClickable(true);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                binding.tvCounter.setText((Integer.parseInt(binding.tvCounter.getText().toString()) + 1) + "");
                                activity.binding.setCartCount(response.body().getData().getDetails().size() + "");


                            }
                        } else {

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
                    public void onFailure(Call<CartDataModel> call, Throwable t) {
                        try {
                            binding.imgIncrease.setClickable(true);
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


    public void setItemData(String id){
        Intent intent = new Intent(activity, ProductDetialsActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent,100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode== Activity.RESULT_OK){
            getOffer();
            activity.updateFragmentHome();
        }
    }
}
