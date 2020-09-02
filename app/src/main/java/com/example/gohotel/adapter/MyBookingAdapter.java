package com.example.gohotel.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gohotel.R;
import com.example.gohotel.activity.BookingDetail;
import com.example.gohotel.activity.BookingList;
import com.example.gohotel.model.api.BookingUserForm;
import com.example.gohotel.utils.AppTimeUtils;
import com.example.gohotel.utils.Utils;

import java.util.List;

public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.Viewholder> {
    private Context context;
    private List<BookingUserForm> bookingUserForms;


    public MyBookingAdapter(Context context, List<BookingUserForm> bookingUserForms) {
        this.context = context;
        this.bookingUserForms = bookingUserForms;
    }

    // hàm tạo layout
    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_booking_adapter, parent, false);
        return new Viewholder(view);
    }

    //hàm set giá trị vào layout
    @Override
    public void onBindViewHolder(Viewholder holder, int position) {

        BookingUserForm bookingForm = bookingUserForms.get(position);
        if (bookingForm != null) {
            // set ten hotel
            holder.tvHotelName.setText(bookingForm.getNameHotel());
            //set thoi gian book
            holder.tvDate.setText(AppTimeUtils.changeDateShowClient(bookingForm.getTimeBook()));
            //set ten phòng
            holder.tvRoomName.setText(bookingForm.getNameRoom());
            //set giá tiền
            holder.tvFee.setText(String.format("%s VND", Utils.formatCurrency(bookingForm.getPrice())));
            // set loại booking
            holder.tvBookingType.setText("Theo ngày");
            holder.tvBookingStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            //set trang thai booking
            if (bookingForm.getStatus() == 0) {
                holder.tvBookingStatus.setText("Đã đặt");
            } else if (bookingForm.getStatus() == -1)
                holder.tvBookingStatus.setText("Đã hủy");
            else if (bookingForm.getStatus() == 1)
                holder.tvBookingStatus.setText("Đã nhận phòng");
            // sự kiện user nhấn vào dòng
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //gọi qua màn hình booking detail truyền vào bookingid
                    Intent intent=new Intent(context, BookingDetail.class);
                    intent.putExtra("BookingID",bookingForm.getIdBook());
                    context.startActivity(intent);
                }
            });
        }
    }

    //get số lượng item của list
    @Override
    public int getItemCount() {
        return bookingUserForms.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        TextView tvHotelName, tvDate, tvRoomName, tvFee, tvBookingType, tvBookingStatus;

        Viewholder(View itemView) {
            super(itemView);
            tvHotelName = itemView.findViewById(R.id.tvHotelName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvFee = itemView.findViewById(R.id.tvFee);
            tvBookingType = itemView.findViewById(R.id.tvBookingType);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);
        }
    }
}
