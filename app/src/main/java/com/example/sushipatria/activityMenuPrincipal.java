package com.example.sushipatria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activityMenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnDisponible = findViewById(R.id.btnDisponible);
        btnDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityMenuPrincipal.this, activityDisponible.class);
                startActivity(intent);
            }
        });

        Button btnRealizarPedido = findViewById(R.id.btnRealizarPedido);
        btnRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityMenuPrincipal.this, activityRealizarPedido.class);
                startActivity(intent);
            }
        });

        Button btnVerSucursales = findViewById(R.id.btnVerSucursales);
        btnVerSucursales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityMenuPrincipal.this,activityVerSucursales.class);
                startActivity(intent);
            }
        });
    }
}