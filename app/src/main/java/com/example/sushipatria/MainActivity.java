package com.example.sushipatria;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsuario;
    private EditText editTextClave;
    private Button btnIngresar;
    private Button btnCrearUsuario;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseHelper = new FirebaseHelper();

        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextClave = findViewById(R.id.editTextClave);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnCrearUsuario = findViewById(R.id.btnCrearUsuario);

        btnIngresar.setOnClickListener(v -> {
            String username = editTextUsuario.getText().toString().trim();
            String password = editTextClave.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por Favor, Ingrese Un Usuario", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por Favor, Ingrese Una Contraseña", Toast.LENGTH_SHORT).show();
            } else {
                firebaseHelper.revisarUsuarioExistente(username, new FirebaseHelper.FirebaseCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(MainActivity.this, "Bienvenido, " + username, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, activityMenuPrincipal.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MainActivity.this, "Error, Usuario No Encontrado", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnCrearUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activityCrearUsuario.class);
            startActivity(intent);
        });
    }
}
