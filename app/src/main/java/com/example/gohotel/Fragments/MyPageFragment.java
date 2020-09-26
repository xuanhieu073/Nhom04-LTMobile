package com.example.gohotel.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gohotel.Enums.TypeFragment;
import com.example.gohotel.R;
import com.example.gohotel.activity.BookingList;
import com.example.gohotel.activity.ChangPasswordActivity;
import com.example.gohotel.activity.EditProfileActivity;
import com.example.gohotel.activity.MainActivity;
import com.example.gohotel.model.api.UserInfo;
import com.example.gohotel.utils.ParamConstants;
import com.example.gohotel.utils.PreferenceUtils;
import com.google.gson.Gson;

public class MyPageFragment extends Fragment {
    private Context context;
    private String userInfo;
    private UserInfo user;
    private TextView tvUserId, tvPhone;
    private LinearLayout btnLogOut, btnMyBooking, btnMyProfile, btnChangePass;

    public static MyPageFragment newInstance() {
        MyPageFragment myFragment = new MyPageFragment();

        Bundle args = new Bundle();
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_page_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ((Activity)context).getWindow().setStatusBarColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }

        // lấy thông tin người dùng
        userInfo = PreferenceUtils.getUserInfo(context);
        //parse json thành user infor
        user = new Gson().fromJson(userInfo, UserInfo.class);
        tvUserId = rootView.findViewById(R.id.tvUserId);
        //set result vào id
        tvUserId.setText(user.getEmail());
        tvPhone = rootView.findViewById(R.id.tvPhone);
        tvPhone.setText(user.getNumberPhone());
        btnLogOut = rootView.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(view -> {
            //set token = rong
            //set userinfo=rong
            PreferenceUtils.setToken(context, "");
            PreferenceUtils.setUserInfo(context, "");
            //chuyển về màn hình home
            ((MainActivity) context).changeTab(TypeFragment.HOME.getType());
        });
        btnMyBooking = rootView.findViewById(R.id.btnMyBooking);
        btnMyBooking.setOnClickListener(view -> {
            //đi đến danh sách booking
            gotoBookingList();
        });
        btnMyProfile = rootView.findViewById(R.id.btnMyProfile);
        //di den thong tin user
        btnMyProfile.setOnClickListener(view -> gotoMyProfile());
        btnChangePass = rootView.findViewById(R.id.btnChangePass);
        btnChangePass.setOnClickListener(view -> gotoChangePass());
        return rootView;
    }

    private void gotoChangePass() {
        Intent intent = new Intent(getContext(), ChangPasswordActivity.class);
        intent.putExtra("phone", user.getNumberPhone());
        ((Activity) context).startActivityForResult(intent, ParamConstants.REQUEST_CHANGE_PASS);
    }

    private void gotoMyProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        ((Activity) context).startActivityForResult(intent, ParamConstants.REQUEST_CHANGE_PROFILE);
    }

    private void gotoBookingList() {
        Intent intent = new Intent(context, BookingList.class);
        startActivity(intent);
    }
}
