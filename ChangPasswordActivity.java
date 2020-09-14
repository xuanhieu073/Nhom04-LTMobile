package com.example.gohotel.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.dialog.DialogLoadingProgress;
import com.example.gohotel.model.api.BookRes;
import com.example.gohotel.utils.PreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangPasswordActivity extends AppCompatActivity {
    ImageView btnClose;
    TextInputLayout inputLayoutPassword, inputLayoutNewPass, inputLayoutConfirm;
    EditText edtPass, edtNewPassword, edtConfirmPassword;
    Button btnUpdate;
    String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        if (getIntent() != null && getIntent().getExtras() != null) {
            phone = getIntent().getExtras().getString("phone");
        }
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> onBackPressed());

        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        inputLayoutNewPass = findViewById(R.id.inputLayoutNewPass);
        inputLayoutConfirm = findViewById(R.id.inputLayoutConfirm);

        edtPass = findViewById(R.id.edtPass);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        edtPass.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                String pass = edtPass.getText().toString();
                if (pass.isEmpty()) {
                    inputLayoutPassword.setError("Mật khẩu không được để trống");
                    inputLayoutPassword.setErrorEnabled(true);
                } else {
                    inputLayoutPassword.setError("");
                    inputLayoutPassword.setErrorEnabled(false);
                }
            }
        });

        edtNewPassword.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                String pass = edtNewPassword.getText().toString();
                if (pass.isEmpty()) {
                    inputLayoutNewPass.setError("Mật khẩu mới không được để trống");
                    inputLayoutNewPass.setErrorEnabled(true);
                } else {
                    inputLayoutNewPass.setError("");
                    inputLayoutNewPass.setErrorEnabled(false);
                }
            }
        });

        edtConfirmPassword.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                String pass = edtNewPassword.getText().toString();
                if (!pass.equals(edtConfirmPassword.getText().toString())) {
                    inputLayoutConfirm.setError("Không trùng với mật khẩu");
                    inputLayoutConfirm.setErrorEnabled(true);
                } else {
                    inputLayoutConfirm.setError("");
                    inputLayoutConfirm.setErrorEnabled(false);
                }
            }
        });
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changPassword();
            }
        });
    }

    private void changPassword() {


        edtPass.requestFocus();
        edtNewPassword.requestFocus();
        edtConfirmPassword.requestFocus();

        String pass = edtPass.getText().toString();


        String newPass = edtNewPassword.getText().toString();

        DialogLoadingProgress.getInstance().show(this);

        GoHotelApplication.serviceApi.changePassword(PreferenceUtils.getToken(this), phone, pass, newPass).enqueue(new Callback<BookRes>() {
            @Override
            public void onResponse(Call<BookRes> call, Response<BookRes> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    BookRes bookRes = response.body();
                    if (bookRes != null && bookRes.getResult() > 0) {
                        Toast.makeText(ChangPasswordActivity.this,"Thay đổi mật khẩu thành công! Vui lòng đăng nhập lại",Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        Toast.makeText(ChangPasswordActivity.this, "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangPasswordActivity.this, "Thay đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookRes> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });

    }
}
