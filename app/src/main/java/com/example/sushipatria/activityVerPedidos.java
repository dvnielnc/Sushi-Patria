package com.example.sushipatria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class activityVerPedidos extends AppCompatActivity {

    private ListView verPedidos;
    private List<String> pedidosList;
    private ArrayAdapter<String> adapter;
    private HashMap<Integer, String> pedidosIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pedidos);

        verPedidos = findViewById(R.id.verPedidos);
        pedidosList = new ArrayList<>();
        pedidosIds = new HashMap<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pedidosList);
        verPedidos.setAdapter(adapter);

        cargarPedidos();

        verPedidos.setOnItemClickListener((parent, view, position, id) -> {
            String pedidoId = pedidosIds.get(position);
            if (pedidoId != null) {
                Intent intent = new Intent(activityVerPedidos.this, activityEditarPedido.class);
                intent.putExtra("pedidoId", pedidoId);
                startActivity(intent);
            } else {
                Toast.makeText(activityVerPedidos.this, "Error Al Seleccionar Pedido", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnVolverEditarPedidos = findViewById(R.id.btnVolverEditarPedidos);
        btnVolverEditarPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activityVerPedidos.this, activityRealizarPedido.class);
                startActivity(intent);
            }
        });
    }

    private void cargarPedidos() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Pedidos");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pedidosList.clear();
                pedidosIds.clear();

                int position = 0;
                for (DataSnapshot pedidoSnapshot : dataSnapshot.getChildren()) {
                    try {
                        DataSnapshot detallesSnapshot = pedidoSnapshot.child("Detalles Del Pedido");
                        if (detallesSnapshot.exists()) {
                            String pedidoId = detallesSnapshot.child("id").getValue(String.class);
                            String nombre = detallesSnapshot.child("nombre").getValue(String.class);
                            nombre = (nombre != null) ? nombre : "Sin nombre";

                            String salsaSoya = detallesSnapshot.child("salsaSoya").getValue(String.class);
                            salsaSoya = (salsaSoya != null) ? salsaSoya : "No";

                            String salsaTeriyaki = detallesSnapshot.child("salsaTeriyaki").getValue(String.class);
                            salsaTeriyaki = (salsaTeriyaki != null) ? salsaTeriyaki : "No";

                            int sushiburger = parseNumberSafe(detallesSnapshot.child("sushiburger").getValue());
                            int sushipizza = parseNumberSafe(detallesSnapshot.child("sushipizza").getValue());
                            int sushipleto = parseNumberSafe(detallesSnapshot.child("sushipleto").getValue());

                            int totalAPagar = parseNumberSafe(detallesSnapshot.child("totalAPagar").getValue());

                            StringBuilder productos = new StringBuilder();
                            if (sushiburger > 0) productos.append("Sushiburger (").append(sushiburger).append("), ");
                            if (sushipizza > 0) productos.append("Sushipizza (").append(sushipizza).append("), ");
                            if (sushipleto > 0) productos.append("Sushipleto (").append(sushipleto).append("), ");

                            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("es", "CL"));
                            String totalFormateado = numberFormat.format(totalAPagar);

                            String pedidoTexto = "Nombre: " + nombre + "\n" +
                                    "Productos: " + (productos.length() > 0 ? productos.substring(0, productos.length() - 2) : "Ninguno") + "\n" +
                                    "Salsa Soya: " + salsaSoya + "\n" +
                                    "Salsa Teriyaki: " + salsaTeriyaki + "\n" +
                                    "Total a Pagar: $" + totalFormateado;

                            pedidosList.add(pedidoTexto);
                            pedidosIds.put(position, pedidoId);
                            position++;
                        }
                    } catch (Exception e) {
                        Toast.makeText(activityVerPedidos.this, "Error Al Procesar Pedido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(activityVerPedidos.this, "Error Al Cargar Pedidos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int parseNumberSafe(Object value) {
        if (value instanceof Long) {
            return ((Long) value).intValue();
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
