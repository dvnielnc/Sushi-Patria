package com.example.sushipatria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class activityRealizarPedido extends AppCompatActivity {

    private EditText cantidadSushipleto, cantidadSushiburger, cantidadSushipizza, editTextNombre;
    private CheckBox checkBoxSoya, checkBoxTeriyaki;
    private Button btnAgregarPedido;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_pedido);

        editTextNombre = findViewById(R.id.nombrePedido);
        cantidadSushipleto = findViewById(R.id.cantidadSushipleto);
        cantidadSushiburger = findViewById(R.id.cantidadSushiburger);
        cantidadSushipizza = findViewById(R.id.cantidadSushipizza);
        checkBoxSoya = findViewById(R.id.checkBoxSoya);
        checkBoxTeriyaki = findViewById(R.id.checkBoxTeriyaki);
        btnAgregarPedido = findViewById(R.id.btnAgregarPedido);

        firebaseHelper = new FirebaseHelper();

        btnAgregarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextNombre.getText().toString().trim();
                if (nombre.isEmpty()) {
                    Toast.makeText(activityRealizarPedido.this, "Por Favor Ingrese Su Nombre", Toast.LENGTH_SHORT).show();
                    return;
                }

                String sushipletoText = cantidadSushipleto.getText().toString().trim();
                int sushipletoCant = sushipletoText.isEmpty() ? 0 : Integer.parseInt(sushipletoText);

                String sushiburgerText = cantidadSushiburger.getText().toString().trim();
                int sushiburgerCant = sushiburgerText.isEmpty() ? 0 : Integer.parseInt(sushiburgerText);

                String sushipizzaText = cantidadSushipizza.getText().toString().trim();
                int sushipizzaCant = sushipizzaText.isEmpty() ? 0 : Integer.parseInt(sushipizzaText);

                boolean salsaSoya = checkBoxSoya.isChecked();
                boolean salsaTeriyaki = checkBoxTeriyaki.isChecked();

                agregarPedidoAFirebase(nombre, sushipletoCant, sushiburgerCant, sushipizzaCant, salsaSoya, salsaTeriyaki);
            }
        });

        Button btnVerOEditarPedido = findViewById(R.id.btnVerOEditarPedido);
        btnVerOEditarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activityRealizarPedido.this, activityVerPedidos.class);
                startActivity(intent);
            }
        });

        Button btnVolverRealizarPedido = findViewById(R.id.btnVolverRealizarPedido);
        btnVolverRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activityRealizarPedido.this, activityMenuPrincipal.class);
                startActivity(intent);
            }
        });
    }

    private void agregarPedidoAFirebase(String nombre, int sushipletoCant, int sushiburgerCant, int sushipizzaCant, boolean salsaSoya, boolean salsaTeriyaki) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Pedidos");
        String pedidoId = databaseReference.push().getKey();

        int totalAPagar = (sushipletoCant * 8000) + (sushiburgerCant * 7000) + (sushipizzaCant * 10000);

        HashMap<String, Object> pedidoDetalles = new HashMap<>();
        pedidoDetalles.put("id", pedidoId);
        pedidoDetalles.put("nombre", nombre);
        pedidoDetalles.put("sushipleto", sushipletoCant);
        pedidoDetalles.put("sushiburger", sushiburgerCant);
        pedidoDetalles.put("sushipizza", sushipizzaCant);
        pedidoDetalles.put("salsaSoya", salsaSoya ? "Sí" : "No");
        pedidoDetalles.put("salsaTeriyaki", salsaTeriyaki ? "Sí" : "No");
        pedidoDetalles.put("totalAPagar", totalAPagar);

        HashMap<String, Object> pedido = new HashMap<>();
        pedido.put("Detalles Del Pedido", pedidoDetalles);

        databaseReference.child(pedidoId).setValue(pedido).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(activityRealizarPedido.this, "Pedido Agregado Correctamente", Toast.LENGTH_SHORT).show();
                limpiarCampos();
            } else {
                Toast.makeText(activityRealizarPedido.this, "Error Al Agregar Pedido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        editTextNombre.setText("");
        cantidadSushipleto.setText("");
        cantidadSushiburger.setText("");
        cantidadSushipizza.setText("");
        checkBoxSoya.setChecked(false);
        checkBoxTeriyaki.setChecked(false);
    }
}
