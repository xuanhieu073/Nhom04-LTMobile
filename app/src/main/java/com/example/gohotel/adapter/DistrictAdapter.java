package com.example.gohotel.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gohotel.R;
import com.example.gohotel.model.api.CityForm;
import com.example.gohotel.model.api.DistrictForm;

import java.util.List;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.ViewHolder> {
    private Context context;
    private List<DistrictForm> cityForms;
    private OnItemClick onItemClick;
    private Resources resource;

    public DistrictAdapter(Context context, List<DistrictForm> cityForms, OnItemClick onItemClick) {
        this.context = context;
        this.cityForms = cityForms;
        this.onItemClick = onItemClick;
        resource = context.getResources();

    }

    @NonNull
    @Override
    public DistrictAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.province_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictAdapter.ViewHolder holder, int position) {
        if (cityForms.get(position).isClicked()) {
            holder.bgContainer.setBackgroundColor(resource.getColor(R.color.colorPrimary));
            holder.tvProvinceName.setTextColor(resource.getColor(R.color.colorWhite));

        } else {
            holder.tvProvinceName.setTextColor(resource.getColor(R.color.colorBlack));
            holder.bgContainer.setBackgroundColor(resource.getColor(R.color.colorWhite));
        }
        holder.tvProvinceName.setText(cityForms.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int size = cityForms.size();
                for (int i = 0; i < size; i++) {
                    if (i != position)
                        cityForms.get(i).setClicked(false);
                }
                cityForms.get(position).setClicked(!cityForms.get(position).isClicked());
                onItemClick.onClick(cityForms.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityForms.size();
    }

    public List<DistrictForm> getDistrictForm() {
        return cityForms;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProvinceName;
        LinearLayout bgContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProvinceName = itemView.findViewById(R.id.tvProvinceName);
            bgContainer = itemView.findViewById(R.id.bgContainer);

        }
    }

    public interface OnItemClick {
        void onClick(DistrictForm districtForm);
    }
}
