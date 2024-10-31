package com.example.sushipatria;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activityCrearUsuario extends AppCompatActivity {

    private EditText editTextUsuario;
    private EditText editTextClave;
    private Button btnAgregarUsuario;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_usuario);

        editTextUsuario = findViewById(R.id.editTextCrearUsuario);
        editTextClave = findViewById(R.id.editTextCrearClave);
        btnAgregarUsuario = findViewById(R.id.btnAgregarUsuario);

        databaseHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnAgregarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsuario.getText().toString();
                String password = editTextClave.getText().toString();

                if (databaseHelper.isUserExists(username)) {
                    Toast.makeText(activityCrearUsuario.this, "Usuario Existente", Toast.LENGTH_SHORT).show();
                } else {
                    databaseHelper.addUser(username, password);
                    Toast.makeText(activityCrearUsuario.this, "Usuario Agregado Correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
