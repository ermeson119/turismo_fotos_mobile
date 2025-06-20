package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TrabalhoDao {
    @Insert
    long inserir(Trabalho trabalho);

    @Query("SELECT * FROM Trabalho ORDER BY id DESC")
    List<Trabalho> listarTodos();

    @Query("SELECT * FROM Trabalho WHERE id = :id")
    Trabalho buscarPorId(long id);
} 