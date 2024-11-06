package com.example.sushipatria;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class activityEditarPedido extends AppCompatActivity {

    private EditText cantidadSushipleto, cantidadSushiburger, cantidadSushipizza;
    private CheckBox checkBoxSoya, checkBoxTeriyaki;
    private Button btnGuardarCambios, btnEliminarPedido, btnVolver;
    private String pedidoId;
    private String nombrePedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);

        cantidadSushipleto = findViewById(R.id.editarCantidadSushipleto);
        cantidadSushiburger = findViewById(R.id.editarCantidadSushiburger);
        cantidadSushipizza = findViewById(R.id.editarCantidadSushipizza);
        checkBoxSoya = findViewById(R.id.editarSalsaSoya);
        checkBoxTeriyaki = findViewById(R.id.editarSalsaTeriyaki);
        btnGuardarCambios = findViewById(R.id.btnRealizarCambios);
        btnEliminarPedido = findViewById(R.id.btnEliminarPedido);
        btnVolver = findViewById(R.id.btnVolver);

        pedidoId = getIntent().getStringExtra("pedidoId");
        if (pedidoId == null) {
            Toast.makeText(this, "Error: No Se Pudo Cargar El Pedido.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDatosPedido();

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPedidoEnFirebase();
            }
        });

        btnEliminarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarPedidoDeFirebase();
            }
        });

        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarDatosPedido() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Pedidos").child(pedidoId).child("Detalles Del Pedido");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    nombrePedido = dataSnapshot.child("nombre").getValue(String.class);
                    Integer sushipleto = dataSnapshot.child("sushipleto").getValue(Integer.class);
                    Integer sushiburger = dataSnapshot.child("sushiburger").getValue(Integer.class);
                    Integer sushipizza = dataSnapshot.child("sushipizza").getValue(Integer.class);
                    String salsaSoya = dataSnapshot.child("salsaSoya").getValue(String.class);
                    String salsaTeriyaki = dataSnapshot.child("salsaTeriyaki").getValue(String.class);

                    cantidadSushipleto.setText(sushipleto != null ? String.valueOf(sushipleto) : "0");
                    cantidadSushiburger.setText(sushiburger != null ? String.valueOf(sushiburger) : "0");
                    cantidadSushipizza.setText(sushipizza != null ? String.valueOf(sushipizza) : "0");
                    checkBoxSoya.setChecked("Sí".equals(salsaSoya));
                    checkBoxTeriyaki.setChecked("Sí".equals(salsaTeriyaki));
                } else {
                    Toast.makeText(activityEditarPedido.this, "El Pedido No Existe", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activityEditarPedido.this, "Error Al Cargar Los Datos Del Pedido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarPedidoEnFirebase() {
        if (nombrePedido == null || nombrePedido.isEmpty()) {
            Toast.makeText(this, "Error: El Nombre Del Pedido No Está Disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        int sushipleto = Integer.parseInt(cantidadSushipleto.getText().toString().trim());
        int sushiburger = Integer.parseInt(cantidadSushiburger.getText().toString().trim());
        int sushipizza = Integer.parseInt(cantidadSushipizza.getText().toString().trim());
        boolean salsaSoya = checkBoxSoya.isChecked();
        boolean salsaTeriyaki = checkBoxTeriyaki.isChecked();
        int totalAPagar = (sushipleto * 8000) + (sushiburger * 7000) + (sushipizza * 10000);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Pedidos").child(pedidoId).child("Detalles Del Pedido");

        HashMap<String, Object> pedidoActualizado = new HashMap<>();
        pedidoActualizado.put("nombre", nombrePedido);
        pedidoActualizado.put("sushipleto", sushipleto);
        pedidoActualizado.put("sushiburger", sushiburger);
        pedidoActualizado.put("sushipizza", sushipizza);
        pedidoActualizado.put("salsaSoya", salsaSoya ? "Sí" : "No");
        pedidoActualizado.put("salsaTeriyaki", salsaTeriyaki ? "Sí" : "No");
        pedidoActualizado.put("totalAPagar", totalAPagar);

        databaseReference.updateChildren(pedidoActualizado).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(activityEditarPedido.this, "Pedido Actualizado Correctamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(activityEditarPedido.this, "Error Al Actualizar El Pedido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarPedidoDeFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Pedidos").child(pedidoId);

        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(activityEditarPedido.this, "Pedido Eliminado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(activityEditarPedido.this, "Error Al Eliminar El Pedido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
