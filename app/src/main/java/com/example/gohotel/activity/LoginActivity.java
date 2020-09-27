package com.example.gohotel.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.dialog.DialogLoadingProgress;
import com.example.gohotel.model.api.UserInfo;
import com.example.gohotel.utils.ParamConstants;
import com.example.gohotel.utils.PreferenceUtils;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    TextView btnLogin;
    TextView tvSignup;
    ImageView btnClose;
    private TextInputLayout inputLayoutPhone, inputLayoutPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
            }
        }
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        inputLayoutPhone = findViewById(R.id.inputLayoutPhone);
        btnClose = findViewById(R.id.btnClose);
        edtPhone = findViewById(R.id.edtPhone);
        edtPhone.setOnFocusChangeListener((view, hasFocus) -> {
            // user focus
            // nếu user không còn forcus
            if (!hasFocus) {
                String phone = edtPhone.getText().toString();
                if (phone.isEmpty()) {
                    inputLayoutPhone.setError("Số điện thoại không được để trống");
                    inputLayoutPhone.setErrorEnabled(true);
                } else {
                    inputLayoutPhone.setError("");
                    inputLayoutPhone.setErrorEnabled(false);
                }
            }
        });
        
        // kiểm tra password
        edtPassword = findViewById(R.id.edtPassword);
        edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String pass = edtPassword.getText().toString();
                    if (pass.isEmpty()) {
                        inputLayoutPassword.setError("Mật khẩu không được để trống");
                        inputLayoutPassword.setErrorEnabled(true);
                    } else {
                        inputLayoutPassword.setError("");
                        inputLayoutPassword.setErrorEnabled(false);
                    }
                }
            }
        });
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin();
            }
        });
        tvSignup = findViewById(R.id.tvSignup);
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                // request signup hứng kết quả có thành công hay
                startActivityForResult(intent, ParamConstants.REQUEST_SIGNUP_LOGIN);
            }
        });
        //onbackpress dùng để back lại màn hình trước
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ParamConstants.REQUEST_SIGNUP_LOGIN) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    Bundle bundle = data.getExtras();
                    String password = bundle.getString("password");
                    String phone = bundle.getString("phone");
                    edtPhone.setText(phone);
                    edtPassword.setText(password);
                    handleLogin();
                }
            }
        }
    }

    private void handleLogin() {
        //gọi hàm request forcus để chạy vào sự kiện forcus của user
        edtPhone.requestFocus();
        edtPassword.requestFocus();

        if (inputLayoutPassword.isErrorEnabled() || inputLayoutPhone.isErrorEnabled()) {
            return;
        }
        String phone = edtPhone.getText().toString();
        String pass = edtPassword.getText().toString();
        // gọi api
        DialogLoadingProgress.getInstance().show(this);

        GoHotelApplication.serviceApi.login(phone, pass).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {//json trả về trong body
                    UserInfo userInfo = response.body();
                    if (userInfo != null) {
                        if (userInfo.getResult() == 1) {
                            String json = new Gson().toJson(userInfo);
                            PreferenceUtils.setToken(LoginActivity.this, userInfo.getToken());
                            PreferenceUtils.setUserInfo(LoginActivity.this, json);
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });

    }
}
