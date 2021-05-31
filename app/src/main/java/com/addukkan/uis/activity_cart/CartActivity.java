package com.addukkan.uis.activity_cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.addukkan.R;
import com.addukkan.adapters.CartProductAdapter;
import com.addukkan.adapters.FavouriteProductAdapter;
import com.addukkan.databinding.ActivityCartBinding;
import com.addukkan.databinding.CartProductRowBinding;
import com.addukkan.databinding.FavouriteProductRowBinding;
import com.addukkan.interfaces.Listeners;
import com.addukkan.language.Language;
import com.addukkan.models.AddCartDataModel;
import com.addukkan.models.AddCartProductItemModel;
import com.addukkan.models.AppLocalSettings;
import com.addukkan.models.CartDataModel;
import com.addukkan.models.CartDataModel;
import com.addukkan.models.CouponDataModel;
import com.addukkan.models.ResponseModel;
import com.addukkan.models.SingleProductModel;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.remote.Api;
import com.addukkan.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCartBinding binding;
    private String lang;

    private LinearLayoutManager manager;
    private UserModel userModel;
    private Preferences preferences;

    private List<CartDataModel.Data.Detials> detialsList;
    private CartProductAdapter cartProductAdapter;
    private String country_coude;
    private AppLocalSettings settings;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        initView();
    }


    private void initView() {
        detialsList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        preferences = Preferences.getInstance();
        settings = preferences.isLanguageSelected(this);
        userModel = preferences.getUserData(this);
        if (userModel != null) {
            country_coude = userModel.getData().getCountry_code();
        } else {
            country_coude = settings.getCountry_code();
        }

        manager = new GridLayoutManager(this, 1);
        binding.recView.setLayoutManager(manager);
        cartProductAdapter = new CartProductAdapter(detialsList, this);
        binding.recView.setAdapter(cartProductAdapter);
        binding.btcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String copun=binding.edtCopun.getText().toString();
                if(!copun.isEmpty()){
                    binding.edtCopun.setError(null);
                checkCoupon(copun);}
                else {
                    binding.edtCopun.setError(getResources().getString(R.string.field_required));
                }
            }
        });
