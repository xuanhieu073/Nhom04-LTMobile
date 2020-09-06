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
import com.example.gohotel.model.HotelForm;
import com.example.gohotel.utils.Utils;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {
    private Context context;
    private List<HotelForm> hotelForms;
    private HotelAdapterCallback hotelAdapterCallback;

    public HotelAdapter(Context context, List<HotelForm> hotelForms, HotelAdapterCallback hotelAdapterCallback) {
        this.context = context;
        this.hotelForms = hotelForms;
        this.hotelAdapterCallback = hotelAdapterCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hotel_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HotelForm hotelForm = hotelForms.get(position);
        holder.tvHotelName.setText(hotelForm.getNameHotel());
        holder.tvAddress.setText(hotelForm.getAddress());
        holder.tvPriceDiscount.setText(String.format("%s đồng", Utils.formatCurrency(hotelForm.getPriceRoomPerDay())));
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.loading_big)
                .error(R.drawable.loading_big);
        Glide.with(context).load(hotelForm.getNameImage()).apply(requestOptions).into(holder.imgHotel);
        float distance = Utils.calculateDistance(hotelForm, context);
        holder.tvDistance.setText(Utils.meterToKm(distance));
        holder.tvReview.setText(String.valueOf(hotelForm.getCountStar()));
        holder.tvCheckIn.setText(String.format("Giờ check-in ngày: từ %sh", hotelForm.getCheckIn()));
        holder.tvCheckOut.setText(String.format("Giờ check-out ngày: cho đến %sh", hotelForm.getCheckOut()));
        holder.itemView.setOnClickListener(view -> hotelAdapterCallback.onItemClick(hotelForm));
    }

    @Override
    public int getItemCount() {
        return hotelForms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotel;
        TextView tvHotelName, tvAddress, tvPrice, tvPriceDiscount;
        TextView tvReview, tvCheckIn, tvCheckOut, tvDistance;

        ViewHolder(View itemView) {
            super(itemView);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvCheckOut = itemView.findViewById(R.id.tvCheckOut);
            tvCheckIn = itemView.findViewById(R.id.tvCheckIn);
            tvReview = itemView.findViewById(R.id.tvReview);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            tvHotelName = itemView.findViewById(R.id.tvHotelName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPriceDiscount = itemView.findViewById(R.id.tvPriceDiscount);
        }
    }

    public interface HotelAdapterCallback {
        void onItemClick(HotelForm hotelForm);
    }
}
