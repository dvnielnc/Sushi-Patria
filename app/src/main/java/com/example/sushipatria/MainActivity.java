package com.example.sushipatria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsuario;
    private EditText editTextClave;
    private Button btnIngresar;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextClave = findViewById(R.id.editTextClave);
        btnIngresar = findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsuario.getText().toString();
                String password = editTextClave.getText().toString();

                if (databaseHelper.checkUserCredentials(username, password)) {
                    Intent intent = new Intent(MainActivity.this, activityMenuPrincipal.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Ingreso Exitoso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Usuario O Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnCrearUsuario = findViewById(R.id.btnCrearUsuario);
        btnCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activityCrearUsuario.class);
                startActivity(intent);
            }
        });
    }
}
