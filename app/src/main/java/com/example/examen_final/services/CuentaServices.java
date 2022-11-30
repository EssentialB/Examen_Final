package com.example.examen_final.services;

import com.example.examen_final.entities.Cuenta;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CuentaServices {
    @GET("Cuenta")
    Call<List<Cuenta>> getAll();

    @POST("Cuenta")
    Call<Cuenta> create(@Body Cuenta pelicula);
}
