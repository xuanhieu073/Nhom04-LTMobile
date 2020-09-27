package com.example.gohotel.activity;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.adapter.SearchAdapter;
import com.example.gohotel.model.HotelForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rcvSearchHotel;
    private TextView btnBack;
    private List<HotelForm> hotelForms;
    private Resources resources;
    private EditText edtSearch;
    private int offset;
    private String keyword = "";
    private ProgressBar progressBar;

    @Override
	<!--Create by: Thắm-->
	<!--Description: SearchHotel-->
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        resources = getResources();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(resources.getColor(R.color.colorWhite));
            }
        }

        if (getIntent() != null && getIntent().getExtras() != null)
            keyword = getIntent().getExtras().getString("keyWord", "");

        rcvSearchHotel = findViewById(R.id.rcvSearchHotel);
        rcvSearchHotel.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcvSearchHotel.setHasFixedSize(true);
        rcvSearchHotel.setAdapter(new SearchAdapter(this, hotelForms));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        edtSearch = findViewById(R.id.edtSearch);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            private Timer timer = new Timer();
            private final long DELAY = 500;

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.toString().length() >= 110) {
                    return;
                }
                if (!s.toString().isEmpty()) {
                    offset = 0;
                    progressBar.setVisibility(View.VISIBLE);
                    rcvSearchHotel.setVisibility(View.GONE);
                } else {
                    /*Show Icon*/
                    hotelForms = new ArrayList<>();
                    progressBar.setVisibility(View.GONE);

                    /*Show RecyclerView*/
                    rcvSearchHotel.setVisibility(View.VISIBLE);
                    rcvSearchHotel.setAdapter(new SearchAdapter(SearchActivity.this, hotelForms));
                    //findRecommend();
                    offset = 0;

                    //findRecommend();
                }

                if (timer != null)
                    timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                                   @Override
                                   public void run() {
                                       keyword = s.toString();
                                       searchHotel(keyword);

                                   }
                               }, DELAY
                );
            }
        });

        if (!keyword.isEmpty())
            edtSearch.setText(keyword);
    }

    private void searchHotel(String keyword) {
        suggestSearch(keyword, offset, GoHotelApplication.limit);
    }

    private void suggestSearch(String _keyword, final int _offset, int _limit) {
        if (_keyword.trim().isEmpty()) {
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
            });

            return;
        }

        GoHotelApplication.serviceApi.search(keyword).enqueue(new Callback<List<HotelForm>>() {
            @Override
            public void onResponse(Call<List<HotelForm>> call, Response<List<HotelForm>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.code() == 200) {
                    hotelForms = response.body();
                    if (hotelForms != null && hotelForms.size() > 0) {
                        rcvSearchHotel.setVisibility(View.VISIBLE);

                        rcvSearchHotel.setAdapter(new SearchAdapter(SearchActivity.this, hotelForms));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HotelForm>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }
}
