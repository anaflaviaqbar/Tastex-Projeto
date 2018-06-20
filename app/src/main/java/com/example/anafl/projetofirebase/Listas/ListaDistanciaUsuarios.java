package com.example.anafl.projetofirebase.Listas;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.anafl.projetofirebase.Entidades.Prato;
import com.example.anafl.projetofirebase.Entidades.Usuario;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListaDistanciaUsuarios extends ArrayList<Usuario> {

    private Context context;

    public ListaDistanciaUsuarios(Context context) {
        this.context = context;
    }

    public ListaDistanciaUsuarios(Context context, List<Usuario> listaInicial) {
        super(listaInicial);

        this.context = context;
    }

    public void ordenarLista() {
//        Usuario[] listaDeUsuarios = (Usuario[]) toArray();
        Usuario[] listaDeUsuarios = new Usuario[size()];
        int tamanho = listaDeUsuarios.length;
        int i;
        Usuario temp;

        i = 0;
        for (Object o : toArray()) {
            listaDeUsuarios[i] = (Usuario) o;
            ++i;
        }

        LatLng localizacaoUsuarioEu = pegarMinhaLocalizacao();
        atribuirDistancias(localizacaoUsuarioEu, listaDeUsuarios);

        for (i = tamanho / 2 - 1; i >= 0; --i) {
            //converte a estrutura para um heap
            heapficar(listaDeUsuarios, tamanho, i);
        }

        //faz as trocas das ordenacoes
        for (i = tamanho - 1; i >= 0; --i) {
            temp = listaDeUsuarios[0];
            listaDeUsuarios[0] = listaDeUsuarios[i];
            listaDeUsuarios[i] = temp;

            heapficar(listaDeUsuarios, i, 0);
        }

        removeRange(0, tamanho);
        addAll(Arrays.asList(listaDeUsuarios));


    }

    private void heapficar(Usuario[] lista, int raiz, int i) {

        int maior = i;
        int esquerda = 2 * i + 1;
        int direita = 2 * i + 2;

        //esquerda eh maior que a raiz
        if (esquerda < raiz && lista[esquerda].getDistancia() > lista[maior].getDistancia()) { // sobreescrever comparator
            maior = esquerda;
        }

        //direita maior que a raiz
        if (direita < raiz && lista[direita].getDistancia() > lista[maior].getDistancia()) {
            maior = direita;
        }

        //maior eh diferente da raiz
        if (maior != i) {

            Usuario temp = lista[i];
            lista[i] = lista[maior];
            lista[maior] = temp;

            //faz a recursão
            heapficar(lista, raiz, maior);
        }
    }

    private void atribuirDistancias(@Nullable LatLng minhaLocalizacao, Usuario[] lista) {

        double minhaLatitude = 0.0, minhaLongitude = 0.0;
        double radianoTerra = 6371e3;
        double latitudeU1;
        double latitudeU2;
        double deltaLatitude;
        double deltaLongitude;
        double distancia;

        if (minhaLocalizacao != null) {
            minhaLatitude = minhaLocalizacao.latitude;
            minhaLongitude = minhaLocalizacao.longitude;
        }

        for (Usuario u : lista) {



            if(u.getLatitude() == 0 && u.getLongitude() == 0){
                u.setDistancia(0);
                continue;
            }
            latitudeU1 = grauRadiano(minhaLatitude);
            latitudeU2 = grauRadiano(u.getLatitude());
            deltaLatitude = grauRadiano(u.getLatitude() - minhaLatitude);
            deltaLongitude = grauRadiano(u.getLongitude() - minhaLongitude);

            distancia = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2) +
                    Math.cos(latitudeU1) * Math.cos(latitudeU2) *
                            Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);
            distancia = 2 * Math.atan2(Math.sqrt(distancia), Math.sqrt(1 - distancia));
            distancia = radianoTerra * distancia;

            u.setDistancia(distancia);
        }
    }

    private double grauRadiano(double angulo) {
        return (angulo / 180.0) * Math.PI;
    }

    private LatLng pegarMinhaLocalizacao() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location == null){
                return new LatLng(0,0);
            }
            return new LatLng(location.getLatitude(), location.getLongitude());
        }

//        ActivityCompat.requestPermissions(context., new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);

        return null;
    }
}

