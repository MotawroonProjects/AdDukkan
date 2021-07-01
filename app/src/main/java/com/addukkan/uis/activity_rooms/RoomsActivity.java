package com.addukkan.uis.activity_rooms;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.addukkan.R;
import com.addukkan.adapters.NotificationAdapter;
import com.addukkan.adapters.RoomAdapter;
import com.addukkan.databinding.ActivityNotificationBinding;
import com.addukkan.databinding.ActivityRoomBinding;
import com.addukkan.language.Language;
import com.addukkan.models.ChatRoomModel;
import com.addukkan.models.NotificationDataModel;
import com.addukkan.models.NotificationModel;
import com.addukkan.models.ResponseModel;
import com.addukkan.models.RoomDataModel;
import com.addukkan.models.RoomModel;
import com.addukkan.models.UserModel;
import com.addukkan.preferences.Preferences;
import com.addukkan.remote.Api;
import com.addukkan.share.Common;
import com.addukkan.tags.Tags;
import com.addukkan.uis.activity_chat.ChatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomsActivity extends AppCompatActivity {
    private ActivityRoomBinding binding;
    private String lang;
    private List<RoomModel> list;
    private RoomAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        list = new ArrayList<>();
        binding.setLang(lang);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        adapter = new RoomAdapter(this,list);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);
        binding.llBack.setOnClickListener(v -> finish());
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefresh.setOnRefreshListener(this::getRoom);
    }

    private void getRoom() {
        list.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
        Api.getService(Tags.base_url)
                .getRoom("Bearer "+userModel.getData().getToken(),userModel.getData().getId())
                .enqueue(new Callback<RoomDataModel>() {
                    @Override
                    public void onResponse(Call<RoomDataModel> call, Response<RoomDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getStatus() == 200) {
                                if (response.body().getData() != null) {
                                    if (response.body().getData().size() > 0) {
                                        binding.tvNoData.setVisibility(View.GONE);
                                        list.addAll(response.body().getData());
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        binding.tvNoData.setVisibility(View.VISIBLE);

                                    }
                                }
                            } else {

                                binding.swipeRefresh.setRefreshing(false);

                                //    Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            binding.progBar.setVisibility(View.GONE);
                            binding.swipeRefresh.setRefreshing(false);

                            switch (response.code()) {
                                case 500:
                                    //  Toast.makeText(CountryActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    // Toast.makeText(CountryActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_");
                            } catch (NullPointerException e) {

                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<RoomDataModel> call, Throwable t) {
                        try {
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    //Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    //Toast.makeText(CountryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }


    public void setItemData(RoomModel roomModel) {

        ChatRoomModel chatRoomModel = new ChatRoomModel(roomModel.getId(),roomModel.getOther_user().getId(),roomModel.getOther_user().getLogo(),roomModel.getOther_user().getName());
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("data", chatRoomModel);
        startActivity(intent);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getRoom();
    }
}
