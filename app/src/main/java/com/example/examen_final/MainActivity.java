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

public class MainActivity extends AppCompatActivity {
    RecyclerView rvCuentas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvCuentas = findViewById(R.id.rvCuentas);
    }
    public  void crearCuenta(View view){
        Intent intent = new Intent(getApplicationContext(), CrearCuentaActivity.class);
        startActivity(intent);
    }
    public  void mostrarCuentas(View view){
        Intent intent = new Intent(getApplicationContext(), MostrarCuentasActivity.class);
        startActivity(intent);
    }

}