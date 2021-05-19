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
import com.addukkan.adapters.SliderAdapter;
import com.addukkan.databinding.DialogAlertBinding;
import com.addukkan.databinding.DialogRoshtaBinding;
import com.addukkan.databinding.FragmentHomeBinding;
import com.addukkan.models.AppLocalSettings;
import com.addukkan.models.MainCategoryDataModel;
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
    private List<MainCategoryDataModel.Data> categoryDataModelDataList;
    private CategoryAdapter categoryAdapter;
    private AppLocalSettings settings;

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
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        settings=preferences.isLanguageSelected(activity);
        userModel = preferences.getUserData(activity);
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
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarSlider.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.white), PorterDuff.Mode.SRC_IN);
        binding.tab.setupWithViewPager(binding.pager);
        categoryAdapter = new CategoryAdapter(categoryDataModelDataList, activity);

        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));

        binding.recViewCategory.setAdapter(categoryAdapter);
        get_slider();
        getMainCategory();

    }

    private void getMainCategory() {
        Api.getService(Tags.base_url)
                .get_category()
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
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
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
        Api.getService(Tags.base_url).get_slider("public", country_coude).enqueue(new Callback<SliderDataModel>() {
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

}
