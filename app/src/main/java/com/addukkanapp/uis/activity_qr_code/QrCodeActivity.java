package com.addukkanapp.uis.activity_qr_code;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;


import com.addukkanapp.R;
import com.addukkanapp.databinding.ActivityQrCodeBinding;
import com.addukkanapp.language.Language;
import com.addukkanapp.models.AddCartDataModel;
import com.addukkanapp.models.AddCartProductItemModel;
import com.addukkanapp.models.AppLocalSettings;
import com.addukkanapp.models.ResponseModel;
import com.addukkanapp.models.ScanCart;
import com.addukkanapp.models.UserModel;
import com.addukkanapp.preferences.Preferences;
import com.addukkanapp.remote.Api;
import com.addukkanapp.share.Common;
import com.addukkanapp.tags.Tags;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.ScanMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCodeActivity extends AppCompatActivity {
    private ActivityQrCodeBinding binding;
    private String lang;
    private CodeScanner mCodeScanner;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final int CAMERA_REQ = 2;
    private Preferences preferences;
    private UserModel userModel;
    private AppLocalSettings settings;
    private String country_coude;
    private String currecny;
    private AddCartDataModel createOrderModel;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_code);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        settings = preferences.isLanguageSelected(this);
        userModel = preferences.getUserData(this);
        if (userModel != null) {
            country_coude = userModel.getData().getCountry_code();
            currecny = userModel.getData().getUser_country().getCountry_setting_trans_fk().getCurrency();
        } else {
            country_coude = settings.getCountry_code();
            currecny = settings.getCurrency();
        }
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        checkCameraPermission();

    }

    private void initScanner() {

        mCodeScanner = new CodeScanner(this, binding.scannerView);
        mCodeScanner.setScanMode(ScanMode.SINGLE);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            binding.scannerView.setVisibility(View.GONE);
            scanOrder(result.getText());
        }));
        binding.scannerView.setVisibility(View.VISIBLE);
        mCodeScanner.startPreview();
    }

    private void scanOrder(String barcode) {


        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Log.e("vvvvvvvv", barcode + "___");

        String token = null;
        String user_id = null;

        if (userModel != null) {
            token = "Bearer " + userModel.getData().getToken();
            user_id = userModel.getData().getId() + "";
        } else {

        }

        Api.getService(Tags.base_url)
                .scanOrder(token, user_id, barcode, country_coude)
                .enqueue(new Callback<ScanCart>() {
                    @Override
                    public void onResponse(Call<ScanCart> call, Response<ScanCart> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                //  CreateDialogAlert(activity, response.body());
                                mCodeScanner.releaseResources();
                                mCodeScanner.stopPreview();

                                Toast.makeText(QrCodeActivity.this, getResources().getString(R.string.suc_and), Toast.LENGTH_LONG).show();
                                if (userModel == null) {
                                    updateData(response.body());
                                }
                            } else if (response.body().getStatus() == 404) {
                                Toast.makeText(QrCodeActivity.this, getString(R.string.not_found), Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus() == 406) {
                                Toast.makeText(QrCodeActivity.this, getString(R.string.no_product), Toast.LENGTH_SHORT).show();

                            } else {
                                // Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }
                            finish();
                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(QrCodeActivity.this, "Server Error ssss", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(QrCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ScanCart> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(QrCodeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(QrCodeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }


    public void checkCameraPermission() {


        if (ContextCompat.checkSelfPermission(this, write_permission) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, camera_permission) == PackageManager.PERMISSION_GRANTED
        ) {
            initScanner();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, CAMERA_REQ);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQ) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initScanner();

            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateData(ScanCart body) {
        for (int i = 0; i < body.getData().getDetails().size(); i++) {
            addItemToCart(body.getData().getDetails().get(i));
        }

    }

    private void addItemToCart(ScanCart.Data.Detail singleProductModel) {


        AddCartDataModel addCartDataModel;

        if (userModel != null) {
            addCartDataModel = new AddCartDataModel();
        } else {
            addCartDataModel = preferences.getCartData(this);
            if (addCartDataModel == null) {
                addCartDataModel = new AddCartDataModel();
            }
        }
        List<AddCartProductItemModel> addCartProductItemModelList;
        if (addCartDataModel.getCart_products() != null) {
            addCartProductItemModelList = addCartDataModel.getCart_products();
        } else {
            addCartProductItemModelList = new ArrayList<>();
        }
        AddCartProductItemModel addCartProductItemModel = new AddCartProductItemModel();
        addCartDataModel.setCountry_code(country_coude);
        if (userModel != null) {
            addCartDataModel.setUser_id(userModel.getData().getId());
        }
        //double totalPrice = price;
        // Log.e("ttt", totalPrice + "__" + price);

        int pos = -1;
        for (int i = 0; i < addCartProductItemModelList.size(); i++) {
            if (addCartProductItemModelList.get(i).getProduct_id().equals(singleProductModel.getId() + "")) {
                addCartProductItemModel = addCartProductItemModelList.get(i);
                pos = i;
                break;
            }
        }
        if (pos > -1) {
            addCartProductItemModel.setAmount(addCartProductItemModel.getAmount() + 1);
            addCartProductItemModelList.set(pos, addCartProductItemModel);
            addCartDataModel.setCart_products(addCartProductItemModelList);

        } else {
            addCartDataModel.setTotal_price(singleProductModel.getPrice());
            addCartProductItemModel.setAmount(singleProductModel.getAmount());
            addCartProductItemModel.setHave_offer(singleProductModel.getHave_offer());
            addCartProductItemModel.setOffer_bonus(singleProductModel.getOffer_bonus());
            addCartProductItemModel.setOffer_min(singleProductModel.getOffer_min());
            addCartProductItemModel.setOffer_type(singleProductModel.getOffer_type());
            addCartProductItemModel.setOld_price(singleProductModel.getPrice());
            addCartProductItemModel.setPrice(singleProductModel.getPrice());
            addCartProductItemModel.setProduct_id(singleProductModel.getId() + "");
            addCartProductItemModel.setOffer_value(singleProductModel.getOffer_value());
            addCartProductItemModel.setProduct_price_id(singleProductModel.getProduct_price_id() + "");
            addCartProductItemModel.setVendor_id(singleProductModel.getVendor_id() + "");
            addCartProductItemModel.setName(singleProductModel.getProduct_data().getProduct_trans_fk().getTitle());
            addCartProductItemModel.setImage(singleProductModel.getProduct_data().getMain_image());
            addCartProductItemModel.setRate(singleProductModel.getProduct_data().getRate());
            addCartProductItemModel.setDesc(singleProductModel.getProduct_data().getProduct_trans_fk().getDescription());
            addCartProductItemModelList.add(addCartProductItemModel);
            addCartDataModel.setCart_products(addCartProductItemModelList);
        }


        // binding.tvCounter.setText(counter + "");

        preferences.create_update_cart(this, addCartDataModel);


    }

    @Override
    public void onBackPressed() {

        finish();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCodeScanner != null) {
            mCodeScanner.releaseResources();

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mCodeScanner != null) {
            mCodeScanner.startPreview();

        }
    }
}