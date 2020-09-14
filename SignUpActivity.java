package com.example.gohotel.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.dialog.DialogLoadingProgress;
import com.example.gohotel.model.api.ResponseUserCreate;
import com.example.gohotel.utils.AppTimeUtils;
import com.example.gohotel.utils.PreferenceUtils;
import com.example.gohotel.utils.UtilityValidate;
import com.example.gohotel.widgets.dialog.DateTimeDialogUtils;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText edtPhone, edtEmail, edtPassword, edtConfirmPassword, edtBirthday;
    RadioButton rdNam, rdNu;
    Button btnRegister;
    TextInputLayout inputLayoutMail, inputLayoutPhone, inputLayoutConfirm, inputLayoutPassword;
    ImageView btnClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        addViews();

    }

    private void addViews() {
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> onBackPressed());
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        inputLayoutConfirm = findViewById(R.id.inputLayoutConfirm);
        inputLayoutPhone = findViewById(R.id.inputLayoutPhone);
        inputLayoutMail = findViewById(R.id.inputLayoutMail);
        edtPhone = findViewById(R.id.edtPhone);
        // bắt sự kiện khi người dùng forcus
        edtPhone.setOnFocusChangeListener((view, hasFocus) -> {
            // người dùng ko còn forcus với nhập phone => has forcus = false
            if (!hasFocus) {
                String phone=edtPhone.getText().toString();
                //kiểm tra phone rỗng
                if(phone.isEmpty()){
                    inputLayoutPhone.setError("Số điện thoại không được để trống");
                    inputLayoutPhone.setErrorEnabled(true);
                }else {
                    // không rỗng gọi api kiểm tra số điện thoại tồn tại trên server hay không
                checkPhoneAlready(new CheckPhoneListener() {
                    @Override
                    public void onAlreadyExists() {
                        inputLayoutPhone.setError("Số điện thoại đã tồn tại");
                        inputLayoutPhone.setErrorEnabled(true);
                    }

                    @Override
                    public void onNotAlreadyExists() {
                        inputLayoutPhone.setError("");
                        inputLayoutPhone.setErrorEnabled(false);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }}
        });
        edtEmail = findViewById(R.id.edtEmail);
        edtEmail.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                //kiểm tra dinh dạng mail
                if (!UtilityValidate.isEmailValid(edtEmail.getText().toString())) {
                    inputLayoutMail.setError("Định dạng email không hợp lệ");
                    inputLayoutMail.setErrorEnabled(true);
                } else {
                    inputLayoutMail.setError("");
                    inputLayoutMail.setErrorEnabled(false);
                }
            }
        });

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
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String pass = edtPassword.getText().toString();
                    //kiểm tra trùng
                    if (!pass.equals(edtConfirmPassword.getText().toString())) {
                        inputLayoutConfirm.setError("Không trùng với mật khẩu");
                        inputLayoutConfirm.setErrorEnabled(true);
                    } else {
                        inputLayoutConfirm.setError("");
                        inputLayoutConfirm.setErrorEnabled(false);
                    }
                }
            }
        });
        edtBirthday = findViewById(R.id.edtBirthday);
        // sự kiện user click vào
        edtBirthday.setOnClickListener(view -> {
            // lấy ngày tháng năm nhỏ nhất
            Calendar minYear = Calendar.getInstance();
            minYear.set(Calendar.YEAR, 1950);

            // max year là năm hiện -
            Calendar maxYear = Calendar.getInstance();
            maxYear.set(Calendar.YEAR, maxYear.get(Calendar.YEAR) - 18);

            DateTimeDialogUtils.showDatePickerDialog(SignUpActivity.this, edtBirthday, minYear, maxYear);
        });
        rdNam = findViewById(R.id.rdNam);
        rdNu = findViewById(R.id.rdNu);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(view -> registerUser(SignUpActivity.this));
    }

    private void checkPhoneAlready(CheckPhoneListener checkPhoneListener) {
        // lấy số điện thoại đã nhập ra
        String phone = edtPhone.getText().toString();

        GoHotelApplication.serviceApi.checkEqualPhone(phone, "").enqueue(new Callback<ResponseUserCreate>() {
            @Override
            public void onResponse(Call<ResponseUserCreate> call, Response<ResponseUserCreate> response) {
                // trả về thành công
                if (response.code() == 200) {
                    // chỗi json nằm trong body
                    ResponseUserCreate responseUserCreate = response.body();
                    //nếu result ==1 => tồn
                    if (responseUserCreate.getResult() == 1) {
                        checkPhoneListener.onNotAlreadyExists();
                        // lưu token lại trong preferrent
                        PreferenceUtils.setToken(SignUpActivity.this, responseUserCreate.getToken());
                    } else {
                        checkPhoneListener.onAlreadyExists();
                        Toast.makeText(SignUpActivity.this, responseUserCreate.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    checkPhoneListener.onError();
                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại vui lòng thử lại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserCreate> call, Throwable t) {

            }
        });
    }

    private void registerUser(Context context) {
        //request forcus gọi lại sự kiện forcus
        edtPhone.requestFocus();
        edtConfirmPassword.requestFocus();
        edtPassword.requestFocus();
        edtEmail.requestFocus();
        // nếu 1 trong tất cả bị lỗi
        if (inputLayoutMail.isErrorEnabled() || inputLayoutPhone.isErrorEnabled() || inputLayoutConfirm.isErrorEnabled()) {
            return;
        }
        String pass = edtPassword.getText().toString();
        if (!pass.equals(edtConfirmPassword.getText().toString())) {
            inputLayoutConfirm.setError("Không trùng với mật khẩu");
            inputLayoutConfirm.setErrorEnabled(true);
        } else {
            inputLayoutConfirm.setError("");
            inputLayoutConfirm.setErrorEnabled(false);
        }

        String phone = edtPhone.getText().toString();

        String email = edtEmail.getText().toString();
        String birthday = AppTimeUtils.changeDateUpToServer(edtBirthday.getText().toString());
        String gender = "nam";
        if (!rdNam.isChecked())
            gender = "nữ";
        //gọi api create user
        DialogLoadingProgress.getInstance().show(context);
        GoHotelApplication.serviceApi.createUser(phone, pass, birthday, email, gender, GoHotelApplication.DEVICE_ID, "").enqueue(new Callback<ResponseUserCreate>() {
            @Override
            public void onResponse(Call<ResponseUserCreate> call, retrofit2.Response<ResponseUserCreate> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    //json se dc trả về trong response body
                    ResponseUserCreate responseUserCreate = response.body();
                    if (responseUserCreate.getResult() > 0) {
                        //đăng ký thành công
                        PreferenceUtils.setToken(context, responseUserCreate.getToken());
                        Intent intent = new Intent();
                        intent.putExtra("phone", phone);
                        intent.putExtra("password", pass);
                        setResult(RESULT_OK,intent);
                        //đóng màn hình
                        finish();
                    } else {
                        Toast.makeText(context, responseUserCreate.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Đăng ký thất bại vui lòng thử lại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserCreate> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });

    }


    private interface CheckPhoneListener {
        void onAlreadyExists();

        void onNotAlreadyExists();

        void onError();
    }
}
