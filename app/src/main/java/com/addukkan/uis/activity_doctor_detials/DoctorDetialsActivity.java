package com.addukkan.uis.activity_doctor_detials;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.addukkan.R;
import com.addukkan.adapters.DoctorsAdapter;
import com.addukkan.adapters.RateAdapter;
import com.addukkan.databinding.ActivityDoctorDetialsBinding;
import com.addukkan.interfaces.Listeners;
import com.addukkan.language.Language;
import com.addukkan.models.ChatRoomModel;
import com.addukkan.models.CreateRoomModel;
import com.addukkan.models.DoctorsDataModel;
import com.addukkan.models.ResponseModel;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.remote.Api;
import com.addukkan.share.Common;
import com.addukkan.tags.Tags;
import com.addukkan.uis.FragmentMapTouchListener;
import com.addukkan.uis.activity_chat.ChatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDetialsActivity extends AppCompatActivity implements Listeners.BackListener , OnMapReadyCallback {
    private ActivityDoctorDetialsBinding binding;
    private UserModel.Data doctorModel;
    private RateAdapter adapter;
    private String lang;
    private GoogleMap mMap;
    private Marker marker;
    private float zoom = 15.0f;
    private FragmentMapTouchListener fragment;
    private Preferences preferences;
    private UserModel userModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_detials);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        doctorModel = (UserModel.Data) intent.getSerializableExtra("data");

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        binding.btnAsk.setOnClickListener(v -> {
            createRoom();
        });
        getDoctorById();


    }

    private void createRoom() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .createRoom("Bearer "+userModel.getData().getToken(),userModel.getData().getId(),doctorModel.getId())
                .enqueue(new Callback<CreateRoomModel>() {
                    @Override
                    public void onResponse(Call<CreateRoomModel> call, Response<CreateRoomModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                ChatRoomModel chatRoomModel = new ChatRoomModel(response.body().getData().getId(),doctorModel.getId(),doctorModel.getLogo(),doctorModel.getName());
                                Intent intent = new Intent(DoctorDetialsActivity.this, ChatActivity.class);
                                intent.putExtra("data", chatRoomModel);
                                startActivity(intent);
                            }
                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                // Toast.makeText(SignUpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CreateRoomModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    // Toast.makeText(SignUpActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(SignUpActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void getDoctorById() {
        Api.getService(Tags.base_url)
                .getDoctorById(doctorModel.getId())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            Log.e("code", response.body().getStatus()+"__");

                            if (response.body() != null && response.body().getStatus() == 200) {

                                if (response.body().getData() != null) {
                                    updateUi(response.body());
                                }
                            }


                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            switch (response.code()) {
                                case 500:
                                    break;
                                default:
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void updateUi(UserModel body) {
        updateMapUI();
        doctorModel = body.getData();
        binding.setModel(doctorModel);
        binding.llData.setVisibility(View.VISIBLE);
        binding.progBar.setVisibility(View.GONE);
        if (doctorModel.getUser_rates().size()>0){
            binding.tvNoData.setVisibility(View.GONE);
            adapter = new RateAdapter(doctorModel.getUser_rates(),this);
            binding.recView.setAdapter(adapter);
        }else {
            binding.tvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void updateMapUI() {

        fragment = (FragmentMapTouchListener) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);



    }

    @Override
    public void back() {
        finish();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            fragment.setListener(()->binding.scrollView.requestDisallowInterceptTouchEvent(true));
            AddMarker(doctorModel.getLatitude(), doctorModel.getLongitude());

        }

    }

    private void AddMarker(double lat, double lng) {


        if (marker == null) {
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        } else {
            marker.setPosition(new LatLng(lat, lng));


        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
    }

}
