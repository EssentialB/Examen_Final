package com.example.examen_final.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examen_final.DetalleActivity;
import com.example.examen_final.R;
import com.example.examen_final.entities.Cuenta;
import com.google.gson.Gson;

import java.util.List;

public class CuentaAdapter extends RecyclerView.Adapter {
    List<Cuenta> datos;

    public CuentaAdapter(List<Cuenta> data){
        this.datos = data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_cuenta,parent,false);
        return new CuentaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Cuenta cuenta = datos.get(position);

        TextView tvNombreCuenta = holder.itemView.findViewById(R.id.tvNombreCuenta);
        tvNombreCuenta.setText(datos.get(position).nombre);

        tvNombreCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), DetalleActivity.class);
                intent.putExtra("CUENTA_DATA",new Gson().toJson(cuenta));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }
    class CuentaViewHolder extends RecyclerView.ViewHolder {
        public CuentaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
