package com.example.myapplication.model.conexao;

import android.content.Context;
import androidx.room.Room;

public class DatabaseProvider {
    private static AppDatabase instance;

    public static AppDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "trabalhos_db").allowMainThreadQueries().build();
        }
        return instance;
    }
} 