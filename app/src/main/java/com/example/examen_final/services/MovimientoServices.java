package com.example.examen_final.services;

import com.example.examen_final.entities.Cuenta;
import com.example.examen_final.entities.Movimiento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MovimientoServices {
    @GET("Movimiento")
    Call<List<Movimiento>> getAll();

    @POST("Movimiento")
    Call<Movimiento> create(@Body Movimiento movimiento);
}
