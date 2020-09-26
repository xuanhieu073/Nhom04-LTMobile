package com.example.gohotel.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.adapter.DistrictAdapter;
import com.example.gohotel.adapter.ProvinceAdapter;
import com.example.gohotel.dialog.DialogLoadingProgress;
import com.example.gohotel.model.api.CityForm;
import com.example.gohotel.model.api.DistrictForm;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseAreaActivity extends AppCompatActivity {

    RecyclerView lvProvinces, lvHotelArea;
    ProvinceAdapter provinceAdapter;
    DistrictAdapter districtAdapter;
    TextView btnApply;
    private DistrictForm district;
    private CityForm city;
    private ImageView btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_setting_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
        }

        addview();
        getListCity();

    }

    private void getListCity() {
        // lấy danh sách thành
        DialogLoadingProgress.getInstance().show(this);

        GoHotelApplication.serviceApi.getCity().enqueue(new Callback<List<CityForm>>() {
            @Override
            public void onResponse(Call<List<CityForm>> call, Response<List<CityForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    //danh sach thành phó lưu vào body
                    List<CityForm> cityForms = response.body();
                    if (cityForms != null && cityForms.size() > 0) {
                        // hàm xử lý danh sách thành phố
                        handleListCity(cityForms);
                    }
                } else {
                    Toast.makeText(ChooseAreaActivity.this, "Không thể lấy danh sách thành phố", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CityForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    private void handleListCity(List<CityForm> cityForms) {
        //chọn thành phố đứng đầu
        cityForms.get(0).setClicked(true);
        // city dùng để xác định thành phố nào dc chọn
        city = cityForms.get(0);
        provinceAdapter = new ProvinceAdapter(this, cityForms, cityForm -> {
            //onclick
            // lấy danh sách quận huyện
            getDistrict(cityForm.getKey());
            provinceAdapter.notifyDataSetChanged();
            city = cityForm;
        });
        lvProvinces.setAdapter(provinceAdapter);
        getDistrict(cityForms.get(0).getKey());
    }

    private void handleListDistrict(List<DistrictForm> districtForms) {
        // xử lý danh sách quận huyện
        districtAdapter = new DistrictAdapter(this, districtForms, new DistrictAdapter.OnItemClick() {

            @Override
            public void onClick(DistrictForm districtForm) {
                // onclick
                district = districtForm;
                // bắt adater khi nó thay đổi
                districtAdapter.notifyDataSetChanged();
            }
        });
        lvHotelArea.setAdapter(districtAdapter);
    }

    private void getDistrict(int provine) {
        // api để lấy quận huyện
        DialogLoadingProgress.getInstance().show(this);
        GoHotelApplication.serviceApi.accordingToCityId(provine).enqueue(new Callback<List<DistrictForm>>() {
            @Override
            public void onResponse(Call<List<DistrictForm>> call, Response<List<DistrictForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<DistrictForm> districtForms = response.body();
                    if (districtForms != null && districtForms.size() > 0) {
                        handleListDistrict(districtForms);
                    }
                } else {
                    Toast.makeText(ChooseAreaActivity.this, "Không thể lấy danh sách thành phố", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<DistrictForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    // ánh xạ layout để xử lý
    private void addview() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> onBackPressed());
        btnApply = findViewById(R.id.btnApply);
        lvProvinces = findViewById(R.id.lvProvinces);
        lvProvinces.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvProvinces.setHasFixedSize(true);

        lvHotelArea = findViewById(R.id.lvHotelArea);
        lvHotelArea.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvHotelArea.setHasFixedSize(true);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleApply();
            }
        });
    }

    private void handleApply() {
        // xử lý nút đồng ý
        Intent intent = new Intent();
        if (city != null && city.isClicked()) {
            intent.putExtra("cityName", city.getName());
            intent.putExtra("cityKey", city.getKey());
        }
        if (district != null && district.isClicked()) {
            intent.putExtra("districtName", district.getName());
            intent.putExtra("districtKey", district.getKey());
        }
        // result code = resut ok
        setResult(RESULT_OK, intent);
        finish();
    }


}
