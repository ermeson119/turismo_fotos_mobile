package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapter.FotoAdapter;
import com.example.myapplication.model.conexao.DatabaseProvider;
import com.example.myapplication.model.entity.Foto;

import java.util.List;

public class PhotosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        long idTrabalho = getIntent().getLongExtra("idTrabalho", -1);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewFotos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Foto> fotos = DatabaseProvider.getDatabase(this).fotoDao().listarPorTrabalho(idTrabalho);
        FotoAdapter adapter = new FotoAdapter(fotos);
        recyclerView.setAdapter(adapter);
    }
} 