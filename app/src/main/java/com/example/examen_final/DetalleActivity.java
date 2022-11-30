package com.example.examen_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.examen_final.entities.Cuenta;
import com.google.gson.Gson;

public class DetalleActivity extends AppCompatActivity {
    TextView tvNombreCuenta;
    Cuenta cuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        tvNombreCuenta =findViewById(R.id.idNombreCuenta);

        Intent intent = getIntent();
        String cuentaJson = intent.getStringExtra("CUENTA_DATA");

        if(cuentaJson!=null){
            cuenta = new Gson().fromJson(cuentaJson, Cuenta.class);
            tvNombreCuenta.setText(cuenta.nombre);
        }
        if(cuentaJson == null) return;

    }

    public void registroMovimiento(View view){
        Intent intent = new Intent(getApplicationContext(), RegistrarMovimientoActivity.class);
        startActivity(intent);
    }

    public void verMovimientos(View view){
        Intent intent = new Intent(getApplicationContext(), MostrarMovimientoActivity.class);
        startActivity(intent);
    }
    public void sincronizar(){

    }
}
