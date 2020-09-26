package com.example.gohotel.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gohotel.R;
import com.example.gohotel.utils.Utils;
import com.example.gohotel.widgets.OnRangeChangedListener;
import com.example.gohotel.widgets.RangeSeekBar;

public class SortFilterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView btnApply;
    private Resources resources;
    private ImageView btnBack;
    private TextView tvDistance, tvPriceAsc, tvPriceDesc, tvRanting;
    private ImageView imgDistance, imgPriceAsc, imgPriceDesc, imgRating;
    private LinearLayout btnDistance, btnPriceAsc, btnPriceDesc, btnRating;
    private RangeSeekBar sbRange;
    private TextView tvPriceTo;
    private TextView tvPriceFrom, btnClear;
    private int typeSort;
    private int priceStart, priceEnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort_filter_activity);
        resources = getResources();
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        tvDistance = findViewById(R.id.tvDistance);
        tvPriceAsc = findViewById(R.id.tvPriceAsc);
        tvPriceDesc = findViewById(R.id.tvPriceDesc);
        tvRanting = findViewById(R.id.tvRanting);
        imgDistance = findViewById(R.id.imgDistance);
        imgPriceAsc = findViewById(R.id.imgPriceAsc);
        imgPriceDesc = findViewById(R.id.imgPriceDesc);
        imgRating = findViewById(R.id.imgRating);
        btnApply = findViewById(R.id.btnApply);
        btnApply.setOnClickListener(this);
        btnDistance = findViewById(R.id.btnDistance);
        btnDistance.setOnClickListener(this);
        btnPriceAsc = findViewById(R.id.btnPriceAsc);
        btnPriceAsc.setOnClickListener(this);
        btnPriceDesc = findViewById(R.id.btnPriceDesc);
        btnPriceDesc.setOnClickListener(this);
        btnRating = findViewById(R.id.btnRating);
        btnRating.setOnClickListener(this);
        sbRange = findViewById(R.id.sbRange);
        sbRange.setRange(0, 30);
        sbRange.setValue(0, 30);
        priceEnd = 3000000;
        // bắt sự kiện thay đổi giá
		<!--Create by: Tham-->
		<!--Description: ChangedListener-->
        sbRange.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                if (isFromUser) {
                    priceStart = ((int) leftValue) * 100000;
                    priceEnd = ((int) rightValue) * 100000;
                    tvPriceFrom.setText(Utils.formatCurrency(((int) leftValue) * 100000));
                    tvPriceTo.setText(Utils.formatCurrency((int) rightValue * 100000));
                }
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
            }
        });
        tvPriceFrom = findViewById(R.id.tvPriceFrom);
        tvPriceTo = findViewById(R.id.tvPriceTo);
        btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(resources.getColor(R.color.colorWhite));
            }
        }
    }

    private void chooseSort(int i) {
        switch (i) {
            case 0:
                typeSort = 0;
                tvDistance.setTextColor(resources.getColor(R.color.colorPrimary));
                tvPriceAsc.setTextColor(resources.getColor(R.color.colorDefault));
                tvPriceDesc.setTextColor(resources.getColor(R.color.colorDefault));
                tvRanting.setTextColor(resources.getColor(R.color.colorDefault));

                imgDistance.setImageResource(R.drawable.verified_selected);
                imgPriceAsc.setImageResource(R.drawable.verified);
                imgPriceDesc.setImageResource(R.drawable.verified);
                imgRating.setImageResource(R.drawable.verified);

                break;
            case 1:
                typeSort = 1;
                tvPriceAsc.setTextColor(resources.getColor(R.color.colorPrimary));
                tvDistance.setTextColor(resources.getColor(R.color.colorDefault));
                tvPriceDesc.setTextColor(resources.getColor(R.color.colorDefault));
                tvRanting.setTextColor(resources.getColor(R.color.colorDefault));

                imgPriceAsc.setImageResource(R.drawable.verified_selected);
                imgDistance.setImageResource(R.drawable.verified);
                imgPriceDesc.setImageResource(R.drawable.verified);
                imgRating.setImageResource(R.drawable.verified);

                break;
            case 2:
                typeSort = 2;
                tvPriceDesc.setTextColor(resources.getColor(R.color.colorPrimary));
                tvPriceAsc.setTextColor(resources.getColor(R.color.colorDefault));
                tvDistance.setTextColor(resources.getColor(R.color.colorDefault));
                tvRanting.setTextColor(resources.getColor(R.color.colorDefault));

                imgPriceDesc.setImageResource(R.drawable.verified_selected);
                imgPriceAsc.setImageResource(R.drawable.verified);
                imgDistance.setImageResource(R.drawable.verified);
                imgRating.setImageResource(R.drawable.verified);

                break;
            case 3:
                typeSort = 3;
                tvRanting.setTextColor(resources.getColor(R.color.colorPrimary));
                tvPriceAsc.setTextColor(resources.getColor(R.color.colorDefault));
                tvPriceDesc.setTextColor(resources.getColor(R.color.colorDefault));
                tvDistance.setTextColor(resources.getColor(R.color.colorDefault));

                imgRating.setImageResource(R.drawable.verified_selected);
                imgPriceAsc.setImageResource(R.drawable.verified);
                imgPriceDesc.setImageResource(R.drawable.verified);
                imgDistance.setImageResource(R.drawable.verified);

                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnApply:
                apply();
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnDistance:
                chooseSort(0);
                break;
            case R.id.btnPriceAsc:
                chooseSort(1);
                break;
            case R.id.btnPriceDesc:
                chooseSort(2);
                break;
            case R.id.btnRating:
                chooseSort(3);
                break;
            case R.id.btnClear:
                chooseSort(0);
                sbRange.setValue(0, 30);
                tvPriceFrom.setText(Utils.formatCurrency(0));
                tvPriceTo.setText(Utils.formatCurrency(30 * 100000));
                priceStart = 0;
                priceEnd = 30 * 100000;
                break;
        }
    }

    private void apply() {
        Intent intent = new Intent();
        intent.putExtra("typeSort", typeSort);
        intent.putExtra("priceStart", priceStart);
        intent.putExtra("priceEnd", priceEnd);
        setResult(RESULT_OK, intent);
        finish();
    }
}
