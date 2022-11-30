package com.example.examen_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.examen_final.adapters.CuentaAdapter;
import com.example.examen_final.adapters.MovimientoAdapter;
import com.example.examen_final.entities.Cuenta;
import com.example.examen_final.entities.Movimiento;
import com.example.examen_final.services.CuentaServices;
import com.example.examen_final.services.MovimientoServices;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MostrarMovimientoActivity extends AppCompatActivity {
    private RecyclerView rvMovimientos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_movimiento);

        rvMovimientos = findViewById(R.id.rvMovimientos);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://635a19a3ff3d7bddb9af0f31.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //obtener los datos desde el getAll
        MovimientoServices services = retrofit.create(MovimientoServices.class);
        services.getAll().enqueue(new Callback<List<Movimiento>>() {
            @Override
            public void onResponse(Call<List<Movimiento>> call, Response<List<Movimiento>> response) {
                List<Movimiento> data = response.body();
                rvMovimientos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvMovimientos.setAdapter(new MovimientoAdapter(data));
                Log.i("MAIN_APP","Response" + response.body().size());
                Log.i("MAIN_APP",new Gson().toJson(data));
            }

            @Override
            public void onFailure(Call<List<Movimiento>> call, Throwable t) {
                Log.i("MAIN_APP","FALLO AL OBTENER LA INFO");
            }
        });
    }
}