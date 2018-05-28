package com.example.anafl.projetofirebase.Entidades;

import java.util.ArrayList;

public class Pedido {

    private String idVendedor;
    private String idComprador;
    private ArrayList<Prato> listaPratos;
    private float total;

    public Pedido(){

    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(String idComprador) {
        this.idComprador = idComprador;
    }
}
