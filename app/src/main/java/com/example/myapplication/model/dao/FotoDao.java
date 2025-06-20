package com.example.myapplication.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.model.entity.Foto;

import java.util.List;

@Dao
public interface FotoDao {
    @Insert
    long inserir(Foto foto);

    @Query("SELECT * FROM Foto WHERE idTrabalho = :idTrabalho ORDER BY id DESC")
    List<Foto> listarPorTrabalho(long idTrabalho);

    @Query("SELECT * FROM Foto WHERE id = :id")
    Foto buscarPorId(long id);
} 