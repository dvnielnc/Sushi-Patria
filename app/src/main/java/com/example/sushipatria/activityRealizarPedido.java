package com.example.sushipatria;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activityRealizarPedido extends AppCompatActivity {

    private EditText cantSushipleto, cantSushiburger, cantSushipizza;
    private CheckBox elegirSalsaSoya, elegirSalsaTeriyaki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_realizar_pedido);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cantSushipleto = findViewById(R.id.cantidadSushipleto);
        cantSushiburger = findViewById(R.id.cantidadSushiburger);
        cantSushipizza = findViewById(R.id.cantidadSushipizza);
        elegirSalsaSoya = findViewById(R.id.checkBoxSoya);
        elegirSalsaTeriyaki = findViewById(R.id.checkBoxTeriyaki);

        Button btnVolverRealizarPedido = findViewById(R.id.btnVolverRealizarPedido);
        btnVolverRealizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityRealizarPedido.this, activityMenuPrincipal.class);
                startActivity(intent);
            }
        });

        Button btnVerOEditarPedido = findViewById(R.id.btnVerOEditarPedido);
        btnVerOEditarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityRealizarPedido.this, activityVerPedidos.class);
                startActivity(intent);
            }
        });

        Button btnAgregarPedido = findViewById(R.id.btnAgregarPedido);
        btnAgregarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarPedido();
            }
        });
    }

    public void insertarPedido() {
        try {
            String sushipleto = cantSushipleto.getText().toString();
            String sushiburger = cantSushiburger.getText().toString();
            String sushipizza = cantSushipizza.getText().toString();
            long salsaSoya = elegirSalsaSoya.isChecked() ? 1 : 0;
            long salsaTeriyaki = elegirSalsaTeriyaki.isChecked() ? 1 : 0;

            SQLiteDatabase db = openOrCreateDatabase("SushiPatria", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS productos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "cantidadSushipleto INTEGER, " +
                    "cantidadSushiburger INTEGER, " +
                    "cantidadSushipizza INTEGER, " +
                    "salsaSoya INTEGER, " +
                    "salsaTeriyaki INTEGER)");

            String sql = "INSERT INTO productos (cantidadSushipleto, cantidadSushiburger, cantidadSushipizza, salsaSoya, salsaTeriyaki) VALUES (?, ?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindString(1, sushipleto);
            statement.bindString(2, sushiburger);
            statement.bindString(3, sushipizza);
            statement.bindLong(4, salsaSoya);
            statement.bindLong(5, salsaTeriyaki);

            statement.execute();

            Toast.makeText(this, "Pedido Agregado Correctamente", Toast.LENGTH_LONG).show();

            cantSushipleto.setText("");
            cantSushiburger.setText("");
            cantSushipizza.setText("");
            elegirSalsaSoya.setChecked(false);
            elegirSalsaTeriyaki.setChecked(false);
        } catch (Exception ex) {
            Toast.makeText(this, "Error, Pedido No Agregado", Toast.LENGTH_LONG).show();
        }
    }
}