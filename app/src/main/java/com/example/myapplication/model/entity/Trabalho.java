package com.example.myapplication.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Trabalho {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String dataHora;
    public double latitude;
    public double longitude;
} 