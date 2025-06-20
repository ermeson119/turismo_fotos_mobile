package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.model.entity.Trabalho;
import java.util.List;

public class TrabalhoAdapter extends RecyclerView.Adapter<TrabalhoAdapter.ViewHolder> {
    private List<Trabalho> trabalhos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Trabalho trabalho);
    }

    public TrabalhoAdapter(List<Trabalho> trabalhos, OnItemClickListener listener) {
        this.trabalhos = trabalhos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trabalho, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trabalho trabalho = trabalhos.get(position);
        holder.txtInfo.setText("ID: " + trabalho.id + "\nData: " + trabalho.dataHora + "\nLat: " + trabalho.latitude + ", Lon: " + trabalho.longitude);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(trabalho));
    }

    @Override
    public int getItemCount() {
        return trabalhos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInfo = itemView.findViewById(R.id.txtInfo);
        }
    }
} 