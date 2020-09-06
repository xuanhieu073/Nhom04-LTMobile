package com.example.gohotel.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.gohotel.R;
import com.example.gohotel.activity.HotelDetailActivity;
import com.example.gohotel.model.HotelForm;
import com.example.gohotel.utils.Utils;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class HotelMapAdapter extends RecyclerView.Adapter<HotelMapAdapter.ViewHolder> {
    private List<HotelForm> hotelFormList;
    private Context context;
    private int widthImg, heightImg;
    private int widthSreen;

    public HotelMapAdapter(Context context, List<HotelForm> hotelFormList, int width) {
        this.context = context;
        this.hotelFormList = hotelFormList;
        this.widthSreen = width;
        this.widthImg = (int) (width - Utils.convertDpToPixel(40, context));
        this.heightImg = (widthImg * 200) / 400;
    }

    @NonNull
    @Override
    public HotelMapAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hotel_map_adapter, parent, false);
        return new ViewHolder(view);
    }

    public List<HotelForm> getHotelFormList() {
        return hotelFormList;
    }

    public void updateData(List<HotelForm> hotelFormList) {
        this.hotelFormList = hotelFormList;
    }

    @Override
    public void onBindViewHolder(@NonNull HotelMapAdapter.ViewHolder holder, int position) {
        HotelForm hotelForm = hotelFormList.get(position);
        ViewGroup.LayoutParams lp = holder.layoutImg.getLayoutParams();
        lp.height = heightImg;
        lp.width = widthImg;
        holder.layoutImg.setLayoutParams(lp);
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.loading_big)
                .error(R.drawable.loading_big);
        Glide.with(context)
                .load(hotelFormList.get(position).getNameImage())
                .apply(requestOptions)
                .transition(withCrossFade())
                .into(holder.imgItem);
        holder.tvHotelName.setText(hotelForm.getNameHotel());
        holder.tvReview.setText(String.valueOf(hotelForm.getCountStar()));
        holder.tvPrice.setText(String.format("%s VND", Utils.formatCurrency(hotelForm.getPriceRoomPerDay())));
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, HotelDetailActivity.class);
            intent.putExtra("hotelKey", hotelForm.getIdHotel());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (hotelFormList == null) return 0;
        return hotelFormList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layoutImg;
        ImageView imgItem;
        TextView tvHotelName, tvPrice, tvReview;

        public ViewHolder(View itemView) {
            super(itemView);
            tvReview = itemView.findViewById(R.id.tvReview);
            layoutImg = itemView.findViewById(R.id.layoutImg);
            imgItem = itemView.findViewById(R.id.imgItem);
            tvHotelName = itemView.findViewById(R.id.tvHotelName);
            tvPrice = itemView.findViewById(R.id.tvPrice);

        }
    }
}
