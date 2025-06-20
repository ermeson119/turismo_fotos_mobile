package com.example.myapplication;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Trabalho.class, Foto.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TrabalhoDao trabalhoDao();
    public abstract FotoDao fotoDao();
} 