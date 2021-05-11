package com.addukkan.uis.activity_home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.addukkan.models.SignUpModel;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.uis.activity_countries.CountryActivity;
import com.addukkan.uis.activity_home.HomeActivity;
import com.addukkan.uis.activity_language.LanguageActivity;
import com.addukkan.uis.activity_login.LoginActivity;
import com.addukkan.uis.activity_sign_up.SignUpActivity;

import io.paperdb.Paper;

public class FragmentProfile extends Fragment implements Listeners.ProfileActions {
    private FragmentProfileBinding binding;
    private HomeActivity activity;
    private String lang = "ar";
    private UserModel  userModel;
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
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        preferences  = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setActions(this);
        binding.iconEdit.setOnClickListener(v -> {
            Intent intent = new Intent(activity, SignUpActivity.class);
            startActivityForResult(intent,100);
        });


    }

    private void navigateToLoginActivity(){
        Intent intent = new Intent(activity, LoginActivity.class);
        startActivityForResult(intent,100);
    }

    public void updateUserData(){
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
    }


    @Override
    public void onOrder() {
        userModel = preferences.getUserData(activity);
        if (userModel==null){
            navigateToLoginActivity();
        }else {

        }
    }

    @Override
    public void onFavorite() {
        userModel = preferences.getUserData(activity);
        if (userModel==null){
            navigateToLoginActivity();
        }else {

        }
    }

    @Override
    public void onChat() {
        userModel = preferences.getUserData(activity);
        if (userModel==null){
            navigateToLoginActivity();
        }else {

        }
    }

    @Override
    public void onChangeLanguage() {
        Intent intent = new Intent(activity, LanguageActivity.class);
        startActivityForResult(intent,200);
    }

    @Override
    public void onCountry() {
        userModel = preferences.getUserData(activity);
        if (userModel==null){
            navigateToLoginActivity();
        }else {
            Intent intent = new Intent(activity, CountryActivity.class);
            startActivityForResult(intent,100);
        }
    }

    @Override
    public void onAboutApp() {

    }

    @Override
    public void onChatWithAdmin() {
        userModel = preferences.getUserData(activity);
        if (userModel==null){
            navigateToLoginActivity();
        }else {

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode== Activity.RESULT_OK){
            updateUserData();
        }else if (requestCode==200&&resultCode== Activity.RESULT_OK&&data!=null){
            String lang = data.getStringExtra("lang");
            activity.refreshActivity(lang);
        }
    }
}
