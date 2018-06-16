package com.example.anafl.projetofirebase.Entidades;

import java.util.ArrayList;
import java.util.List;

public class NoUsuario {

    private Usuario usuario;
    private List<Usuario> listaFavoritos = new ArrayList<Usuario>();

    public NoUsuario(Usuario u){
        this.usuario = u;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public List<Usuario> getListaFavoritos() {
        return listaFavoritos;
    }

    public void setListaFavoritos(List<Usuario> listaFavoritos) {
        this.listaFavoritos = listaFavoritos;
    }
}
