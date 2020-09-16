package com.example.gohotel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gohotel.R;
import com.example.gohotel.model.SearchForm;

import java.util.List;

public class SearchedAdapter extends RecyclerView.Adapter<SearchedAdapter.ViewHolder> {
    private Context context;
    private List<SearchForm> searchForms;
    private OnclickListener onclickListener;

    public SearchedAdapter(Context context, List<SearchForm> searchForms, OnclickListener onclickListener) {
        this.context = context;
        this.searchForms = searchForms;
        this.onclickListener = onclickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.searched_adapter, parent, false);
        return new SearchedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvText.setText(searchForms.get(position).getKey());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclickListener.onClick(searchForms.get(position).getKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchForms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;

        public ViewHolder(View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
        }
    }

    public interface OnclickListener {
        void onClick(String key);
    }
}
