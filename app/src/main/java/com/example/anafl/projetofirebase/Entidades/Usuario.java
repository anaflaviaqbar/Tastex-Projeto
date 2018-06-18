package com.example.anafl.projetofirebase.Entidades;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anafl on 23/03/2018.
 */

public class Usuario {

    private String id;
    private String nome;
    private String senha;
    private String confSenha;
    private String endereco;
    private double latitude;
    private double longitude;
    private double distancia;
    private String dataNasc;
    private String sexo;
    private String email;
    private String descricao;
    private String imagemPerfil;
    private List<String> listaIdFavoritos = new ArrayList<>();

    public Usuario(){

    }

    public Usuario(String nome){
        this.nome = nome;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String, Object> hashMapUsuario= new HashMap<>();

        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("nome", getNome());
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("data nascimento", getDataNasc());
        hashMapUsuario.put("senha", getSenha());
        hashMapUsuario.put("endereco", getEndereco());
        hashMapUsuario.put("sexo", getSexo());
        hashMapUsuario.put("descricao", getDescricao());
        hashMapUsuario.put("imagemPerfil", getImagemPerfil());

        return hashMapUsuario;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfSenha() {
        return confSenha;
    }

    public void setConfSenha(String confSenha) {
        this.confSenha = confSenha;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String cep) {
        this.endereco = cep;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(String imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }

    public List<String> getListaIdFavoritos() {
        return listaIdFavoritos;
    }

    public void setListaIdFavoritos(List<String> listaIdFavoritos) {
        this.listaIdFavoritos = listaIdFavoritos;
    }

    public boolean addFavorito(String id){

        if(!listaIdFavoritos.contains(id)){
            this.listaIdFavoritos.add(id);
            return true;
        }else{
            return false;
        }

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitudeLongitude(Context context) {

        Geocoder geocoder = new Geocoder(context);
        List<Address> listaEnderecos = null;

        try {
            listaEnderecos = geocoder.getFromLocationName(endereco, 10);
        }
        catch (IOException ex){
            Toast.makeText(context, "Altere seu endereço", Toast.LENGTH_LONG).show();
            latitude = longitude = 0.0;
            endereco = "";
            return;
        }

        if(listaEnderecos == null){
            Toast.makeText(context, "Endereço não encontrado", Toast.LENGTH_LONG).show();
            return;
        }

        Address endereco = listaEnderecos.get(0);
        latitude = endereco.getLatitude();
        longitude = endereco.getLongitude();
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
}
