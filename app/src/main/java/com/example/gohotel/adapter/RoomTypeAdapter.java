package com.example.gohotel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.gohotel.R;
import com.example.gohotel.model.api.RoomTypeForm;
import com.example.gohotel.utils.Utils;

import java.util.List;

public class RoomTypeAdapter extends RecyclerView.Adapter<RoomTypeAdapter.ViewHolder> {

    private Context context;
    private List<RoomTypeForm> roomTypeForms;
    private RoomtypeCallback roomtypeCallback;

    public RoomTypeAdapter(Context context, List<RoomTypeForm> roomTypeForms, RoomtypeCallback roomtypeCallback) {
        this.context = context;
        this.roomTypeForms = roomTypeForms;
        this.roomtypeCallback = roomtypeCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.room_type_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomTypeForm roomTypeForm = roomTypeForms.get(position);
        holder.tvRoomName.setText(roomTypeForm.getName());
        holder.tvPrice.setText(String.format("%s VND", Utils.formatCurrency(roomTypeForm.getPricePerDay())));
        holder.tvSoPhong.setText(String.format("Số phòng : %d", roomTypeForm.getNumberOfRooms()));
        //dùng load hình ảnh
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.loading_big)
                .error(R.drawable.loading_big).skipMemoryCache(true);

        Glide.with(context)
                .load(roomTypeForm.getImage())
                .apply(requestOptions)
                .into(holder.imgHotel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomtypeCallback.onItemClick(roomTypeForm);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomTypeForms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvPrice, tvSoPhong;
        ImageView imgHotel;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSoPhong = itemView.findViewById(R.id.tvSoPhong);
            imgHotel = itemView.findViewById(R.id.imgHotel);

        }
    }

    public interface RoomtypeCallback {
        void onItemClick(RoomTypeForm roomTypeForm);
    }
}
