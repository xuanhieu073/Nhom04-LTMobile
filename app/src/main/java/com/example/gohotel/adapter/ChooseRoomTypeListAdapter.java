package com.example.gohotel.adapter;

import android.content.Context;
import android.content.res.Resources;
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

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ChooseRoomTypeListAdapter extends RecyclerView.Adapter<ChooseRoomTypeListAdapter.ViewHolder> {
    private Context context;
    private List<RoomTypeForm> roomTypeFormList;
    private int potitionChoose = -1;
    private CallbackRoomTypeList callbackRoomTypeList;
    private Resources resources;

    public ChooseRoomTypeListAdapter(Context context, List<RoomTypeForm> roomTypeFormList, CallbackRoomTypeList callbackRoomTypeList) {
        this.context = context;
        this.roomTypeFormList = roomTypeFormList;
        this.callbackRoomTypeList = callbackRoomTypeList;
        resources = context.getResources();
    }

    public void updatePotitionChoose(int i) {
        this.potitionChoose = i;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_choose_room_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position == potitionChoose) {
            holder.tvRoomName.setTextColor(resources.getColor(R.color.colorPrimary));
        } else {
            holder.tvRoomName.setTextColor(resources.getColor(R.color.colorBlack));
        }
        RoomTypeForm roomTypeForm = roomTypeFormList.get(position);
        holder.tvRoomName.setText(roomTypeForm.getName());
        holder.tvPrice.setText(String.format("%s VND", Utils.formatCurrency(roomTypeForm.getPricePerDay())));
        holder.tvSoPhong.setText(String.format("Số phòng : %d", roomTypeForm.getNumberOfRooms()));
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.loading_big)
                .circleCropTransform()
                .override(context.getResources().getDimensionPixelSize(R.dimen.roomtype_height), context.getResources().getDimensionPixelSize(R.dimen.roomtype_height))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(roomTypeForm.getImage()).apply(requestOptions).transition(withCrossFade()).into(holder.imgRoom);
        holder.itemView.setOnClickListener(v -> {
            updatePotitionChoose(position);
            callbackRoomTypeList.resultRoomType(roomTypeForm, position);
        });
    }

    @Override
    public int getItemCount() {
        return roomTypeFormList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRoom;
        TextView tvRoomName, tvPrice, tvSoPhong;

        public ViewHolder(View itemView) {
            super(itemView);
            imgRoom = itemView.findViewById(R.id.imgRoom);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSoPhong = itemView.findViewById(R.id.tvSoPhong);
        }
    }

    public interface CallbackRoomTypeList {
        void resultRoomType(RoomTypeForm roomTypeForm, int potition);
    }
}
