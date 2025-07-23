package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapter.TrabalhoAdapter;
import com.example.myapplication.model.conexao.DatabaseProvider;
import com.example.myapplication.model.entity.Trabalho;

import java.util.List;

public class LocalizacaoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewLocalizacoes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Trabalho> trabalhos = DatabaseProvider.getDatabase(this).trabalhoDao().listarTodos();
        TrabalhoAdapter adapter = new TrabalhoAdapter(trabalhos, trabalho -> {
            Intent intent = new Intent(LocalizacaoActivity.this, FotosActivity.class);
            intent.putExtra("idTrabalho", trabalho.id);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
} 