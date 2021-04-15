package com.addukkan.uis.activity_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.addukkan.R;
import com.addukkan.databinding.ActivityHomeBinding;
import com.addukkan.language.Language;
import com.addukkan.preferences.Preferences;
import com.addukkan.uis.activity_home.fragments.FragmenDukkan;
import com.addukkan.uis.activity_home.fragments.FragmentHome;
import com.addukkan.uis.activity_home.fragments.FragmentOffer;
import com.addukkan.uis.activity_home.fragments.FragmentProfile;

import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private String lang = "";
    private Preferences preferences;
    private FragmentManager fragmentManager;
    private FragmentHome fragmentHome;
    private FragmenDukkan fragmenDukkan;
    private FragmentOffer fragmentOffer;
    private FragmentProfile fragmentProfile;
    private ActionBarDrawerToggle toggle;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        toggle = new ActionBarDrawerToggle(this,binding.drawerLayout,binding.toolBar,R.string.open,R.string.close);
        toggle.syncState();
        binding.toolBar.setNavigationIcon(R.drawable.ic_toolbar_nav_icon);
        updateCartCount(0);

        displayFragmentHome();
        binding.llHome.setOnClickListener(v -> displayFragmentHome());
        binding.llDukkan.setOnClickListener(v -> displayFragmentDukkan());
        binding.llOffer.setOnClickListener(v -> displayFragmentOffers());
        binding.llProfile.setOnClickListener(v -> displayFragmentProfile());

    }

    private void updateCartCount(int count) {
        binding.setCartCount(String.valueOf(count));
    }


    public void displayFragmentHome() {
        if (fragmentHome == null) {
            fragmentHome = FragmentHome.newInstance();

        }


        if (fragmenDukkan != null && fragmenDukkan.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmenDukkan).commit();
        }

        if (fragmentOffer != null && fragmentOffer.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentOffer).commit();
        }
        if (fragmentProfile != null && fragmentProfile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentProfile).commit();
        }

        if (fragmentHome.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentHome).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentHome, "fragmentHome").commit();
        }

        updateNavUiHome();
    }

    private void displayFragmentDukkan() {
        if (fragmenDukkan == null) {
            fragmenDukkan = FragmenDukkan.newInstance();

        }


        if (fragmentHome != null && fragmentHome.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentHome).commit();
        }

        if (fragmentOffer != null && fragmentOffer.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentOffer).commit();
        }
        if (fragmentProfile != null && fragmentProfile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentProfile).commit();
        }

        if (fragmenDukkan.isAdded()) {
            fragmentManager.beginTransaction().show(fragmenDukkan).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmenDukkan, "fragmenDukkan").commit();
        }

        updateNavUiDukkan();
    }

    private void displayFragmentOffers() {

        if (fragmentOffer == null) {
            fragmentOffer = FragmentOffer.newInstance();

        }


        if (fragmentHome != null && fragmentHome.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentHome).commit();
        }

        if (fragmenDukkan != null && fragmenDukkan.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmenDukkan).commit();
        }
        if (fragmentProfile != null && fragmentProfile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentProfile).commit();
        }

        if (fragmentOffer.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentOffer).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentOffer, "fragmentOffer").commit();
        }

        updateNavUiOffer();
    }

    private void displayFragmentProfile() {
        if (fragmentProfile == null) {
            fragmentProfile = FragmentProfile.newInstance();

        }


        if (fragmentHome != null && fragmentHome.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentHome).commit();
        }

        if (fragmenDukkan != null && fragmenDukkan.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmenDukkan).commit();
        }
        if (fragmentOffer != null && fragmentOffer.isAdded()) {
            fragmentManager.beginTransaction().hide(fragmentOffer).commit();
        }

        if (fragmentProfile.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentProfile).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentProfile, "fragmentProfile").commit();
        }
        updateNavUiProfile();
    }

    private void updateNavUiHome() {
        binding.iconHome.setSaturation(1.0f);
        binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.iconStore.setSaturation(0.0f);
        binding.tvStore.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconOffer.setSaturation(0.0f);
        binding.tvOffer.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconProfile.setSaturation(0.0f);
        binding.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.gray9));

    }


    private void updateNavUiDukkan() {
        binding.iconStore.setSaturation(1.0f);
        binding.tvStore.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.iconHome.setSaturation(0.0f);
        binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconOffer.setSaturation(0.0f);
        binding.tvOffer.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconProfile.setSaturation(0.0f);
        binding.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.gray9));

    }


    private void updateNavUiOffer() {
        binding.iconOffer.setSaturation(1.0f);
        binding.tvOffer.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.iconHome.setSaturation(0.0f);
        binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconStore.setSaturation(0.0f);
        binding.tvStore.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconProfile.setSaturation(0.0f);
        binding.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.gray9));

    }

    private void updateNavUiProfile() {
        binding.iconProfile.setSaturation(1.0f);
        binding.tvProfile.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        binding.iconHome.setSaturation(0.0f);
        binding.tvHome.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconStore.setSaturation(0.0f);
        binding.tvStore.setTextColor(ContextCompat.getColor(this, R.color.gray9));

        binding.iconOffer.setSaturation(0.0f);
        binding.tvOffer.setTextColor(ContextCompat.getColor(this, R.color.gray9));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onBackPressed() {
        if (fragmentHome != null && fragmentHome.isAdded() && fragmentHome.isVisible()) {

            finish();
        } else {
            displayFragmentHome();
        }
    }
}