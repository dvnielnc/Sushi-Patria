package com.example.sushipatria;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {

    private DatabaseReference database;

    public FirebaseHelper() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void revisarUsuarioExistente(String username, FirebaseCallback callback) {
        database.child("Usuarios")
                .orderByChild("username")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            callback.onSuccess("Usuario Encontrado");
                        } else {
                            callback.onFailure(new Exception("Usuario No Encontrado"));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onFailure(new Exception(databaseError.getMessage()));
                    }
                });
    }

    public void agregarUsuario(String username, String password, FirebaseCallback callback) {
        String userId = database.child("Usuarios").push().getKey();
        if (userId != null) {
            User user = new User(username, password);
            database.child("Usuarios").child(userId)
                    .setValue(user)
                    .addOnSuccessListener(aVoid -> callback.onSuccess("Usuario Agregado Correctamente"))
                    .addOnFailureListener(callback::onFailure);
        } else {
            callback.onFailure(new Exception("Error Al Generar ID De Usuario"));
        }
    }

    public static class User {
        public String username;
        public String password;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public interface FirebaseCallback {
        void onSuccess(String message);
        void onFailure(Exception e);
    }

    public void agregarPedido(int sushipletoCant, int sushiburgerCant, int sushipizzaCant, boolean salsaSoya, boolean salsaTeriyaki, FirebaseCallback callback) {
        String pedidoId = database.child("Pedidos").push().getKey();
        if (pedidoId != null) {
            Map<String, String> detalles = new HashMap<>();
            detalles.put("Sushipleto", String.valueOf(sushipletoCant));
            detalles.put("Sushiburger", String.valueOf(sushiburgerCant));
            detalles.put("Sushipizza", String.valueOf(sushipizzaCant));
            detalles.put("Salsa Soya", salsaSoya ? "Sí" : "No");
            detalles.put("Salsa Teriyaki", salsaTeriyaki ? "Sí" : "No");

            Pedido pedido = new Pedido(pedidoId, detalles, 0.0);

            database.child("Pedidos").child(pedidoId)
                    .setValue(pedido)
                    .addOnSuccessListener(aVoid -> callback.onSuccess("Pedido Agregado Correctamente"))
                    .addOnFailureListener(callback::onFailure);
        } else {
            callback.onFailure(new Exception("Error Al Generar ID De Pedido"));
        }
    }

}