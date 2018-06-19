package com.example.anafl.projetofirebase.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anafl.projetofirebase.Entidades.Pedido;
import com.example.anafl.projetofirebase.Entidades.Usuario;
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
import java.util.Calendar;
import java.util.List;

public class ComprarPratoActivity extends AppCompatActivity {

    private String idVendedor;
    private String nomePrato;
    private String descPrato;
    private float precoPrato;
    private String uidPrato;
    private String nomeComprador;
    private String nomeVendedor;
    private String imgPratoUrl;

    private TextView nomePratoComprarPrato;
    private TextView descPratoComprarPrato;
    private TextView precoPratoComprarPrato;
    private ImageView imgPratoComprarPrato;



    private Button comprarPrato;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_prato);


        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        idVendedor = bundle.getString("idVendedor");
        nomePrato = bundle.getString("nome");
        descPrato = bundle.getString("descricao");
        precoPrato = bundle.getFloat("preco");
        uidPrato = bundle.getString("uidPrato");
        nomeComprador = bundle.getString("nomeComprador");
        //nomeVendedor = bundle.getString("nomeVendedor");
        imgPratoUrl = bundle.getString("imgPratoUrl");

        nomePratoComprarPrato = (TextView) findViewById(R.id.txtNomePratoComprarPrato);
        descPratoComprarPrato = (TextView) findViewById(R.id.txtDescricaoPratoComprarPrato);
        precoPratoComprarPrato = (TextView) findViewById(R.id.txtPrecoPratoComprarPrato);
        imgPratoComprarPrato = (ImageView) findViewById(R.id.imgPratoComprarPrato);

        nomePratoComprarPrato.setText(nomePrato);
        descPratoComprarPrato.setText(descPrato);
        precoPratoComprarPrato.setText(precoPrato + " R$");

        Glide.with(imgPratoComprarPrato.getContext()).load(imgPratoUrl).into(imgPratoComprarPrato);

        inicializarFirebase();

        lerNomeVendedor(idVendedor);

        comprarPrato = (Button) findViewById(R.id.btnComprarPrato);
        comprarPrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewPedido(firebaseUser.getUid());
                finish();
            }
        });


    }

    private void writeNewPedido(String userID) {
        Pedido novoPedido = new Pedido();
        Calendar dataPedido = Calendar.getInstance();

        novoPedido.setIdVendedor(idVendedor);
        novoPedido.setIdComprador(userID);
        novoPedido.setNomePrato(nomePrato);
        novoPedido.setPrecoPrato(precoPrato);
        novoPedido.setDescricaoPrato(descPrato);
        novoPedido.setUidPrato(uidPrato);
        novoPedido.setDataPedido(dataPedido.getTime().toString());
        novoPedido.setStatus(0);
        novoPedido.setNomeComprador(nomeComprador);
        novoPedido.setNomeVendedor(nomeVendedor);
        novoPedido.setImgPratoUrl(imgPratoUrl);

        String idPedido = databaseReference.child("pedidos").push().getKey();
        novoPedido.setIdPedido(idPedido);

        databaseReference.child("pedidos").child(idPedido).setValue(novoPedido);
        Toast.makeText(ComprarPratoActivity.this, "Pedido realizado com sucesso", Toast.LENGTH_SHORT).show();
    }



    private void inicializarFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private void lerNomeVendedor(String idVendedor){

        Query queryV = databaseReference.child("users").orderByChild("id").equalTo(idVendedor);
        queryV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Usuario> listUsers = new ArrayList<>();
                //aqui mano
                for (DataSnapshot objSnapShot:dataSnapshot.getChildren()){
                    Usuario u = objSnapShot.getValue(Usuario.class);

                    listUsers.add(u);
                }
                nomeVendedor = listUsers.get(0).getNome();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
