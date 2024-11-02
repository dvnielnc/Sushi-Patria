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

import androidx.appcompat.app.AppCompatActivity;

public class activityEditarPedido extends AppCompatActivity {

    private EditText cantSushipleto, cantSushiburger, cantSushipizza;
    private CheckBox elegirSalsaSoya, elegirSalsaTeriyaki;
    private Button btnRealizarCambios, btnEliminarPedido, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);

        cantSushipleto = findViewById(R.id.editarCantidadSushipleto);
        cantSushiburger = findViewById(R.id.editarCantidadSushiburger);
        cantSushipizza = findViewById(R.id.editarCantidadSushipizza);
        elegirSalsaSoya = findViewById(R.id.editarSalsaSoya);
        elegirSalsaTeriyaki = findViewById(R.id.editarSalsaTeriyaki);
        btnRealizarCambios = findViewById(R.id.btnRealizarCambios);
        btnEliminarPedido = findViewById(R.id.btnEliminarPedido);
        btnVolver = findViewById(R.id.btnVolver);

        Intent i = getIntent();
        String id = i.getStringExtra("id");
        String sushipleto = i.getStringExtra("cantidadSushipleto");
        String sushiburger = i.getStringExtra("cantidadSushiburger");
        String sushipizza = i.getStringExtra("cantidadSushipizza");
        String salsaSoya = i.getStringExtra("salsaSoya");
        String salsaTeriyaki = i.getStringExtra("salsaTeriyaki");

        Toast.makeText(this, "Espere..." + id, Toast.LENGTH_LONG).show();

        cantSushipleto.setText(sushipleto);
        cantSushiburger.setText(sushiburger);
        cantSushipizza.setText(sushipizza);
        elegirSalsaSoya.setChecked(salsaSoya.equals("1"));
        elegirSalsaTeriyaki.setChecked(salsaTeriyaki.equals("1"));

        btnRealizarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editar();
            }
        });

        btnEliminarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), activityVerPedidos.class);
                startActivity(i);
            }
        });
    }

    public void eliminar() {
        try {
            String sushipleto = cantSushipleto.getText().toString();
            String sushiburger = cantSushiburger.getText().toString();
            String sushipizza = cantSushipizza.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("SushiPatria", Context.MODE_PRIVATE, null);

            String sql = "DELETE FROM productos WHERE cantidadSushipleto = ? AND cantidadSushiburger = ? AND cantidadSushipizza = ?";
            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindString(1, sushipleto);
            statement.bindString(2, sushiburger);
            statement.bindString(3, sushipizza);

            statement.execute();

            Toast.makeText(this, "Pedido Eliminado Correctamente", Toast.LENGTH_LONG).show();

            cantSushipleto.setText("");
            cantSushiburger.setText("");
            cantSushipizza.setText("");
            elegirSalsaSoya.setChecked(false);
            elegirSalsaTeriyaki.setChecked(false);

        } catch (Exception ex) {
            Toast.makeText(this, "Error, No Se Pudo Eliminar El Pedido", Toast.LENGTH_LONG).show();
        }
    }

    public void editar() {
        try {
            String sushipleto = cantSushipleto.getText().toString();
            String sushiburger = cantSushiburger.getText().toString();
            String sushipizza = cantSushipizza.getText().toString();
            long salsaSoya = elegirSalsaSoya.isChecked() ? 1 : 0;
            long salsaTeriyaki = elegirSalsaTeriyaki.isChecked() ? 1 : 0;

            SQLiteDatabase db = openOrCreateDatabase("SushiPatria", Context.MODE_PRIVATE, null);

            String sql = "UPDATE productos SET cantidadSushipleto = ?, cantidadSushiburger = ?, cantidadSushipizza = ?, salsaSoya = ?, salsaTeriyaki = ? WHERE cantidadSushipleto = ? AND cantidadSushiburger = ? AND cantidadSushipizza = ?";
            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindString(1, sushipleto);
            statement.bindString(2, sushiburger);
            statement.bindString(3, sushipizza);
            statement.bindLong(4, salsaSoya);
            statement.bindLong(5, salsaTeriyaki);

            statement.bindString(6, sushipleto);
            statement.bindString(7, sushiburger);
            statement.bindString(8, sushipizza);

            statement.execute();

            Toast.makeText(this, "Pedido Actualizado Correctamente", Toast.LENGTH_LONG).show();

            cantSushipleto.setText("");
            cantSushiburger.setText("");
            cantSushipizza.setText("");
            elegirSalsaSoya.setChecked(false);
            elegirSalsaTeriyaki.setChecked(false);

        } catch (Exception ex) {
            Toast.makeText(this, "Error, No Se Pudo Actualizar El Pedido", Toast.LENGTH_LONG).show();
        }
    }
}