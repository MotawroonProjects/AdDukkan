package com.addukkan.uis.activity_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.addukkan.R;
import com.addukkan.adapters.SideMenuCategoryAdapter;
import com.addukkan.adapters.SideMenuSubCategoryAdapter;
import com.addukkan.databinding.ActivityHomeBinding;
import com.addukkan.language.Language;
import com.addukkan.models.MainCategoryDataModel;
import com.addukkan.models.ResponseModel;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.remote.Api;
import com.addukkan.share.Common;
import com.addukkan.tags.Tags;
import com.addukkan.uis.activity_home.fragments.FragmenDukkan;
import com.addukkan.uis.activity_home.fragments.FragmentHome;
import com.addukkan.uis.activity_home.fragments.FragmentOffer;
import com.addukkan.uis.activity_home.fragments.FragmentProfile;
import com.addukkan.uis.activity_login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private String lang = "";
    private Preferences preferences;
    private UserModel userModel;
    private FragmentManager fragmentManager;
    private FragmentHome fragmentHome;
    private FragmenDukkan fragmenDukkan;
    private FragmentOffer fragmentOffer;
    private FragmentProfile fragmentProfile;
    private ActionBarDrawerToggle toggle;
    private List<MainCategoryDataModel.Data> categoryDataModelDataList;
    private SideMenuCategoryAdapter sideMenuSubCategoryAdapter;


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
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        categoryDataModelDataList=new ArrayList<>();
        sideMenuSubCategoryAdapter=new SideMenuCategoryAdapter(categoryDataModelDataList,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(sideMenuSubCategoryAdapter);
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

        updateTokenFireBase();
        getSideMenu();

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

    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }

    private void updateTokenFireBase()
    {
        if (userModel!=null){
            try {
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        String token = task.getResult().getToken();
                        Api.getService(Tags.base_url)
                                .updateFirebaseToken("Bearer "+userModel.getData().getToken(),userModel.getData().getId(),token,"android")
                                .enqueue(new Callback<ResponseModel>() {
                                    @Override
                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                        if (response.isSuccessful() && response.body() != null&&response.body().getStatus()==200) {
                                            userModel.getData().setFirebase_token(token);
                                            preferences.create_update_userdata(HomeActivity.this,userModel);

                                        } else {
                                            try {

                                                Log.e("errorToken", response.code() + "_" + response.errorBody().string());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                                        try {

                                            if (t.getMessage() != null) {
                                                Log.e("errorToken2", t.getMessage());
                                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                    //Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                                } else {
                                                   // Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        } catch (Exception e) {
                                        }
                                    }
                                });
                    }
                });


            } catch (Exception e) {


            }
        }
    }

    public void logout()
    {
        if (userModel==null){
            return;
        }
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .logout("Bearer "+userModel.getData().getToken(),userModel.getData().getId(),userModel.getData().getFirebase_token(),"android")
                .enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus()==200){
                                    userModel = null;
                                    preferences.clear(HomeActivity.this);
                                    if (fragmentProfile!=null&&fragmentProfile.isAdded()){
                                        fragmentProfile.updateUserData();
                                    }
                                }else {
                                }
                            }


                        } else {

                            try {
                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("errorToken2", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                     Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });


    }
    public void navigateToSignInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void getSideMenu() {

        Api.getService(Tags.base_url)
                .getSideMenu(lang)
                .enqueue(new Callback<MainCategoryDataModel>() {
                    @Override
                    public void onResponse(Call<MainCategoryDataModel> call, Response<MainCategoryDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {

                                categoryDataModelDataList.clear();
                                categoryDataModelDataList.addAll(response.body().getData());

                                if (categoryDataModelDataList.size() > 0) {
                                    sideMenuSubCategoryAdapter.notifyDataSetChanged();

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
                    public void onFailure(Call<MainCategoryDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

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