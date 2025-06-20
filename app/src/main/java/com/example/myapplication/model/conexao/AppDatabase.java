package com.example.myapplication.model.conexao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myapplication.model.dao.FotoDao;
import com.example.myapplication.model.dao.TrabalhoDao;
import com.example.myapplication.model.entity.Foto;
import com.example.myapplication.model.entity.Trabalho;

@Database(entities = {Trabalho.class, Foto.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TrabalhoDao trabalhoDao();
    public abstract FotoDao fotoDao();
} 