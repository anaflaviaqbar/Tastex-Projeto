package com.example.anafl.projetofirebase.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anafl.projetofirebase.Entidades.Prato;
import com.example.anafl.projetofirebase.Entidades.Usuario;
import com.example.anafl.projetofirebase.Listas.ClickRecyclerViewInterfacePrato;
import com.example.anafl.projetofirebase.Listas.PratoAdapter;
import com.example.anafl.projetofirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PaginaVendedor extends AppCompatActivity implements ClickRecyclerViewInterfacePrato {


    private String idVendedor;

    private ImageView imgPerfilPagVendedor;
    private TextView txtNomeVendedor;
    private String nomeVendedor;
    private String uidComprador;
    private String nomeComprador;

    private Usuario comprador;

    private FloatingActionButton btnAdicionarFavoritos;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private RecyclerView mRecyclerView;
    private PratoAdapter pratoAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_vendedor);

        instanciarFirebase();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        idVendedor = bundle.getString("idVendedor");


        txtNomeVendedor = (TextView) findViewById(R.id.txtNomeVendedorPagVend);
        imgPerfilPagVendedor = (ImageView) findViewById(R.id.imgPerfilPagVendedor);

        btnAdicionarFavoritos = (FloatingActionButton) findViewById(R.id.btnAdicionarFavoritos);
        btnAdicionarFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarFavoritos();
            }
        });



        lerNomeComprador();

        lerNomeVendedor();

        lerPratosDoVendedor();



    }
    private void lerNomeVendedor(){

        Query queryV = databaseReference.child("users").orderByChild("id").equalTo(idVendedor);
        queryV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Usuario> listUsers = new ArrayList<Usuario>();
                for (DataSnapshot objSnapShot:dataSnapshot.getChildren()){
                    Usuario u = objSnapShot.getValue(Usuario.class);

                    listUsers.add(u);
                }
                nomeVendedor = listUsers.get(0).getNome();
                txtNomeVendedor.setText(nomeVendedor);
                Glide.with(imgPerfilPagVendedor.getContext()).load(listUsers.get(0).getImagemPerfil()).into(imgPerfilPagVendedor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void lerNomeComprador(){

        Query queryV = databaseReference.child("users").orderByChild("id").equalTo(uidComprador);
        queryV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Usuario> listUsers = new ArrayList<Usuario>();
                for (DataSnapshot objSnapShot:dataSnapshot.getChildren()){
                    Usuario u = objSnapShot.getValue(Usuario.class);

                    listUsers.add(u);
                }
                comprador = listUsers.get(0);
                nomeComprador = comprador.getNome();
                //txtNomeVendedor.setText(nomeVendedor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void adicionarFavoritos(){


        DatabaseReference compradorReferencia = databaseReference.child("users").child(uidComprador);

        compradorReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario compradorAux = dataSnapshot.getValue(Usuario.class);

                comprador = compradorAux;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(comprador.addFavorito(idVendedor)){
            compradorReferencia.child("listaIdFavoritos").setValue(comprador.getListaIdFavoritos());
            Toast.makeText(PaginaVendedor.this, "Favorito adicionado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(PaginaVendedor.this, "Favorito já existe", Toast.LENGTH_SHORT).show();
        }

    }


    private void instanciarFirebase() {
        uidComprador = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uidComprador = user.getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void lerPratosDoVendedor() {

        Query query;
        query = databaseReference.child("pratos").orderByChild("idVendedor").equalTo(idVendedor);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Prato> listPratos = new ArrayList<Prato>();
                for (DataSnapshot objSnapShot:dataSnapshot.getChildren()){
                    Prato p = objSnapShot.getValue(Prato.class);

                    listPratos.add(p);
                }
                instanciarRecyclerView(/*view,*/ listPratos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void instanciarRecyclerView(/*View view,*/ List<Prato> listPratos){

        //Aqui é instanciado o Recyclerview

        mRecyclerView = (RecyclerView) /*view.*/findViewById(R.id.rvPratosPaginaVendedor);
        mLayoutManager = new LinearLayoutManager(PaginaVendedor.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        pratoAdapter = new PratoAdapter(listPratos, this);
        mRecyclerView.setAdapter(pratoAdapter);


    }


    @Override
    public void onCustomClick(Object object) {
        Prato pratoAtual = (Prato) object;

        Intent comprarPrato = new Intent(PaginaVendedor.this, ComprarPratoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("nome", pratoAtual.getNome());
        bundle.putString("descricao", pratoAtual.getDescricao());
        bundle.putString("idVendedor", pratoAtual.getIdVendedor());
        bundle.putFloat("preco", pratoAtual.getPreco());
        bundle.putString("uidPrato", pratoAtual.getUidPrato());
        bundle.putInt("tipoPrato", pratoAtual.getTipoPrato());
        bundle.putString("nomeVendedor", nomeVendedor);
        bundle.putString("nomeComprador", nomeComprador);
        bundle.putString("imgPratoUrl", pratoAtual.getImgPratoUrl());
        comprarPrato.putExtras(bundle);

        startActivity(comprarPrato);
    }
}
