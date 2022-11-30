package com.example.examen_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.examen_final.entities.Movimiento;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class DetalleMovimientoActivity extends AppCompatActivity {
    TextView tvMontoMov, tvMotivoMov, tvTipoMov, tvLatitudMov, tvLongitudMov;
    ImageView ivMovimien;
    Movimiento movimiento = new Movimiento();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_movimiento);

        tvMontoMov =findViewById(R.id.tvMontoMov);
        tvMotivoMov = findViewById(R.id.tvMotivoMov);
        tvTipoMov = findViewById(R.id.tvTipoMov);
        tvLatitudMov = findViewById(R.id.tvLatitudMov);
        tvLongitudMov = findViewById(R.id.tvLongitudMov);
        ivMovimien = findViewById(R.id.ivMovimien);

        Intent intent = getIntent();
        String MovimientoJson = intent.getStringExtra("MOVIMIENTO_DATA");

        if(MovimientoJson != null){
            movimiento = new Gson().fromJson(MovimientoJson,Movimiento.class);
            tvTipoMov.setText(movimiento.tipo);
            tvMontoMov.setText(movimiento.monto.toString());
            tvMotivoMov.setText(movimiento.motivo);
            Picasso.get().load(movimiento.imagen).into(ivMovimien);
            tvLatitudMov.setText("Latitud: " + movimiento.latitud );
            tvLongitudMov.setText("Longitud: " + movimiento.longitud);
        }
        if(MovimientoJson == null) return;
    }
    public void abrirMapa(View view){
        Intent intent = new Intent(this,MapsActivity.class);
        intent.putExtra("COORDENADAS_PELICULA", new Gson().toJson(movimiento));
        startActivity(intent);
    }
}