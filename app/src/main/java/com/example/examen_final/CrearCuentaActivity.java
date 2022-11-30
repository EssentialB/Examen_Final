package com.example.examen_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.examen_final.entities.Cuenta;
import com.example.examen_final.services.CuentaServices;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearCuentaActivity extends AppCompatActivity {
    private EditText etNombreCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        etNombreCuenta = findViewById(R.id.etNombreCuenta);
    }
    public void guardarCuenta(View view){
        String nombreCuenta = etNombreCuenta.getText().toString();

        Cuenta cuenta = new Cuenta();
        cuenta.nombre =nombreCuenta;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://635a19a3ff3d7bddb9af0f31.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CuentaServices services = retrofit.create(CuentaServices.class);
        services.create(cuenta).enqueue(new Callback<Cuenta>() {
            @Override
            public void onResponse(Call<Cuenta> call, Response<Cuenta> response) {
                Log.i("MAIN_APP",String.valueOf(response.code()));
                Toast.makeText(getApplicationContext(),"SE CREÓ DE MANERA CORRECTA", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Cuenta> call, Throwable t) {
                Log.i("MAIN_APP","FALLO AL GUARDAR");
                Toast.makeText(getApplicationContext(),"NO SE CREÓ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}