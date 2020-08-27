package com.example.gohotel.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gohotel.Enums.TypeFragment;
import com.example.gohotel.Fragments.HomeFragment;
import com.example.gohotel.Fragments.MapFragment;
import com.example.gohotel.Fragments.MyPageFragment;
import com.example.gohotel.Fragments.SearchFragment;
import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.dialog.DialogConfirmHotel;
import com.example.gohotel.dialog.DialogLoadingProgress;
import com.example.gohotel.dialog.DialogReviewHotel;
import com.example.gohotel.model.api.BookingUserForm;
import com.example.gohotel.utils.ParamConstants;
import com.example.gohotel.utils.PreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Resources resources;
    private RelativeLayout rltHome, rltSearch, rltMap, rltAccount;
    private ImageView imgHome, imgSearch, imgMap, imgAccount;
    private TextView tvHome, tvSearch, tvMap, tvAccount;
    private String address;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private MapFragment mapFragment;
    private MyPageFragment myPageFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        resources = getResources();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(resources.getColor(R.color.colorWhite));
            }
        }
        if (getIntent() != null && getIntent().getExtras() != null) {
            address = getIntent().getExtras().getString("address");
        }

        addViews();
        changeTab(TypeFragment.HOME.getType());
        //addFragment(savedInstanceState);

        getBookingDetailByStatus();
        getBookingNotCheckInByStatus();
    }

    private void getBookingNotCheckInByStatus() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -2);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        DialogLoadingProgress.getInstance().show(this);

        GoHotelApplication.serviceApi.getBookingDetailByStatus(GoHotelApplication.DEVICE_ID, 0, yesterday).enqueue(new Callback<List<BookingUserForm>>() {
            @Override
            public void onResponse(Call<List<BookingUserForm>> call, Response<List<BookingUserForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<BookingUserForm> bookingUserForms = response.body();
                    if (bookingUserForms != null && bookingUserForms.size() > 0) {
                        handleDialogConfirm(bookingUserForms);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BookingUserForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    private void handleDialogConfirm(List<BookingUserForm> bookingUserForms) {
        int idBook = -1;

        for (int i = 0; i < bookingUserForms.size(); i++) {
            if (idBook != bookingUserForms.get(i).getIdBook() ) {
                DialogConfirmHotel dialogReviewHotel = new DialogConfirmHotel();
                dialogReviewHotel.showConfirmHotel(this, bookingUserForms.get(i));
                idBook = bookingUserForms.get(i).getIdBook();
            }
        }
    }

    private void addViews() {
        rltHome = findViewById(R.id.rltHome);
        rltSearch = findViewById(R.id.rltSearch);
        rltMap = findViewById(R.id.rltMap);
        rltAccount = findViewById(R.id.rltAccount);

        imgHome = findViewById(R.id.imgHome);
        imgSearch = findViewById(R.id.imgSearch);
        imgMap = findViewById(R.id.imgMap);
        imgAccount = findViewById(R.id.imgAccount);

        tvHome = findViewById(R.id.tvHome);
        tvSearch = findViewById(R.id.tvSearch);
        tvMap = findViewById(R.id.tvMap);
        tvAccount = findViewById(R.id.tvAccount);

        addEvents();
    }

    private void addEvents() {
        rltHome.setOnClickListener(this);
        rltSearch.setOnClickListener(this);
        rltMap.setOnClickListener(this);
        rltAccount.setOnClickListener(this);

    }

    private void getBookingDetailByStatus() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        DialogLoadingProgress.getInstance().show(this);

        GoHotelApplication.serviceApi.getBookingDetailByStatus(GoHotelApplication.DEVICE_ID, 1, yesterday).enqueue(new Callback<List<BookingUserForm>>() {
            @Override
            public void onResponse(Call<List<BookingUserForm>> call, Response<List<BookingUserForm>> response) {
                DialogLoadingProgress.getInstance().hide();

                if (response.code() == 200) {
                    List<BookingUserForm> bookingUserForms = response.body();
                    if (bookingUserForms != null && bookingUserForms.size() > 0) {
                        handleDialogCheckIn(bookingUserForms);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BookingUserForm>> call, Throwable t) {
                DialogLoadingProgress.getInstance().hide();

            }
        });
    }

    private void handleDialogCheckIn(List<BookingUserForm> bookingUserForms) {
        int idBook = -1;
        for (int i = 0; i < bookingUserForms.size(); i++) {
            if (idBook != bookingUserForms.get(i).getIdBook() && bookingUserForms.get(i).getReviewed() == null) {
                DialogReviewHotel dialogReviewHotel = new DialogReviewHotel();
//                dialogReviewHotel.showRatingReviewHotel(this, bookingUserForms.get(i));
                idBook = bookingUserForms.get(i).getIdBook();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rltHome:
                changeTab(TypeFragment.HOME.getType());
                break;
            case R.id.rltSearch:
                changeTab(TypeFragment.SEARCH.getType());
                break;
            case R.id.rltMap:
                changeTab(TypeFragment.MAP.getType());
                break;
            case R.id.rltAccount:
                changeTab(TypeFragment.ACCOUNT.getType());
                break;
        }
    }

    public void changeTab(int potition) {

        if (potition == TypeFragment.HOME.getType()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
                }
            }

            imgHome.setImageResource(R.drawable.home_selected);
            imgSearch.setImageResource(R.drawable.search);
            imgMap.setImageResource(R.drawable.map);
            imgAccount.setImageResource(R.drawable.user);

            tvHome.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvSearch.setTextColor(getResources().getColor(R.color.colorDefault));
            tvMap.setTextColor(getResources().getColor(R.color.colorDefault));
            tvAccount.setTextColor(getResources().getColor(R.color.colorDefault));

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (homeFragment == null) {
                homeFragment = HomeFragment.newInstance(address);
            }
            if (homeFragment.isAdded()) { // if the fragment is already in container
                ft.show(homeFragment);
            } else { // fragment needs to be added to frame container
                ft.add(R.id.frLayout, homeFragment, "homeFragment");
            }
            // Hide fragment B
            if (mapFragment == null) {
                mapFragment = MapFragment.newInstance(address);
            }
            if (mapFragment.isAdded()) {
                ft.hide(mapFragment);
            }
            // Hide fragment C
            if (searchFragment == null) {
                searchFragment = SearchFragment.newInstance();
            }
            if (searchFragment.isAdded()) {
                ft.hide(searchFragment);
            }

            if (myPageFragment == null) {
                myPageFragment = MyPageFragment.newInstance();
            }
            if (myPageFragment.isAdded()) {
                ft.hide(myPageFragment);
            }
            // Commit changes
            ft.commit();

        } else if (potition == TypeFragment.MAP.getType()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
                }
            }

            imgHome.setImageResource(R.drawable.home);
            imgSearch.setImageResource(R.drawable.search);
            imgMap.setImageResource(R.drawable.map_selected);
            imgAccount.setImageResource(R.drawable.user);

            tvHome.setTextColor(getResources().getColor(R.color.colorDefault));
            tvSearch.setTextColor(getResources().getColor(R.color.colorDefault));
            tvMap.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvAccount.setTextColor(getResources().getColor(R.color.colorDefault));

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (mapFragment == null) {
                mapFragment = MapFragment.newInstance(address);
            }
            if (mapFragment.isAdded()) { // if the fragment is already in container
                ft.show(mapFragment);
            } else { // fragment needs to be added to frame container
                ft.add(R.id.frLayout, mapFragment, "mapFragment");
            }
            // Hide fragment B
            if (homeFragment == null) {
                homeFragment = HomeFragment.newInstance(address);
            }
            if (homeFragment.isAdded()) {
                ft.hide(homeFragment);
            }
            // Hide fragment C
            if (searchFragment == null) {
                searchFragment = SearchFragment.newInstance();
            }
            if (searchFragment.isAdded()) {
                ft.hide(searchFragment);
            }

            if (myPageFragment == null) {
                myPageFragment = MyPageFragment.newInstance();
            }
            if (myPageFragment.isAdded()) {
                ft.hide(myPageFragment);
            }
            // Commit changes
            ft.commit();

        } else if (potition == TypeFragment.SEARCH.getType()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
                }
            }

            imgHome.setImageResource(R.drawable.home);
            imgSearch.setImageResource(R.drawable.search_selected);
            imgMap.setImageResource(R.drawable.map);
            imgAccount.setImageResource(R.drawable.user);

            tvHome.setTextColor(getResources().getColor(R.color.colorDefault));
            tvSearch.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvMap.setTextColor(getResources().getColor(R.color.colorDefault));
            tvAccount.setTextColor(getResources().getColor(R.color.colorDefault));

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (searchFragment == null) {
                searchFragment = SearchFragment.newInstance();
            }
            if (searchFragment.isAdded()) { // if the fragment is already in container
                ft.show(searchFragment);
            } else { // fragment needs to be added to frame container
                ft.add(R.id.frLayout, searchFragment, "searchFragment");
            }
            // Hide fragment B
            if (homeFragment == null) {
                homeFragment = HomeFragment.newInstance(address);
            }
            if (homeFragment.isAdded()) {
                ft.hide(homeFragment);
            }
            // Hide fragment C
            if (mapFragment == null) {
                mapFragment = MapFragment.newInstance(address);
            }
            if (mapFragment.isAdded()) {
                ft.hide(mapFragment);
            }
            if (myPageFragment == null) {
                myPageFragment = MyPageFragment.newInstance();
            }
            if (myPageFragment.isAdded()) {
                ft.hide(myPageFragment);
            }
            // Commit changes
            ft.commit();
        } else {

            if (PreferenceUtils.getToken(this).isEmpty()) {
                gotoLogin();
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            imgHome.setImageResource(R.drawable.home);
            imgSearch.setImageResource(R.drawable.search);
            imgMap.setImageResource(R.drawable.map);
            imgAccount.setImageResource(R.drawable.user_selected);

            tvHome.setTextColor(getResources().getColor(R.color.colorDefault));
            tvSearch.setTextColor(getResources().getColor(R.color.colorDefault));
            tvMap.setTextColor(getResources().getColor(R.color.colorDefault));
            tvAccount.setTextColor(getResources().getColor(R.color.colorPrimary));

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (myPageFragment == null) {
                myPageFragment = MyPageFragment.newInstance();
            }
            if (myPageFragment.isAdded()) { // if the fragment is already in container
                ft.show(myPageFragment);
            } else { // fragment needs to be added to frame container
                ft.add(R.id.frLayout, myPageFragment, "myPageFragment");
            }
            // Hide fragment B
            if (homeFragment == null) {
                homeFragment = HomeFragment.newInstance(address);
            }
            if (homeFragment.isAdded()) {
                ft.hide(homeFragment);
            }
            // Hide fragment C
            if (mapFragment == null) {
                mapFragment = MapFragment.newInstance(address);
            }
            if (mapFragment.isAdded()) {
                ft.hide(mapFragment);
            }

            if (searchFragment == null) {
                searchFragment = SearchFragment.newInstance();
            }
            if (searchFragment.isAdded()) {
                ft.hide(searchFragment);
            }
            // Commit changes
            ft.commit();
        }
    }

    private void gotoLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, ParamConstants.REQUEST_MAIN_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ParamConstants.REQUEST_SORT_FILTER:
                if (RESULT_OK == resultCode) {
                    if (data != null && data.getExtras() != null) {
                        Bundle bundle = data.getExtras();
                        int typeSort = bundle.getInt("typeSort", 0);
                        int priceStart = bundle.getInt("priceStart", 0);
                        int priceEnd = bundle.getInt("priceEnd", 3000000);
                        homeFragment.setPriceStart(priceStart);
                        homeFragment.setPriceEnd(priceEnd);
                        homeFragment.setTypeSort(typeSort);
                        homeFragment.resetHotel();
                        if (typeSort == 0)
                            homeFragment.getHotelByDistance();
                        else if (typeSort == 1)
                            homeFragment.getHotelByPriceASC();
                        else if (typeSort == 2)
                            homeFragment.getHotelByPriceDESC();
                        else if (typeSort == 3)
                            homeFragment.getHotelByRating();
                    }
                }
                break;
            case ParamConstants.REQUEST_CHOOSE_AREA_HOME:
                if (RESULT_OK == resultCode) {
                    if (data != null && data.getExtras() != null) {
                        //bundle là chứa dữ liệu đã duoc put vào intent
                        Bundle bundle = data.getExtras();
                        int city = bundle.getInt("cityKey", -1);
                        String cityName = bundle.getString("cityName", "");
                        int district = bundle.getInt("districtKey", -1);
                        String districtName = bundle.getString("districtName", "");
                        homeFragment.resetHotel();
                        if (city != -1 && !cityName.isEmpty() && district != -1 && !districtName.isEmpty()) {
                            homeFragment.setCity(city);
                            homeFragment.setProvine(districtName);
                            homeFragment.setDistrict(district);
                        } else if (city != -1 && !cityName.isEmpty()) {
                            homeFragment.setCity(city);
                            homeFragment.setProvine(cityName);
                            homeFragment.setDistrict(0);
                        } else {
                            homeFragment.setProvine("Tất cả hotel");
                            homeFragment.setCity(0);
                            homeFragment.setDistrict(0);
                        }

                    } else {
                        homeFragment.resetHotel();
                        homeFragment.setProvine("Tất cả hotel");
                        homeFragment.setCity(0);
                        homeFragment.setDistrict(0);
                    }
                    int typeSort = homeFragment.getTypeSort();
                    if (typeSort == 0)
                        homeFragment.getHotelByDistance();
                    else if (typeSort == 1)
                        homeFragment.getHotelByPriceASC();
                    else if (typeSort == 2)
                        homeFragment.getHotelByPriceDESC();
                    else if (typeSort == 3)
                        homeFragment.getHotelByRating();
                }
                break;
            case ParamConstants.REQUEST_CHOOSE_AREA_MAP:
                if (RESULT_OK == resultCode) {
                    if (data != null && data.getExtras() != null) {
                        Bundle bundle = data.getExtras();
                        int city = bundle.getInt("cityKey", -1);
                        String cityName = bundle.getString("cityName", "");
                        int district = bundle.getInt("districtKey", -1);
                        String districtName = bundle.getString("districtName", "");
                        if (city != -1 && !cityName.isEmpty() && district != -1 && !districtName.isEmpty())
                            mapFragment.getHotelCityDistrict(city, district, districtName);
                        else if (city != -1 && !cityName.isEmpty())
                            mapFragment.getHotelCity(city, cityName);
                    }
                }
                break;
            case ParamConstants.REQUEST_MAIN_LOGIN:
                if (RESULT_OK == resultCode) {
                    changeTab(TypeFragment.MYPAGE.getType());
                }
                break;
            case ParamConstants.REQUEST_CHANGE_PASS:
                if (RESULT_OK == resultCode) {
                    PreferenceUtils.setUserInfo(this, "");
                    PreferenceUtils.setToken(this, "");
                    changeTab(TypeFragment.MYPAGE.getType());
                }
                break;
        }
    }
}
