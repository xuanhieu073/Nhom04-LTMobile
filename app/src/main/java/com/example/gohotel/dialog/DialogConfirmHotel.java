package com.example.gohotel.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gohotel.GoHotelApplication;
import com.example.gohotel.R;
import com.example.gohotel.model.api.BookRes;
import com.example.gohotel.model.api.BookingUserForm;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogConfirmHotel {
    public void showConfirmHotel(final Activity activity, BookingUserForm bookingUserForm) {
        if (activity != null && !activity.isFinishing()) {
            final Dialog dialog = new Dialog(activity, R.style.dialog_full_transparent_background);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.confirm_hotel_dialog);
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();
            }

            TextView tvHotelName = dialog.findViewById(R.id.tvHotelName);
            tvHotelName.setText(bookingUserForm.getNameHotel());
            ImageView imgHotel = dialog.findViewById(R.id.imgHotel);
            Glide.with(activity)
                    .load(bookingUserForm.getLinkImage())
                    .apply(RequestOptions.circleCropTransform()
                            .error(R.drawable.loading_big)
                            .placeholder(R.drawable.loading_big))
                    .into(imgHotel);
            TextView btnCheckIn = dialog.findViewById(R.id.btnCheckIn);
            btnCheckIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkInBooking(bookingUserForm.getIdBook());
                    dialog.dismiss();
                }
            });
            TextView btnNoShow = dialog.findViewById(R.id.btnNoShow);
            btnNoShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelBooking(bookingUserForm.getIdBook());
                    dialog.dismiss();
                }
            });
        }
    }

    private void cancelBooking(int bookingId) {
        GoHotelApplication.serviceApi.updateBookingDetail(bookingId, -1).enqueue(new Callback<BookRes>() {
            @Override
            public void onResponse(Call<BookRes> call, Response<BookRes> response) {

            }

            @Override
            public void onFailure(Call<BookRes> call, Throwable t) {

            }
        });
    }

    private void checkInBooking(int bookingId) {
        GoHotelApplication.serviceApi.updateBookingDetail(bookingId, 1).enqueue(new Callback<BookRes>() {
            @Override
            public void onResponse(Call<BookRes> call, Response<BookRes> response) {

            }

            @Override
            public void onFailure(Call<BookRes> call, Throwable t) {

            }
        });
    }

}
