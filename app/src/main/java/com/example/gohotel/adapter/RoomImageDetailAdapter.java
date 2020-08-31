package com.example.gohotel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.gohotel.R;
import com.example.gohotel.model.api.HotelImageForm;

import java.util.List;

public class RoomImageDetailAdapter extends RecyclerView.Adapter<RoomImageDetailAdapter.ViewHolder> {

    private Context context;
    private List<HotelImageForm> hotelImageForms;

    public RoomImageDetailAdapter(Context context, List<HotelImageForm> hotelImageForms) {
        this.context = context;
        this.hotelImageForms = hotelImageForms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hotel_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        /*
        / Set Image Normal
        */

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.loading_big)
                .error(R.drawable.loading_big).skipMemoryCache(true);

        Glide.with(context)
                .load(hotelImageForms.get(position).getNameImage())
                .apply(requestOptions)
                .into(holder.imgViewNormal);

    }

    @Override
    public int getItemCount() {
        return hotelImageForms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgViewNormal;

        public ViewHolder(View itemView) {
            super(itemView);
            imgViewNormal = itemView.findViewById(R.id.imgItem);
        }
    }
}
