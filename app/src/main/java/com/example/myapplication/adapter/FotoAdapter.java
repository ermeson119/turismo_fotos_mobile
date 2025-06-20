package com.example.myapplication.adapter;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.Foto;
import java.util.List;

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.ViewHolder> {
    private List<Foto> fotos;

    public FotoAdapter(List<Foto> fotos) {
        this.fotos = fotos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foto foto = fotos.get(position);
        holder.imgFoto.setImageBitmap(BitmapFactory.decodeFile(foto.caminhoArquivo));
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFoto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoto = itemView.findViewById(R.id.imgFoto);
        }
    }
} 