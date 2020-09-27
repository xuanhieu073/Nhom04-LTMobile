package com.example.gohotel.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.adapter.RoomImageDetailAdapter;
import com.example.gohotel.adapter.RoomTypeAdapter;
import com.example.gohotel.dialog.DialogLoadingProgress;
import com.example.gohotel.model.HotelForm;
import com.example.gohotel.model.api.HotelImageForm;
import com.example.gohotel.model.api.RoomTypeForm;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelDetailActivity extends AppCompatActivity {
    private RecyclerView rcvImgRoom, rcvRoomAdapter;
    private int width;
    private int height;
    private Resources resources;
    private RoomImageDetailAdapter adapter;
    private RoomTypeAdapter roomTypeAdapter;
    private TextView tvHotelName, tvAddress, tvAmountImage;
    private List<HotelImageForm> hotelImageForms;
    private int hotelId;
    private ImageView btnBack;
    private Button btnBook;
    private List<RoomTypeForm> roomTypeForms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_detail_activity);
        resources = getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (getIntent() != null && getIntent().getExtras() != null)
            hotelId = getIntent().getExtras().getInt("hotelKey", 0);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        setViews();
    }

    private void setViews() {
        btnBook = findViewById(R.id.btnBook);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookRoom();
            }
        });
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> onBackPressed());
        tvAmountImage = findViewById(R.id.tvAmountImage);
        tvAddress = findViewById(R.id.tvAddress);
        tvHotelName = findViewById(R.id.tvHotelName);
        rcvImgRoom = findViewById(R.id.rcvImgRoom);
        rcvImgRoom.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcvImgRoom.setHasFixedSize(true);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2);
        rcvImgRoom.setLayoutParams(layoutParams);
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(rcvImgRoom);

        rcvImgRoom.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    if (rcvImgRoom.getLayoutManager() != null) {
                        int potition = (((LinearLayoutManager) rcvImgRoom.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
                        if (potition != -1 && hotelImageForms != null && hotelImageForms.size() > 0) {
                            tvAmountImage.setText(String.format("%d/%d", potition + 1, hotelImageForms.size()));
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        rcvRoomAdapter = findViewById(R.id.rcvRoomAdapter);
        rcvRoomAdapter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvImgRoom.setHasFixedSize(true);

        gethotelDetail();
        getImageHotel();
        getRoomHotel();
    }

    private void bookRoom() {
        if(roomTypeForms!=null&&roomTypeForms.size()>0){
            Intent intent = new Intent(HotelDetailActivity.this, ReservationActivity.class);
            intent.putExtra("hotelId", roomTypeForms.get(0).getHotelId());
            intent.putExtra("roomId", roomTypeForms.get(0).getId());
            startActivity(intent);
        }

    }


    private void gethotelDetail() {
        DialogLoadingProgress.getInstance().show(this);

        GoHotelApplication.serviceApi.getHotelDetail(hotelId).enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<HotelForm> hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        handleDataHotel(hotelForms.get(0));
                    }
                } else {
                    Toast.makeText(HotelDetailActivity.this, "Không thể lấy thông tin khách sạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }
// update get image
    private void getImageHotel() {
        DialogLoadingProgress.getInstance().show(this);

        GoHotelApplication.serviceApi.getImageHotel(hotelId).enqueue(new Callback<List<HotelImageForm>>() {
            @Override
            public void onResponse(Call<List<HotelImageForm>> call, Response<List<HotelImageForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<HotelImageForm> hotelImageForms = response.body();
                    if (hotelImageForms != null && hotelImageForms.size() > 0) {
                        handleImageHotel(hotelImageForms);
                    }
                } else {
                    Toast.makeText(HotelDetailActivity.this, "Không thể lấy hình khách sạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HotelImageForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    private void getRoomHotel() {
        DialogLoadingProgress.getInstance().show(this);

        GoHotelApplication.serviceApi.getRoomTypeHotel(hotelId).enqueue(new Callback<List<RoomTypeForm>>() {
            @Override
            public void onResponse(Call<List<RoomTypeForm>> call, Response<List<RoomTypeForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<RoomTypeForm> roomTypeForms = response.body();
                    if (roomTypeForms != null && roomTypeForms.size() > 0) {
                        handleRoomTypeHotel(roomTypeForms);
                    }
                } else {
                    Toast.makeText(HotelDetailActivity.this, "Không thể lấy danh sách phòng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RoomTypeForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    private void handleRoomTypeHotel(List<RoomTypeForm> roomTypeForms) {
        this.roomTypeForms = roomTypeForms;
        roomTypeAdapter = new RoomTypeAdapter(this, roomTypeForms, new RoomTypeAdapter.RoomtypeCallback() {
            @Override
            public void onItemClick(RoomTypeForm roomTypeForm) {
                gotoReservation(roomTypeForm);
            }
        });
        rcvRoomAdapter.setAdapter(roomTypeAdapter);
    }

    private void gotoReservation(RoomTypeForm roomTypeForm) {
        Intent intent = new Intent(HotelDetailActivity.this, ReservationActivity.class);
        intent.putExtra("hotelId", roomTypeForm.getHotelId());
        intent.putExtra("roomId", roomTypeForm.getId());
        startActivity(intent);
    }

    @SuppressLint("DefaultLocale")
    private void handleImageHotel(List<HotelImageForm> hotelImageForms) {
        this.hotelImageForms = hotelImageForms;
        adapter = new RoomImageDetailAdapter(this, hotelImageForms);
        rcvImgRoom.setAdapter(adapter);
        tvAmountImage.setText(String.format("1/%d", hotelImageForms.size()));
    }

    private void handleDataHotel(HotelForm hotelForm) {
        tvHotelName.setText(hotelForm.getNameHotel());
        tvAddress.setText(hotelForm.getAddress());
    }

}
