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

import java.util.List;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {
    private Context context;
    private List<CityForm> cityForms;
    private OnItemClick onItemClick;
    private Resources resource;

    public ProvinceAdapter(Context context, List<CityForm> cityForms, OnItemClick onItemClick) {
        this.context = context;
        this.cityForms = cityForms;
        this.onItemClick = onItemClick;
        resource = context.getResources();
    }

    @NonNull
    @Override
    public ProvinceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.province_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProvinceAdapter.ViewHolder holder, int position) {
        // nếu được chọn thì màu xanh
        if (cityForms.get(position).isClicked()) {
            holder.bgContainer.setBackgroundColor(resource.getColor(R.color.colorPrimary));
            holder.tvProvinceName.setTextColor(resource.getColor(R.color.colorWhite));

        } else {
            holder.tvProvinceName.setTextColor(resource.getColor(R.color.colorBlack));
            holder.bgContainer.setBackgroundColor(resource.getColor(R.color.colorWhite));
        }


        holder.tvProvinceName.setText(cityForms.get(position).getName());
        // click vào dòng nào
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lấy kích cỡ của mảng
                int size = cityForms.size();
                // nếu mà i khác với vị trí được chọn thì bằng false
                for (int i = 0; i < size; i++) {
                    if (i != position)
                        cityForms.get(i).setClicked(false);
                }
                cityForms.get(position).setClicked(!cityForms.get(position).isClicked());
                onItemClick.onclick(cityForms.get(position));
            }
        });
    }

    public List<CityForm> getCityForms() {
        return cityForms;
    }

    @Override
    public int getItemCount() {
        return cityForms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProvinceName;
        LinearLayout bgContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            bgContainer = itemView.findViewById(R.id.bgContainer);
            tvProvinceName = itemView.findViewById(R.id.tvProvinceName);
        }
    }

    public interface OnItemClick {
        void onclick(CityForm cityForm);
    }
}
