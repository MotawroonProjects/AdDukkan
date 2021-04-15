package com.addukkan.uis.activity_sign_up;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import com.addukkan.R;
import com.addukkan.databinding.ActivityLoginBinding;
import com.addukkan.databinding.ActivitySignUpBinding;
import com.addukkan.language.Language;
import com.addukkan.models.LoginModel;
import com.addukkan.models.SignUpModel;
import com.addukkan.uis.activity_verification_code.VerificationCodeActivity;

import io.paperdb.Paper;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private SignUpModel signUpModel;
    private String lang = "ar";

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.tvLogin.setText(Html.fromHtml(getString(R.string.sign_up_text)));
        signUpModel = new SignUpModel();
        binding.setModel(signUpModel);
        binding.btnSignUp.setOnClickListener(v -> {
            if (signUpModel.isDataValid(this)) {
                Intent intent = new Intent(this, VerificationCodeActivity.class);
                intent.putExtra("phone_code",signUpModel.getPhone_code());
                intent.putExtra("phone", signUpModel.getPhone());
                startActivityForResult(intent,100);
            }
        });

        binding.tvLogin.setOnClickListener(v -> finish());

        binding.llBack.setOnClickListener(v -> finish());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK){
            signUp();
        }
    }

    private void signUp() {

    }
}