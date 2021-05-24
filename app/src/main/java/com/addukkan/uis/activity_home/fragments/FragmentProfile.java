package com.addukkan.uis.activity_home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.addukkan.R;
import com.addukkan.databinding.FragmentHomeBinding;
import com.addukkan.databinding.FragmentProfileBinding;
import com.addukkan.interfaces.Listeners;
import com.addukkan.models.NotificationCountModel;
import com.addukkan.models.SignUpModel;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.remote.Api;
import com.addukkan.tags.Tags;
import com.addukkan.uis.activity_countries.CountryActivity;
import com.addukkan.uis.activity_home.HomeActivity;
import com.addukkan.uis.activity_language.LanguageActivity;
import com.addukkan.uis.activity_login.LoginActivity;
import com.addukkan.uis.activity_my_favorite.MyFavoriteActivity;
import com.addukkan.uis.activity_notification.NotificationActivity;
import com.addukkan.uis.activity_sign_up.SignUpActivity;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends Fragment implements Listeners.ProfileActions {
    private FragmentProfileBinding binding;
    private HomeActivity activity;
    private String lang = "ar";
    private UserModel userModel;
    private Preferences preferences;


    public static FragmentProfile newInstance() {
        return new FragmentProfile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
        binding.setActions(this);
        binding.iconEdit.setOnClickListener(v -> {
            Intent intent = new Intent(activity, SignUpActivity.class);
            startActivityForResult(intent, 100);
        });
        binding.setNotcount("0");
        if (userModel != null) {
            getNotificationCount();
        }
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(activity, LoginActivity.class);
        startActivityForResult(intent, 100);
    }

    public void updateUserData() {
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
    }


    @Override
    public void onOrder() {
        userModel = preferences.getUserData(activity);
        if (userModel == null) {
            navigateToLoginActivity();
        } else {

        }
    }

    @Override
    public void onFavorite() {
        userModel = preferences.getUserData(activity);
        if (userModel == null) {
            navigateToLoginActivity();
        } else {
            Intent intent = new Intent(activity, MyFavoriteActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onChat() {
        userModel = preferences.getUserData(activity);
        if (userModel == null) {
            navigateToLoginActivity();
        } else {

        }
    }

    @Override
    public void onChangeLanguage() {
        Intent intent = new Intent(activity, LanguageActivity.class);
        startActivityForResult(intent, 200);
    }

    @Override
    public void onCountry() {
        userModel = preferences.getUserData(activity);
        if (userModel == null) {
            navigateToLoginActivity();
        } else {
            Intent intent = new Intent(activity, CountryActivity.class);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    public void onAboutApp() {

    }

    @Override
    public void onChatWithAdmin() {
        userModel = preferences.getUserData(activity);
        if (userModel == null) {
            navigateToLoginActivity();
        } else {

        }
    }

    @Override
    public void onTerms() {

    }

    @Override
    public void onContactUs() {

    }

    @Override
    public void onFacebook() {

    }

    @Override
    public void onTwitter() {

    }

    @Override
    public void onInstagram() {

    }

    @Override
    public void onWhatsApp() {

    }

    @Override
    public void onLogout() {
        activity.logout();
    }

    @Override
    public void onNotification() {
        if (userModel == null) {
            navigateToLoginActivity();
        } else {
            binding.setNotcount("0");
            Intent intent = new Intent(activity, NotificationActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            updateUserData();
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK && data != null) {
            String lang = data.getStringExtra("lang");
            activity.refreshActivity(lang);
        }
    }

    private void getNotificationCount() {
        if (userModel == null) {
            binding.setNotcount("0");

            return;
        }
        Api.getService(Tags.base_url).getNotificationCount(userModel.getData().getToken(), userModel.getData().getId())
                .enqueue(new Callback<NotificationCountModel>() {
                    @Override
                    public void onResponse(Call<NotificationCountModel> call, Response<NotificationCountModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                binding.setNotcount(String.valueOf(response.body().getData().getCount()));
                            }
                        } else {

                            try {
                                Log.e("error", response.code() + "_" + response.errorBody().string());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationCountModel> call, Throwable t) {
                        try {

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                            }


                        } catch (Exception e) {
                        }

                    }
                });
    }

}
