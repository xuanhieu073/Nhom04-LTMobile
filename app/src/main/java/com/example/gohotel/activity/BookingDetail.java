package com.example.gohotel.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.dialog.DialogLoadingProgress;
import com.example.gohotel.model.api.BookRes;
import com.example.gohotel.model.api.BookingUserForm;
import com.example.gohotel.utils.AppTimeUtils;
import com.example.gohotel.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetail extends AppCompatActivity {
    int bookingId;
    BookingUserForm bookingUserForm;
    TextView tvSoDienThoai,btnCheckIn, tvHotelTitle, tvBookingId, tvRoomType, tvBookingTime, tvTimeBook, tvTotalPayment, tvStatus, btnCancelBooking;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_detail_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
            }
        }
        btnCheckIn = findViewById(R.id.btnCheckIn);
        btnCheckIn.setOnClickListener(view -> checkInBooking());
        tvSoDienThoai = findViewById(R.id.tvSoDienThoai);
        tvHotelTitle = findViewById(R.id.tvHotelTitle);
        tvBookingId = findViewById(R.id.tvBookingId);
        tvRoomType = findViewById(R.id.tvRoomType);
        tvBookingTime = findViewById(R.id.tvBookingTime);
        tvTimeBook = findViewById(R.id.tvTimeBook);
        tvTotalPayment = findViewById(R.id.tvTotalPayment);
        tvStatus = findViewById(R.id.tvStatus);
        btnCancelBooking = findViewById(R.id.btnCancelBooking);
        btnCancelBooking.setOnClickListener(view -> cancelBooking());
        // dùng intent để lấy booking id
        if (getIntent() != null && getIntent().getExtras() != null) {
            bookingId = getIntent().getExtras().getInt("BookingID");
            getBookingDetail();
        }
    }
	<!--Create by: Tan-->
	<!-- Description: checkInBooking -->
 
    private void checkInBooking() {
        DialogLoadingProgress.getInstance().show(this);

        GoHotelApplication.serviceApi.updateBookingDetail(bookingId, 1).enqueue(new Callback<BookRes>() {

            @Override
            public void onResponse(Call<BookRes> call, Response<BookRes> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    BookRes bookRes = response.body();
                    if (bookRes != null && bookRes.getResult() == 1) {
                        getBookingDetail();
                    }
                }
            }

            @Override
            public void onFailure(Call<BookRes> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }
    <!--Create by:Tan-->
    private void cancelBooking() {
        DialogLoadingProgress.getInstance().show(this);

        GoHotelApplication.serviceApi.updateBookingDetail(bookingId, -1).enqueue(new Callback<BookRes>() {
            @Override
            public void onResponse(Call<BookRes> call, Response<BookRes> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    BookRes bookRes = response.body();
                    if (bookRes != null && bookRes.getResult() == 1) {
                        getBookingDetail();
                    }
                }
            }

            @Override
            public void onFailure(Call<BookRes> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    private void getBookingDetail() {
        // lấy thông tin booking
        DialogLoadingProgress.getInstance().show(this);

        GoHotelApplication.serviceApi.getBookingDetail(bookingId).enqueue(new Callback<List<BookingUserForm>>() {
            @Override
            public void onResponse(Call<List<BookingUserForm>> call, Response<List<BookingUserForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<BookingUserForm> bookingUserForms = response.body();
                    if (bookingUserForms != null && bookingUserForms.size() > 0) {
                        bookingUserForm = bookingUserForms.get(0);
                        handleBookingUserForm();
                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<BookingUserForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void handleBookingUserForm() {
        tvSoDienThoai.setText(bookingUserForm.getPhone());
        tvHotelTitle.setText(bookingUserForm.getNameHotel());
        tvBookingId.setText(String.valueOf(bookingUserForm.getIdBook()));
        tvRoomType.setText(bookingUserForm.getNameRoom());


        tvBookingTime.setText(String.format("%s ~ %s", AppTimeUtils.changeDateShowClient(bookingUserForm.getDateStart()), AppTimeUtils.changeDateShowClient(bookingUserForm.getDateEnd())));
        tvTimeBook.setText(AppTimeUtils.changeTimeShowClient(bookingUserForm.getTimeBook()));
        tvTotalPayment.setText(Utils.formatCurrency(bookingUserForm.getPrice()));
        btnCancelBooking.setVisibility(View.GONE);

        btnCheckIn.setVisibility(View.GONE);
        if (bookingUserForm.getStatus() == 0) {
            btnCancelBooking.setVisibility(View.VISIBLE);
            tvStatus.setText("Đã đặt");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -2);
            String yesterday = new SimpleDateFormat(AppTimeUtils.ddMMyyyy2).format(cal.getTime());
            if (yesterday.equals(AppTimeUtils.changeDateShowClient(bookingUserForm.getDateEnd()))) {
                btnCheckIn.setVisibility(View.VISIBLE);
            }
        } else if (bookingUserForm.getStatus() == -1)
            tvStatus.setText("Đã hủy");
        else if (bookingUserForm.getStatus() == 1)
            tvStatus.setText("Đã nhận phòng");

    }
}
