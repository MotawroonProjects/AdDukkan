package com.addukkan.uis.activity_home.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.addukkan.R;
import com.addukkan.adapters.CategoryAdapter;
import com.addukkan.adapters.MainCategoryAdapter;
import com.addukkan.adapters.Product2Adapter;
import com.addukkan.adapters.SliderAdapter;
import com.addukkan.databinding.DialogAlertBinding;
import com.addukkan.databinding.DialogRoshtaBinding;
import com.addukkan.databinding.FragmentHomeBinding;
import com.addukkan.models.ALLProductDataModel;
import com.addukkan.models.AppLocalSettings;
import com.addukkan.models.MainCategoryDataModel;
import com.addukkan.models.ResponseModel;
import com.addukkan.models.SingleProductModel;
import com.addukkan.models.SliderDataModel;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.remote.Api;
import com.addukkan.tags.Tags;
import com.addukkan.uis.activity_ask_doctor.AskDoctorActivity;
import com.addukkan.uis.activity_home.HomeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {
    private FragmentHomeBinding binding;
    private HomeActivity activity;
    private SliderAdapter sliderAdapter;
    private Timer timer;
    private TimerTask timerTask;
    private Preferences preferences;
    private UserModel userModel;
    private List<MainCategoryDataModel.Data> categoryDataModelDataList, getCategoryDataModelDataList;
    private List<SingleProductModel> productModelList,productModelList2;
    private Product2Adapter product2Adapter,product2Adapter2;
    private CategoryAdapter categoryAdapter;
    private AppLocalSettings settings;
    private String lang = "";
    private MainCategoryAdapter mainCategoryAdapter;
    private String country_coude;

    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        categoryDataModelDataList = new ArrayList<>();
        getCategoryDataModelDataList = new ArrayList<>();
        productModelList = new ArrayList<>();
        productModelList2=new ArrayList<>();
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
        binding.cardaskdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AskDoctorActivity.class);
                startActivity(intent);
            }
        });
        binding.cardroshta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialogAlert(activity);
            }
        });
        binding.progBarCategory.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        binding.progBarSlider.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        binding.progBarrecent.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        binding.progBarmostsell.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        binding.tab.setupWithViewPager(binding.pager);

        categoryAdapter = new CategoryAdapter(categoryDataModelDataList, activity);
        mainCategoryAdapter = new MainCategoryAdapter(getCategoryDataModelDataList, activity,this);
        product2Adapter = new Product2Adapter(productModelList, activity,this,1);
        product2Adapter2 = new Product2Adapter(productModelList, activity,this,0);

        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewCategory.setAdapter(categoryAdapter);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(mainCategoryAdapter);
        binding.recViewrecent.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewrecent.setAdapter(product2Adapter);
        binding.recViewmostsell.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewmostsell.setAdapter(product2Adapter2);
        get_slider();
        getMainCategory();
        getMainCategorySubCategoryProduct();
        getRecentArrived();
        getMostSell();

    }

    private void getMainCategory() {
        Api.getService(Tags.base_url)
                .get_category(lang)
                .enqueue(new Callback<MainCategoryDataModel>() {
                    @Override
                    public void onResponse(Call<MainCategoryDataModel> call, Response<MainCategoryDataModel> response) {
                        binding.progBarCategory.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {

                                categoryDataModelDataList.clear();
                                categoryDataModelDataList.addAll(response.body().getData());

                                if (categoryDataModelDataList.size() > 0) {
                                    categoryAdapter.notifyDataSetChanged();
//                                binding.tvNoDatadepart.setVisibility(View.GONE);
                                    // Log.e(",dkdfkfkkfk", categoryDataModelDataList.get(0).getTitle());
                                } else {
//                                binding.tvNoDatadepart.setVisibility(View.VISIBLE);

                                }

                            }
                        } else {
                            binding.progBarCategory.setVisibility(View.GONE);

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
                    public void onFailure(Call<MainCategoryDataModel> call, Throwable t) {
                        try {
                            binding.progBarCategory.setVisibility(View.GONE);

                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                   // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
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

    private void getMainCategorySubCategoryProduct() {
        String user_id = null;
        if (userModel != null) {
            user_id = userModel.getData().getId() + "";
        }
        //Log.e("sllsks", user_id + lang + country_coude);
        Api.getService(Tags.base_url)
                .get_categorySubProduct(lang, user_id, country_coude)
                .enqueue(new Callback<MainCategoryDataModel>() {
                    @Override
                    public void onResponse(Call<MainCategoryDataModel> call, Response<MainCategoryDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {

                                getCategoryDataModelDataList.clear();
                                getCategoryDataModelDataList.addAll(response.body().getData());

                                if (getCategoryDataModelDataList.size() > 0) {
                                    mainCategoryAdapter.notifyDataSetChanged();

//                                binding.tvNoDatadepart.setVisibility(View.GONE);
                                    //Log.e(",dkdfkfkkfk", categoryDataModelDataList.get(0).getTitle());
                                } else {
//                                binding.tvNoDatadepart.setVisibility(View.VISIBLE);

                                }

                            }
                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            try {
                                Log.e("errorNotCode2", response.code() + "__" + response.errorBody().string());
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
                    public void onFailure(Call<MainCategoryDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                 //   Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
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

    private void getRecentArrived() {
        String user_id = null;
        if (userModel != null) {
            user_id = userModel.getData().getId() + "";
        }
        Log.e("sllsks", user_id + lang + country_coude);
        Api.getService(Tags.base_url)
                .getRecentlyArrived(lang, user_id, country_coude, "off")
                .enqueue(new Callback<ALLProductDataModel>() {
                    @Override
                    public void onResponse(Call<ALLProductDataModel> call, Response<ALLProductDataModel> response) {
                        binding.progBarrecent.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {

                                productModelList.clear();
                                productModelList.addAll(response.body().getData());

                                if (productModelList.size() > 0) {
                                    product2Adapter.notifyDataSetChanged();

//                                binding.tvNoDatadepart.setVisibility(View.GONE);
                                    //Log.e(",dkdfkfkkfk", categoryDataModelDataList.get(0).getTitle());
                                } else {
//                                binding.tvNoDatadepart.setVisibility(View.VISIBLE);

                                }

                            }
                        } else {
                            binding.progBarrecent.setVisibility(View.GONE);

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
                            binding.progBarrecent.setVisibility(View.GONE);

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
    private void getMostSell() {
        String user_id = null;
        if (userModel != null) {
            user_id = userModel.getData().getId() + "";
        }
        Log.e("sllsks", user_id + lang + country_coude);
        Api.getService(Tags.base_url)
                .getMostSell(lang, user_id, country_coude, "off")
                .enqueue(new Callback<ALLProductDataModel>() {
                    @Override
                    public void onResponse(Call<ALLProductDataModel> call, Response<ALLProductDataModel> response) {
                        binding.progBarmostsell.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {

                                productModelList2.clear();
                                productModelList2.addAll(response.body().getData());

                                if (productModelList2.size() > 0) {
                                    product2Adapter2.notifyDataSetChanged();

//                                binding.tvNoDatadepart.setVisibility(View.GONE);
                                    //Log.e(",dkdfkfkkfk", categoryDataModelDataList.get(0).getTitle());
                                } else {
//                                binding.tvNoDatadepart.setVisibility(View.VISIBLE);

                                }

                            }
                        } else {
                            binding.progBarmostsell.setVisibility(View.GONE);

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
                            binding.progBarmostsell.setVisibility(View.GONE);

                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                           //         Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
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

    private void get_slider() {

        binding.progBarSlider.setVisibility(View.VISIBLE);
        binding.pager.setVisibility(View.GONE);
        Api.getService(Tags.base_url).get_slider(lang, "public", country_coude).enqueue(new Callback<SliderDataModel>() {
            @Override
            public void onResponse(Call<SliderDataModel> call, Response<SliderDataModel> response) {
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
                    binding.progBarSlider.setVisibility(View.GONE);
                    binding.pager.setVisibility(View.GONE);

                    Log.e("Error", t.getMessage());

                } catch (Exception e) {
                    Log.e("Error", e.toString());

                }

            }
        });

    }

    public void CreateDialogAlert(Context context) {

        DialogRoshtaBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_roshta, null, false);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        // Grab the window of the dialog, and change the width
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        window.setGravity(Gravity.CENTER);
        binding.flclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // This makes the dialog take up the full width
        window.setAttributes(lp);
        dialog.show();
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
    public int like_dislike(SingleProductModel productModel, int pos, int i) {
        if (userModel != null) {
            try {
               // Log.e("llll", userModel.getUser().getToken());

                Api.getService(Tags.base_url)
                        .addFavoriteProduct("Bearer "+userModel.getData().getToken(),userModel.getData().getId()+"", productModel.getId() + "")
                        .enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                Log.e("dlldl",response.body().getStatus()+"");
                                if (response.isSuccessful()&&response.body().getStatus()==200) {
                                update();

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

    private void update() {
        getRecentArrived();
        getMostSell();
        getMainCategorySubCategoryProduct();
    }


}
