package com.example.gohotel.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.adapter.MyBookingAdapter;
import com.example.gohotel.dialog.DialogLoadingProgress;
import com.example.gohotel.model.api.BookingUserForm;
import com.example.gohotel.utils.PreferenceUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingList extends AppCompatActivity {
    RecyclerView rcvMyBooking;
    private List<BookingUserForm> bookingUserForms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_list_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
            }
        }

        rcvMyBooking = findViewById(R.id.rcvMyBooking);
        // mỗi một recyclerview sẽ được quản lý bằng layout manager
        rcvMyBooking.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvMyBooking.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyBooking();
    }

    private void getMyBooking() {
        //danh sách booking
        DialogLoadingProgress.getInstance().show(this);
        GoHotelApplication.serviceApi.getMyBooking(PreferenceUtils.getToken(this)).enqueue(new Callback<List<BookingUserForm>>() {
            @Override
            public void onResponse(Call<List<BookingUserForm>> call, Response<List<BookingUserForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    //list danh sach boooking trong responese.body
                    bookingUserForms = response.body();
                    if (bookingUserForms != null && bookingUserForms.size() > 0) {
                        // adapter để hung dữ liệu để hiện từng dòng
                        //adapter dung de quản lý view
                        rcvMyBooking.setAdapter(new MyBookingAdapter(BookingList.this, bookingUserForms));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BookingUserForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }
}
