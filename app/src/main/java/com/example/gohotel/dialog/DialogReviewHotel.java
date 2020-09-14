package com.example.gohotel.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.model.api.BookRes;
import com.example.gohotel.model.api.BookingUserForm;
import com.example.gohotel.model.api.UserInfo;
import com.example.gohotel.utils.PreferenceUtils;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogReviewHotel {
    private ImageView[] btnStars = new ImageView[5];
    private int start = 5;

    public void showRatingReviewHotel(final Activity activity, BookingUserForm bookingUserForm) {
        if (activity != null && !activity.isFinishing()) {
            final Dialog dialog = new Dialog(activity, R.style.dialog_full_transparent_background);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.rate_review_dialog);
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();
            }
            EditText txtContent = dialog.findViewById(R.id.txtContent);
            TextView btnSubmit = dialog.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userStr = PreferenceUtils.getUserInfo(activity);
                    UserInfo userInfo = new Gson().fromJson(userStr, UserInfo.class);
                    String phone = "";
                    if (userInfo != null)
                        phone = userInfo.getNumberPhone();
                    reviewHotel(activity,bookingUserForm.getHotelId(), bookingUserForm.getRoomId(), phone, txtContent.getText().toString(), start);
                    dialog.dismiss();
                }
            });
            TextView tvHotelName = dialog.findViewById(R.id.tvHotelName);
            tvHotelName.setText(bookingUserForm.getNameHotel());
            ImageView imgHotel = dialog.findViewById(R.id.imgHotel);
            btnStars[0] = dialog.findViewById(R.id.btnStar1);
            btnStars[1] = dialog.findViewById(R.id.btnStar2);
            btnStars[2] = dialog.findViewById(R.id.btnStar3);
            btnStars[3] = dialog.findViewById(R.id.btnStar4);
            btnStars[4] = dialog.findViewById(R.id.btnStar5);
            btnStars[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showStar(0);
                }
            });
            btnStars[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showStar(1);

                }
            });
            btnStars[2].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showStar(2);

                }
            });
            btnStars[3].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showStar(3);

                }
            });
            btnStars[4].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showStar(4);

                }
            });
            showStar(4);
            Glide.with(activity)
                    .load(bookingUserForm.getLinkImage())
                    .apply(RequestOptions.circleCropTransform()
                            .error(R.drawable.loading_big)
                            .placeholder(R.drawable.loading_big))
                    .into(imgHotel);
        }
    }

    private void showStar(int starIndex) {
        start = starIndex+1;
        for (ImageView btnStar : btnStars) {
            btnStar.setImageResource(R.drawable.review_star);
        }
        for (int i = 0; i <= starIndex; i++) {
            btnStars[i].setImageResource(R.drawable.star);
        }

    }

    public void reviewHotel(Context context, int hotel, int room, String user, String commet, int star) {
        GoHotelApplication.serviceApi.reviewHotel(PreferenceUtils.getToken(context),hotel, room, user, commet, star).enqueue(new Callback<BookRes>() {
            @Override
            public void onResponse(Call<BookRes> call, Response<BookRes> response) {

            }

            @Override
            public void onFailure(Call<BookRes> call, Throwable t) {

            }
        });
    }
}
