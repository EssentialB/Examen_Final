package com.example.examen_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.examen_final.adapters.CuentaAdapter;
import com.example.examen_final.entities.Cuenta;
import com.example.examen_final.services.CuentaServices;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MostrarCuentasActivity extends AppCompatActivity {
    private RecyclerView rvCuentas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_cuentas);
        rvCuentas = findViewById(R.id.rvCuentas);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://635a19a3ff3d7bddb9af0f31.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //obtener los datos desde el getAll
        CuentaServices services = retrofit.create(CuentaServices.class);
        services.getAll().enqueue(new Callback<List<Cuenta>>() {
            @Override
            public void onResponse(Call<List<Cuenta>> call, Response<List<Cuenta>> response) {
                List<Cuenta> data = response.body();
                rvCuentas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvCuentas.setAdapter(new CuentaAdapter(data));
                Log.i("MAIN_APP","Response" + response.body().size());
                Log.i("MAIN_APP",new Gson().toJson(data));
            }
            @Override
            public void onFailure(Call<List<Cuenta>> call, Throwable t) {
                Log.i("MAIN_APP","FALLO AL OBTENER LA INFO");
            }
        });
    }


}