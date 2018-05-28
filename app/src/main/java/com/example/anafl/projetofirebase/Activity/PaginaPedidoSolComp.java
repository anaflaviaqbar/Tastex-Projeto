package com.example.anafl.projetofirebase.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anafl.projetofirebase.Entidades.Pedido;
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

public class PaginaPedidoSolComp extends AppCompatActivity {

    private String idPedido;
    private String nomeVendedor;
    private String nomePrato;
    private String descPrato;
    private String precoPrato;
    private String dataPedido;



    private DatabaseReference mDatabaseReference;

    private TextView txtNomeVendedorSolComp;
    private TextView txtNomePratoSolComp;
    private TextView txtDescPratoSolComp;
    private TextView txtPrecoPratoSolComp;
    private TextView txtDataPedidoSolComp;

    private Button btnNao;
    private Button btnSim;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_pedido_sol_comp);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        idPedido = bundle.getString("idPedido");
        nomeVendedor = bundle.getString("nomeVendedor");
        nomePrato = bundle.getString("nomePrato");
        descPrato = bundle.getString("descPrato");
        precoPrato = bundle.getString("precoPrato");
        dataPedido = bundle.getString("dataPedido");

        instanciarFirebase();

        txtNomeVendedorSolComp = (TextView) findViewById(R.id.txtNomeVendedorSolComp);
        txtNomePratoSolComp = (TextView) findViewById(R.id.txtNomePratoSolComp);
        txtDescPratoSolComp = (TextView) findViewById(R.id.txtDescPratoSolComp);
        txtPrecoPratoSolComp = (TextView) findViewById(R.id.txtPrecoPratoSolComp);
        txtDataPedidoSolComp = (TextView) findViewById(R.id.txtDataPedidoSolComp);

        //erPedido();
        carregarDadosPedido();

        btnNao = (Button) findViewById(R.id.btnNaoSolComp);
        btnNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSim = (Button) findViewById(R.id.btnSimSolComp);
        btnSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.child("pedidos").child(idPedido).removeValue();
                Toast.makeText(PaginaPedidoSolComp.this, "Pedido foi excluído!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
/*
    public void lerPedido(){
        Query query;
        query = mDatabaseReference.child("pedidos").orderByChild("idPedido").equalTo(idPedido);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Pedido> listPedidos = new ArrayList<Pedido>();
                for (DataSnapshot objSnapShot:dataSnapshot.getChildren()){
                    Pedido p = objSnapShot.getValue(Pedido.class);

                    if(p.getStatus() == 0){
                        listPedidos.add(p);
                    }
                }
                carregarDadosPedido(listPedidos.get(0));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    private void instanciarFirebase(){

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void carregarDadosPedido(){

        txtNomeVendedorSolComp.setText(nomeVendedor);
        txtNomePratoSolComp.setText(nomePrato);
        txtDescPratoSolComp.setText(descPrato);
        txtPrecoPratoSolComp.setText(precoPrato);
        txtDataPedidoSolComp.setText(dataPedido);

    }

}
