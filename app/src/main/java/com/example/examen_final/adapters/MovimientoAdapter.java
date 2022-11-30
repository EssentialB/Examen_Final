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
import com.example.examen_final.DetalleMovimientoActivity;
import com.example.examen_final.R;
import com.example.examen_final.entities.Cuenta;
import com.example.examen_final.entities.Movimiento;
import com.example.examen_final.services.MovimientoServices;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.List;

public class MovimientoAdapter extends RecyclerView.Adapter {
    List<Movimiento> datos;

    public MovimientoAdapter(List<Movimiento> data){
        this.datos = data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_movimiento,parent,false);
        return new MovimientoAdapter.MovimientoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movimiento movimiento = datos.get(position);

        TextView tvTipoMovimiento =holder.itemView.findViewById(R.id.tvTipo);
        tvTipoMovimiento.setText(datos.get(position).tipo);

        TextView tvMonto = holder.itemView.findViewById(R.id.tvMonto);
        tvMonto.setText(datos.get(position).monto.toString());

        TextView tvMotivo = holder.itemView.findViewById(R.id.tvMotivo);
        tvMotivo.setText(datos.get(position).motivo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), DetalleMovimientoActivity.class);
                intent.putExtra("MOVIMIENTO_DATA",new Gson().toJson(movimiento));
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datos.size();
    }
    class MovimientoViewHolder extends RecyclerView.ViewHolder {
        public MovimientoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
