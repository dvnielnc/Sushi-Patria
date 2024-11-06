package com.example.sushipatria;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class activityCrearUsuario extends AppCompatActivity {

    private EditText editTextCrearUsuario;
    private EditText editTextCrearClave;
    private Button btnAgregarUsuario;
    private Button btnVolverCrearUsuario;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        firebaseHelper = new FirebaseHelper();

        editTextCrearUsuario = findViewById(R.id.editTextCrearUsuario);
        editTextCrearClave = findViewById(R.id.editTextCrearClave);
        btnAgregarUsuario = findViewById(R.id.btnAgregarUsuario);
        btnVolverCrearUsuario = findViewById(R.id.btnVolverCrearUsuario);

        btnVolverCrearUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(activityCrearUsuario.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnAgregarUsuario.setOnClickListener(v -> {
            String username = editTextCrearUsuario.getText().toString().trim();
            String password = editTextCrearClave.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(activityCrearUsuario.this, "Por Favor, Ingrese Un Usuario Y Una Contraseña", Toast.LENGTH_SHORT).show();
            } else {
                firebaseHelper.revisarUsuarioExistente(username, new FirebaseHelper.FirebaseCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(activityCrearUsuario.this, "Usuario Existente", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        firebaseHelper.agregarUsuario(username, password, new FirebaseHelper.FirebaseCallback() {
                            @Override
                            public void onSuccess(String message) {
                                Toast.makeText(activityCrearUsuario.this, "Usuario Agregado Correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activityCrearUsuario.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(activityCrearUsuario.this, "Error Al Agregar El Usuario", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }
}
