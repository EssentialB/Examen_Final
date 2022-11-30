package com.example.examen_final;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examen_final.entities.Image;
import com.example.examen_final.entities.ImageResponse;
import com.example.examen_final.entities.Movimiento;
import com.example.examen_final.services.ImagenServices;
import com.example.examen_final.services.MovimientoServices;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrarMovimientoActivity extends AppCompatActivity {
    private final static int CAMERA_REQUEST = 1000;
    private final static int GPS_REQUEST = 1001;
    private ImageView ivMovimiento;
    private EditText etTipoMoviento, etMotivo, etMonto;
    private TextView tvLatitud, tvLongitud;
    private String encoded = "";
    String link = "";
    Double latitud = 0d, longitud = 0d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_movimiento);

        etTipoMoviento = findViewById(R.id.etTipoMovimiento);
        etMotivo = findViewById(R.id.etMotivo);
        etMonto = findViewById(R.id.etMonto);
        ivMovimiento = findViewById(R.id.ivResgitroMovimiento);

        tvLatitud = findViewById(R.id.tvLatitud);
        tvLongitud = findViewById(R.id.tvLongitud);

    }

    public void tomarFoto(View view){
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            abrirCamara();
        } else {
            requestPermissions(new String[] {Manifest.permission.CAMERA}, 100);
        }
    }
    private void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_REQUEST ); //requestCode, numeor cualquiera
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Condición para poder usar la cámara y que no deje de funcionar por el Activity Result
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) { //aseurarse de solo llamar a la camara ||
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivMovimiento.setImageBitmap(imageBitmap);

            //Codificar la imagen
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Log.i("MAIN_APP", encoded);
            obtenerLink();
        }
    }
    public void obtenerLink(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.imgur.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Image image = new Image();
        image.image = encoded;

        ImagenServices services = retrofit.create(ImagenServices.class);
        services.create(image).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                Log.i("MAIN_APP",String.valueOf(response.code()));
                ImageResponse data = response.body();
                link = data.data.link;
                Log.i("MAIN_APP", new Gson().toJson(data));
                Log.i("MAIN_APP", link);
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.i("MAIN_APP", "NO SE OBTUVO EL ENLACE");
            }
        });


    }
    public void crearMovimiento(View view){

        //AppDataBase appDataBase = AppDataBase.getInstance(this); //instanciar la base de datos

        String TipoMovimiento = etTipoMoviento.getText().toString();
        String Monto = etMonto.getText().toString();
        String MotivoMovimiento = etMotivo.getText().toString();

        Movimiento movimiento = new Movimiento();
        movimiento.tipo = TipoMovimiento;
        movimiento.monto = Monto;
        movimiento.motivo = MotivoMovimiento;
        movimiento.imagen = link;
        movimiento.latitud = latitud;
        movimiento.longitud = longitud;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://635a19a3ff3d7bddb9af0f31.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovimientoServices services = retrofit.create(MovimientoServices.class);
        services.create(movimiento).enqueue(new Callback<Movimiento>() {
            @Override
            public void onResponse(Call<Movimiento> call, Response<Movimiento> response) {
                Log.i("MAIN_APP",String.valueOf(response.code()));
                Toast.makeText(getApplicationContext(),"SE CREO DE MANERA CORRECTA", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),DetalleActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Movimiento> call, Throwable t) {
                Log.i("MAIN_APP","FALLO AL GUARDAR");
                Toast.makeText(getApplicationContext(),"NO SE CREÓ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == GPS_REQUEST){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
        }
    }
    public void localizacion(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},GPS_REQUEST);
        }else{
            locationStart();
        }
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
    }

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {

        RegistrarMovimientoActivity newRegistrarMovimiento;
        public RegistrarMovimientoActivity getMainActivity() {
            return newRegistrarMovimiento;
        }

        public void setMainActivity(RegistrarMovimientoActivity mainActivity) {
            this.newRegistrarMovimiento = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
            tvLatitud.setText("Latitud: " + latitud);
            tvLongitud.setText("Longitud: " + longitud);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            Toast.makeText(getApplicationContext(), "GPS Desactivado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            Toast.makeText(getApplicationContext(), "GPS Activado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

}