//        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//                    int total = binding.recView.getAdapter().getItemCount();
//
//                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
//
//
//                    if (total > 6 && (total - lastVisibleItem) == 2 && !isLoading) {
//                        isLoading = true;
//                        int page = current_page + 1;
//                        productModelList.add(null);
//                        adapter.notifyDataSetChanged();
//                        loadMore(page);
//                    }
//                }
//            }
//        });*/
        getData();
    }


    public void getData() {

        String user_id = null;
        if (userModel != null) {
            user_id = userModel.getData().getId() + "";
        }

        binding.progBar.setVisibility(View.VISIBLE);
        detialsList.clear();
        cartProductAdapter.notifyDataSetChanged();
        Log.e("sllsks", user_id + "" + lang + "Bearer " + userModel.getData().getToken());
        Api.getService(Tags.base_url)
                .getMyCart("Bearer " + userModel.getData().getToken(), lang, user_id)
                .enqueue(new Callback<CartDataModel>() {
                    @Override
                    public void onResponse(Call<CartDataModel> call, Response<CartDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);

                        //     Log.e("Dldldl",response.message());
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {

                                detialsList.clear();
                                if (response.body().getData() != null && response.body().getData().getDetails() != null) {
                                    detialsList.addAll(response.body().getData().getDetails());
                                    binding.setModel(response.body().getData());
                                }else {
                                    binding.llContainer.setVisibility(View.GONE);
                                }


                                if (detialsList.size() > 0) {
                                    cartProductAdapter.notifyDataSetChanged();

                                    binding.tvNoData.setVisibility(View.GONE);
                                    //Log.e(",dkdfkfkkfk", categoryDataModelDataList.get(0).getTitle());
                                } else {
                                    binding.tvNoData.setVisibility(View.VISIBLE);

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
                    public void onFailure(Call<CartDataModel> call, Throwable t) {
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
    private void checkCoupon(String coupon_num) {
binding.progBarcopun.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url).checkCoupon("Bearer " + userModel.getData().getToken(),userModel.getData().getId()+"",coupon_num)
                .enqueue(new Callback<CouponDataModel>() {
                    @Override
                    public void onResponse(Call<CouponDataModel> call, Response<CouponDataModel> response) {
                        binding.progBarcopun.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                               //     Toast.makeText(SignUpAdvisorActivity.this, R.string.coupon_vaild, Toast.LENGTH_SHORT).show();
UpdateData(response.body());


                                } else if (response.body().getStatus() == 406) {


                                    Toast.makeText(CartActivity.this, R.string.expierd, Toast.LENGTH_SHORT).show();
                                }
                                else if (response.body().getStatus() == 404) {


                                    Toast.makeText(CartActivity.this, R.string.not_found, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            binding.progBarcopun.setVisibility(View.GONE);
                            if (response.code() == 422) {
                           //     Toast.makeText(SignUpAdvisorActivity.this, R.string.inv_coupon, Toast.LENGTH_SHORT).show();


                            }
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<CouponDataModel> call, Throwable t) {
                        try {
                            binding.progBarcopun.setVisibility(View.GONE);

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //Toast.makeText(SignUpAdvisorActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                   // Toast.makeText(SignUpAdvisorActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void UpdateData(CouponDataModel body) {
        if(body.getData().getType().equals("value")){
            binding.tvtotal.setText(getResources().getString(R.string.total)+Math.round((Double.parseDouble(binding.tvtotal.getText().toString().replaceAll(getResources().getString(R.string.total),""))-body.getData().getDiscount_val())));
        }
        else {
            binding.tvtotal.setText(getResources().getString(R.string.total)+Math.round((Double.parseDouble(binding.tvtotal.getText().toString().replaceAll(getResources().getString(R.string.total),""))-(body.getData().getDiscount_val()*Double.parseDouble(binding.tvtotal.getText().toString().replaceAll(getResources().getString(R.string.total),""))/100))));

        }
    }


    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void back() {
        finish();
    }


    private void addTocart(CartDataModel.Data.Detials addCartDataModel, CartProductRowBinding binding, String increment) {

        binding.imgIncrease.setClickable(false);
        binding.imgDecrease.setClickable(false);
        Api.getService(Tags.base_url)
                .incrementDecrementCart("Bearer " + userModel.getData().getToken(), country_coude, userModel.getData().getId() + "", addCartDataModel.getId() + "", addCartDataModel.getCart_id() + "", 1 + "", increment)
                .enqueue(new Callback<CartDataModel>() {
                    @Override
                    public void onResponse(Call<CartDataModel> call, Response<CartDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.imgIncrease.setClickable(true);
                        binding.imgDecrease.setClickable(true);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                detialsList.clear();
                                if (response.body().getData() != null && response.body().getData().getDetails() != null) {
                                    detialsList.addAll(response.body().getData().getDetails());
                                }
                                cartProductAdapter.notifyDataSetChanged();
                                if (detialsList.size() == 0) {
                                    CartActivity.this.binding.tvNoData.setVisibility(View.VISIBLE);
                                } else {
                                    CartActivity.this.binding.tvNoData.setVisibility(View.GONE);
                                }

//                                if(increment.equals("increment")) {
//                                    binding.tvCounter.setText((Integer.parseInt(binding.tvCounter.getText().toString()) + 1) + "");
//                                }
//                                else {
//                                    binding.tvCounter.setText((Integer.parseInt(binding.tvCounter.getText().toString()) - 1) + "");
//
//                                }
//                                binding.tvtotal.setText((Integer.parseInt(binding.tvCounter.getText().toString())*addCartDataModel.getPrice())+"");
//if(addCartDataModel.getHave_offer().equals("yes")){
//    if(addCartDataModel.getOffer_type().equals("amount")){
//
//    }
//}
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
                            binding.imgDecrease.setClickable(true);
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

    public void additemtoCart(CartDataModel.Data.Detials detials, CartProductRowBinding binding, String increment) {
        binding.progBar.setVisibility(View.VISIBLE);
        addTocart(detials, binding, increment);
    }

    public void deleteItemFromcart(CartDataModel.Data.Detials addCartDataModel) {


        Api.getService(Tags.base_url)
                .deleteItemCart("Bearer " + userModel.getData().getToken(), addCartDataModel.getId() + "", addCartDataModel.getCart_id() + "")
                .enqueue(new Callback<CartDataModel>() {
                    @Override
                    public void onResponse(Call<CartDataModel> call, Response<CartDataModel> response) {

                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                detialsList.clear();
                                if (response.body().getData() != null && response.body().getData().getDetails() != null) {
                                    detialsList.addAll(response.body().getData().getDetails());
                                }
                                cartProductAdapter.notifyDataSetChanged();
                                if (detialsList.size() == 0) {
                                    binding.tvNoData.setVisibility(View.VISIBLE);
                                } else {
                                    binding.tvNoData.setVisibility(View.GONE);
                                }

//                                if(increment.equals("increment")) {
//                                    binding.tvCounter.setText((Integer.parseInt(binding.tvCounter.getText().toString()) + 1) + "");
//                                }
//                                else {
//                                    binding.tvCounter.setText((Integer.parseInt(binding.tvCounter.getText().toString()) - 1) + "");
//
//                                }
//                                binding.tvtotal.setText((Integer.parseInt(binding.tvCounter.getText().toString())*addCartDataModel.getPrice())+"");
//if(addCartDataModel.getHave_offer().equals("yes")){
//    if(addCartDataModel.getOffer_type().equals("amount")){
//
//    }
//}
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

}
