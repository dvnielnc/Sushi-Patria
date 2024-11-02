package com.example.sushipatria;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class activityVerPedidos extends AppCompatActivity {

    private ListView listaPedidos;
    private ArrayList<String> arregloPedidos = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapterPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pedidos);

        Button btnVolverEditarPedidos = findViewById(R.id.btnVolverEditarPedidos);
        btnVolverEditarPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activityVerPedidos.this, activityRealizarPedido.class);
                startActivity(intent);
            }
        });

        try {
            SQLiteDatabase db = openOrCreateDatabase("SushiPatria", Context.MODE_PRIVATE, null);
            listaPedidos = findViewById(R.id.verPedidos);

            final Cursor c = db.rawQuery("SELECT * FROM productos", null);
            int id = c.getColumnIndex("id");
            int cantidadSushipleto = c.getColumnIndex("cantidadSushipleto");
            int cantidadSushiburger = c.getColumnIndex("cantidadSushiburger");
            int cantidadSushipizza = c.getColumnIndex("cantidadSushipizza");
            int salsaSoya = c.getColumnIndex("salsaSoya");
            int salsaTeriyaki = c.getColumnIndex("salsaTeriyaki");

            arregloPedidos.clear();
            arrayAdapterPedidos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arregloPedidos);
            listaPedidos.setAdapter(arrayAdapterPedidos);

            final ArrayList<Pedido> listaDatos = new ArrayList<>();

            if (c.moveToFirst()) {
                do {
                    Pedido pedido = new Pedido();
                    pedido.id = c.getString(id);
                    pedido.cantidadSushipleto = c.getString(cantidadSushipleto);
                    pedido.cantidadSushiburger = c.getString(cantidadSushiburger);
                    pedido.cantidadSushipizza = c.getString(cantidadSushipizza);
                    pedido.salsaSoya = c.getInt(salsaSoya) == 1 ? "Sí" : "No";
                    pedido.salsaTeriyaki = c.getInt(salsaTeriyaki) == 1 ? "Sí" : "No";
                    listaDatos.add(pedido);

                    arregloPedidos.add("ID: " + c.getString(id) + " | Sushipleto: " + c.getString(cantidadSushipleto) +
                            " | Sushiburger: " + c.getString(cantidadSushiburger) +
                            " | Sushipizza: " + c.getString(cantidadSushipizza) +
                            " | Soya: " + pedido.salsaSoya +
                            " | Teriyaki: " + pedido.salsaTeriyaki);

                } while (c.moveToNext());
                arrayAdapterPedidos.notifyDataSetChanged();
                listaPedidos.invalidateViews();
            }

            listaPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, android.view.View view, int position, long l) {
                    Pedido pedido = listaDatos.get(position);
                    Intent i = new Intent(getApplicationContext(), activityEditarPedido.class);
                    i.putExtra("id", pedido.id);
                    i.putExtra("cantidadSushipleto", pedido.cantidadSushipleto);
                    i.putExtra("cantidadSushiburger", pedido.cantidadSushiburger);
                    i.putExtra("cantidadSushipizza", pedido.cantidadSushipizza);
                    i.putExtra("salsaSoya", pedido.salsaSoya.equals("Sí") ? "1" : "0");
                    i.putExtra("salsaTeriyaki", pedido.salsaTeriyaki.equals("Sí") ? "1" : "0");
                    startActivity(i);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Ha Ocurrido Un Error, Inténtalo Nuevamente.", Toast.LENGTH_SHORT).show();
        }
    }
}
