package com.example.myapplication.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Foto {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long idTrabalho;
    public String caminhoArquivo;
    public String dataHora;
} 