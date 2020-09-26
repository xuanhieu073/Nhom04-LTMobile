package com.example.gohotel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gohotel.R;
import com.example.gohotel.model.HotelForm;
import java.util.List;

<!--Create by: Khoa-->
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private List<HotelForm> hotelForms;

    public SearchAdapter(Context context, List<HotelForm> hotelForms) {
        this.context = context;
        this.hotelForms = hotelForms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.search_adapter, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HotelForm hotelForm = hotelForms.get(position);
        holder.tvHotelName.setText(hotelForm.getNameHotel());
        holder.tvAddress.setText(hotelForm.getAddress());
    }

    @Override
    public int getItemCount() {
        if (hotelForms == null) return 0;
        return hotelForms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHotelName, tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            tvHotelName = itemView.findViewById(R.id.tvHotelName);
            tvAddress = itemView.findViewById(R.id.tvAddress);

        }
    }
}
