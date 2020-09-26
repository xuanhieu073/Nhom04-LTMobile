package com.example.gohotel.activity;

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
import com.example.gohotel.model.api.BookRes;
import com.example.gohotel.model.api.ResponseUserCreate;
import com.example.gohotel.model.api.UserInfo;
import com.example.gohotel.utils.AppTimeUtils;
import com.example.gohotel.utils.PreferenceUtils;
import com.example.gohotel.utils.UtilityValidate;
import com.example.gohotel.widgets.dialog.DateTimeDialogUtils;
import com.google.gson.Gson;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    UserInfo userInfo;
    TextInputLayout inputLayoutMail, inputLayoutPhone, inputLayoutPassword;
    EditText edtPhone, edtEmail, edtPassword, edtBirthday;
    RadioButton rdNam, rdNu;
    Button btnUpdate;
    ImageView btnClose;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        String jsonUser = PreferenceUtils.getUserInfo(this);
        userInfo = new Gson().fromJson(jsonUser, UserInfo.class);

        inputLayoutMail = findViewById(R.id.inputLayoutMail);
        inputLayoutPhone = findViewById(R.id.inputLayoutPhone);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);

        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtBirthday = findViewById(R.id.edtBirthday);
        edtPhone.setFocusable(false);

        edtEmail.setOnFocusChangeListener((view, hasFocus) -> {
            //sự kiện forcus vào ô email
            if (!hasFocus) {
                //nếu không forcus
                if (!UtilityValidate.isEmailValid(edtEmail.getText().toString())) {
                    inputLayoutMail.setError("Định dạng email không hợp lệ");
                    inputLayoutMail.setErrorEnabled(true);
                } else {
                    inputLayoutMail.setError("");
                    inputLayoutMail.setErrorEnabled(false);
                }
            }
        });

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

        edtBirthday.setOnClickListener(view -> {
            // sét minyear cho lịch chọn này là 1950
            Calendar minYear = Calendar.getInstance();
            minYear.set(Calendar.YEAR, 1950);

            //max year = năm hiện tại - 18
            Calendar maxYear = Calendar.getInstance();
            maxYear.set(Calendar.YEAR, maxYear.get(Calendar.YEAR) - 18);
            DateTimeDialogUtils.showDatePickerDialog(this, edtBirthday, minYear, maxYear);
        });
        rdNam = findViewById(R.id.rdNam);
        rdNu = findViewById(R.id.rdNu);
        //set phone
        edtPhone.setText(userInfo.getNumberPhone());
        edtBirthday.setText(AppTimeUtils.changeDateShowClient(userInfo.getBirthday()));
        String gender=userInfo.getGender();
        //set giới tính
        if(gender.equals("nam"))
            rdNam.setChecked(true);
        else rdNu.setChecked(true);
        //set email
        edtEmail.setText(userInfo.getEmail());

        //update
        btnUpdate=findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
        //dong màn hình
        btnClose=findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void updateUser() {
        //update user gọi hàm request để chạy sự kiện lăng nghe request
        edtPhone.requestFocus();
        edtPassword.requestFocus();
        edtEmail.requestFocus();
        //kiểm tra có lỗi không
        if (inputLayoutMail.isErrorEnabled() || inputLayoutPhone.isErrorEnabled() ) {
            return;
        }
        String pass = edtPassword.getText().toString();

        String phone = edtPhone.getText().toString();

        String email = edtEmail.getText().toString();
        String birthday = AppTimeUtils.changeDateUpToServer(edtBirthday.getText().toString());
        String gender = "nam";
        if (!rdNam.isChecked())
            gender = "nữ";
        // update user
        String finalGender = gender;
        //gọi api update user
        DialogLoadingProgress.getInstance().show(this);

        GoHotelApplication.serviceApi.updateUser(PreferenceUtils.getToken(this),phone,pass,gender,email,birthday).enqueue(new Callback<BookRes>() {
            @Override
            public void onResponse(Call<BookRes> call, Response<BookRes> response) {
                DialogLoadingProgress.getInstance().hide();

                if(response.code()==200){
                    BookRes bookRes=response.body();
                    if(bookRes!=null){
                        if(bookRes.getResult()>0){
                            Toast.makeText(EditProfileActivity.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                            userInfo.setEmail(email);
                            userInfo.setBirthday(birthday);
                            userInfo.setGender(finalGender);
                            PreferenceUtils.setUserInfo(EditProfileActivity.this,new Gson().toJson(userInfo));
                            setResult(RESULT_OK);
                            finish();
                        }else {
                            Toast.makeText(EditProfileActivity.this,"Cập nhật thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(EditProfileActivity.this,"Cập nhật thất bại",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(EditProfileActivity.this,"Cập nhật thất bại",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookRes> call, Throwable t) {
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
