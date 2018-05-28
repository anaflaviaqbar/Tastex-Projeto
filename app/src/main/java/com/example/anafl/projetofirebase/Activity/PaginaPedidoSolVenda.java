package com.example.anafl.projetofirebase.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anafl.projetofirebase.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaginaPedidoSolVenda extends AppCompatActivity {

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
        setContentView(R.layout.activity_pagina_pedido_sol_venda);

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

        btnNao = (Button) findViewById(R.id.btnNaoSolVenda);
        btnNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.child("pedidos").child(idPedido).child("status").setValue(2);
                Toast.makeText(PaginaPedidoSolVenda.this, "Pedido foi cancelado!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnSim = (Button) findViewById(R.id.btnSimSolVenda);
        btnSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.child("pedidos").child(idPedido).child("status").setValue(1);
                Toast.makeText(PaginaPedidoSolVenda.this, "Pedido foi confirmado!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

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
