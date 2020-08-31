package com.example.gohotel.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.adapter.ChooseRoomTypeListAdapter;
import com.example.gohotel.dialog.DialogLoadingProgress;
import com.example.gohotel.model.HotelForm;
import com.example.gohotel.model.api.BookRes;
import com.example.gohotel.model.api.HotelImageForm;
import com.example.gohotel.model.api.RoomTypeForm;
import com.example.gohotel.utils.AppTimeUtils;
import com.example.gohotel.utils.PreferenceUtils;
import com.example.gohotel.utils.Utils;
import com.example.gohotel.widgets.Calendar.CalendarChooseDate;
import com.example.gohotel.widgets.Calendar.CallbackResulCalendar;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ReservationActivity extends AppCompatActivity implements ChooseRoomTypeListAdapter.CallbackRoomTypeList {

    ImageView imgHotel, imgRoom;
    private int width;
    private int height;
    private int hotelId;
    private int roomId;
    private List<RoomTypeForm> roomTypeForms;
    private RequestOptions options;
    private TextView tvRoomName, tvHotelFee, tvStartDate, tvEndDate, tvCheckOut, tvCheckIn, tvHotelName;
    private String startDate, endDate;
    private int hotelFee;
    private RecyclerView rcvListRoom;
    private int roomtypeIndex;
    private LinearLayout layoutBkTranfer, bottomsheet, btnChooseRoom, btnStartDate, btnEndDate;
    private Button btnBook;
    private ImageView btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            hotelId = bundle.getInt("hotelId", 0);
            roomId = bundle.getInt("roomId", 0);
        }
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> onBackPressed());
        btnBook = findViewById(R.id.btnBook);
        btnBook.setOnClickListener(view -> bookRoom());
        tvHotelName = findViewById(R.id.tvHotelName);
        tvCheckIn = findViewById(R.id.tvCheckIn);
        tvCheckOut = findViewById(R.id.tvCheckOut);
        startDate = AppTimeUtils.getSystemDay(new SimpleDateFormat(AppTimeUtils.ddMMyyyy2));
        endDate = AppTimeUtils.getTomorrowDay(new SimpleDateFormat(AppTimeUtils.ddMMyyyy2));
        tvStartDate = findViewById(R.id.tvStartDate);
        tvStartDate.setText(startDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvEndDate.setText(endDate);
        rcvListRoom = findViewById(R.id.rcvListRoom);
        tvRoomName = findViewById(R.id.tvRoomName);
        imgHotel = findViewById(R.id.imgHotel);
        imgRoom = findViewById(R.id.imgRoom);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2);
        imgHotel.setLayoutParams(layoutParams);
        tvHotelFee = findViewById(R.id.tvHotelFee);
        layoutBkTranfer = findViewById(R.id.layoutBkTranfer);
        bottomsheet = findViewById(R.id.bottom_sheet);
        btnChooseRoom = findViewById(R.id.btnChooseRoom);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarChooseDate.getInstance().show(ReservationActivity.this, startDate, endDate, false, new CallbackResulCalendar() {
                    @Override
                    public void CallbackResult(String dateFrom, String dateTo) {
                        tvStartDate.setText(startDate);
                        tvEndDate.setText(endDate);
                        startDate = dateFrom;
                        endDate = dateTo;
                    }
                });
            }
        });
        btnEndDate = findViewById(R.id.btnEndDate);
        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarChooseDate.getInstance().show(ReservationActivity.this, startDate, endDate, false, new CallbackResulCalendar() {
                    @Override
                    public void CallbackResult(String dateFrom, String dateTo) {
                        tvStartDate.setText(startDate);
                        tvEndDate.setText(endDate);
                        startDate = dateFrom;
                        endDate = dateTo;
                    }
                });
            }
        });
        btnChooseRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibleBottomRoomType(View.VISIBLE);

            }
        });
        layoutBkTranfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibleBottomRoomType(View.GONE);
            }
        });
        options = new RequestOptions()
                .placeholder(R.drawable.loading_big)
                .diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop();

        gethotelDetail();
        getImageHotel();
        getRoomHotel();
    }

    private void gethotelDetail() {
        GoHotelApplication.serviceApi.getHotelDetail(hotelId).enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleDataHotel(hotelForms.get(0));
                    }
                } else {
                    Toast.makeText(ReservationActivity.this, "Không thể lấy thông tin khách sạn", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
            }
        });
    }

    private void handleDataHotel(HotelForm hotelForm) {
        tvHotelName.setText(hotelForm.getNameHotel());
        tvCheckIn.setText(hotelForm.getCheckIn() + ":00");
        tvCheckOut.setText(hotelForm.getCheckOut() + ":00");
    }


    private void initRcvRoom() {
        rcvListRoom.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvListRoom.setHasFixedSize(true);
        if (roomTypeForms != null && roomTypeForms.size() != 0) {
            ChooseRoomTypeListAdapter roomTypeListAdapter = new ChooseRoomTypeListAdapter(this, roomTypeForms, this);
            rcvListRoom.setAdapter(roomTypeListAdapter);
            roomTypeListAdapter.notifyDataSetChanged();
            if (roomTypeListAdapter != null) {
                roomTypeListAdapter.updatePotitionChoose(roomtypeIndex);
            }
        }
    }

    private void getRoomHotel() {
        GoHotelApplication.serviceApi.getRoomTypeHotel(hotelId).enqueue(new Callback<List<RoomTypeForm>>() {
            @Override
            public void onResponse(Call<List<RoomTypeForm>> call, Response<List<RoomTypeForm>> response) {
                if (response.code() == 200) {
                    List<RoomTypeForm> roomTypeForms = response.body();
                    if (roomTypeForms != null && roomTypeForms.size() > 0) {
                        handleRoomTypeHotel(roomTypeForms);
                        initRcvRoom();
                    }
                } else {
                    Toast.makeText(ReservationActivity.this, "Không thể lấy danh sách phòng", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<RoomTypeForm>> call, Throwable t) {
            }
        });
    }

    private void handleRoomTypeHotel(List<RoomTypeForm> roomTypeForms) {
        this.roomTypeForms = roomTypeForms;
        for (RoomTypeForm roomTypeForm : roomTypeForms) {
            if (roomTypeForm.getId() == roomId) {
                Glide.with(this)
                        .load(R.drawable.bananahotel).apply(options).into(imgRoom);
                tvRoomName.setText(roomTypeForm.getName());
                tvHotelFee.setText(String.format("%s VND", Utils.formatCurrency(roomTypeForm.getPricePerDay())));
                hotelFee = roomTypeForm.getPricePerDay();
            }

        }
    }

    private void getImageHotel() {
        GoHotelApplication.serviceApi.getImageHotel(hotelId).enqueue(new Callback<List<HotelImageForm>>() {
            @Override
            public void onResponse(Call<List<HotelImageForm>> call, Response<List<HotelImageForm>> response) {
                if (response.code() == 200) {
                    List<HotelImageForm> hotelImageForms = response.body();
                    if (hotelImageForms != null && hotelImageForms.size() > 0) {
                        RequestOptions requestOptions = new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.loading_big)
                                .error(R.drawable.loading_big);
                        Glide.with(ReservationActivity.this)
                                .load(hotelImageForms.get(0).getNameImage())
                                .apply(requestOptions)
                                .transition(withCrossFade())
                                .into(imgHotel);
                    }
                } else {
                    Toast.makeText(ReservationActivity.this, "Không thể lấy hình khách sạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelImageForm>> call, Throwable t) {

            }
        });
    }


    @Override
    public void resultRoomType(RoomTypeForm roomTypeForm, int potition) {
        this.roomId = roomTypeForm.getId();
        this.roomtypeIndex = potition;
        handleRoomTypeHotel(roomTypeForms);
        visibleBottomRoomType(View.GONE);
    }

    private void visibleBottomRoomType(int visible) {
        if (visible == View.VISIBLE) {
            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            layoutBkTranfer.setVisibility(View.VISIBLE);
            bottomsheet.startAnimation(slideUp);
            bottomsheet.setVisibility(View.VISIBLE);
        } else {
            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_down);
            layoutBkTranfer.setVisibility(View.GONE);
            bottomsheet.startAnimation(slideUp);
            bottomsheet.setVisibility(View.GONE);
        }
    }

    public interface DialogCallback {
        void finished();
    }
}
