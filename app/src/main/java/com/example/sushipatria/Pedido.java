package com.example.sushipatria;

import java.util.HashMap;
import java.util.Map;

public class Pedido {
    private String id;
    private Map<String, String> detalles;
    private double total;

    public Pedido() {
        detalles = new HashMap<>();
    }

    public Pedido(String id, Map<String, String> detalles, double total) {
        this.id = id;
        this.detalles = detalles;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getDetalles() {
        return detalles;
    }

    public void setDetalles(Map<String, String> detalles) {
        this.detalles = detalles;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getSushipleto() {
        return detalles.get("Sushipleto");
    }

    public String getSushiburger() {
        return detalles.get("Sushiburger");
    }

    public String getSushipizza() {
        return detalles.get("Sushipizza");
    }

    public String getSalsaSoya() {
        return detalles.get("Salsa Soya");
    }

    public String getSalsaTeriyaki() {
        return detalles.get("Salsa Teriyaki");
    }
}