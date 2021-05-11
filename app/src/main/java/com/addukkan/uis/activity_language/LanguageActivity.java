package com.addukkan.uis.activity_language;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.addukkan.R;
import com.addukkan.databinding.ActivityLanguageBinding;
import com.addukkan.language.Language;
import com.addukkan.models.AppLocalSettings;
import com.addukkan.models.SelectedLocation;
import com.addukkan.preferences.Preferences;
import com.addukkan.uis.activity_map.MapActivity;

import io.paperdb.Paper;

public class LanguageActivity extends AppCompatActivity {
    private ActivityLanguageBinding binding;
    private String lang = "";
    private Animation animation, animation2,animation3;
    private boolean isFromSplash = false;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_language);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        isFromSplash = intent.getBooleanExtra("data", false);
    }

    private void initView() {
        animation = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.scale_down_anim);
        animation3 = AnimationUtils.loadAnimation(this, R.anim.translate);

        preferences = Preferences.getInstance();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");

        if (lang.equals("ar")) {
            binding.flAr.setBackgroundResource(R.drawable.small_stroke_primary);
            binding.flEn.setBackgroundResource(0);

        } else {
            binding.flAr.setBackgroundResource(0);
            binding.flEn.setBackgroundResource(R.drawable.small_stroke_primary);

        }
        binding.btnNext.setVisibility(View.VISIBLE);

        binding.cardAr.setOnClickListener(view -> {
            lang = "ar";
            binding.flAr.setBackgroundResource(R.drawable.small_stroke_primary);
            binding.flEn.setBackgroundResource(0);
            binding.btnNext.setVisibility(View.VISIBLE);

        });

        binding.cardEn.setOnClickListener(view -> {
            lang = "en";
            binding.flAr.setBackgroundResource(0);
            binding.flEn.setBackgroundResource(R.drawable.small_stroke_primary);
            binding.btnNext.setVisibility(View.VISIBLE);
        });

        binding.btnNext.setOnClickListener(view -> {
            if (isFromSplash){
                Intent intent = getIntent();
                intent.putExtra("lang", lang);
                setResult(RESULT_OK, intent);
                finish();
            }else {
                Intent intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent,100);
            }

        });

        if (isFromSplash){
            binding.logo2.startAnimation(animation);

        }else {
            binding.consData.setVisibility(View.VISIBLE);
            binding.logo2.setVisibility(View.GONE);
            binding.logo.setVisibility(View.VISIBLE);
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.consData.setVisibility(View.GONE);
                binding.logo2.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.logo2.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.consData.setVisibility(View.VISIBLE);
                binding.logo2.setVisibility(View.GONE);
                binding.logo.startAnimation(animation3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.logo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK&&data!=null){
            SelectedLocation location = (SelectedLocation) data.getSerializableExtra("location");
            AppLocalSettings settings = preferences.isLanguageSelected(this);
            if (settings==null){
                settings = new AppLocalSettings();

            }

            settings.setAddress(location.getAddress());
            settings.setLat(location.getLat());
            settings.setLng(location.getLng());
            preferences.setIsLanguageSelected(this,settings);
        }
    }
}