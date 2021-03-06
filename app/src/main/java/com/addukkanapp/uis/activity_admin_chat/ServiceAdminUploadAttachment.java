package com.addukkanapp.uis.activity_admin_chat;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.addukkanapp.R;
import com.addukkanapp.models.AdminMessageModel;
import com.addukkanapp.models.SingleAdminMessageDataModel;
import com.addukkanapp.remote.Api;
import com.addukkanapp.share.Common;
import com.addukkanapp.tags.Tags;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceAdminUploadAttachment extends Service {
    private String file_uri;
    private int user_id;
    private int to_user_id;
    private String user_token;
    private int room_id;
    private String attachment_type;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        file_uri = intent.getStringExtra("file_uri");
        user_token = intent.getStringExtra("user_token");
        user_id = intent.getIntExtra("user_id",0);
        to_user_id = intent.getIntExtra("to_user_id",0);

        room_id = intent.getIntExtra("room_id",0);
        attachment_type = intent.getStringExtra("attachment_type");

        uploadAttachment(attachment_type);

        return START_STICKY;
    }

    private void uploadAttachment(String attachment_type) {

        RequestBody user_id_part = Common.getRequestBodyText(String.valueOf(user_id));
        RequestBody to_user_id_part = Common.getRequestBodyText(String.valueOf(to_user_id));
        RequestBody room_id_part = Common.getRequestBodyText(String.valueOf(room_id));
        RequestBody type_part = Common.getRequestBodyText(attachment_type);

        MultipartBody.Part file_part;
        if (attachment_type.equals("image")){
            file_part = Common.getMultiPartImage(this, Uri.parse(file_uri), "image");

        }else{
            file_part = Common.getMultiPartAudio(this, file_uri, "voice");

        }
        Api.getService(Tags.base_url).sendAdminChatAttachment("Bearer "+user_token, room_id_part ,user_id_part,to_user_id_part,type_part,file_part)
                .enqueue(new Callback<SingleAdminMessageDataModel>() {
                    @Override
                    public void onResponse(Call<SingleAdminMessageDataModel> call, Response<SingleAdminMessageDataModel> response) {
                        stopSelf();
                        if (response.isSuccessful() && response.body() != null) {

                            AdminMessageModel model = response.body().getData();
                            EventBus.getDefault().post(model);
                        } else {

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {

                                Toast.makeText(ServiceAdminUploadAttachment.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ServiceAdminUploadAttachment.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleAdminMessageDataModel> call, Throwable t) {

                        try {

                            stopSelf();

                            if (t.getMessage() != null) {
                                Log.e("msg_chat_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {

                                    Toast.makeText(ServiceAdminUploadAttachment.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ServiceAdminUploadAttachment.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });

        stopSelf();
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
