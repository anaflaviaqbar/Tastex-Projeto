package com.example.anafl.projetofirebase.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.anafl.projetofirebase.Activity.ComprarPratoActivity;
import com.example.anafl.projetofirebase.Activity.PaginaVendedor;
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

public class ComprarPrato extends Fragment implements ClickRecyclerViewInterfacePrato{


    private DatabaseReference databaseReference;

    private String uidComprador;

    private RecyclerView mRecyclerView;
    private PratoAdapter pratoAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Spinner spinTipoPratoCompPrato;
    private int tipoPrato;

    private String nomeComprador;
    private String nomeVendedor;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comprar_prato, container, false);

        uidComprador = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uidComprador = user.getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvPratosPesquisar);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Configuração Spinner
        spinTipoPratoCompPrato = (Spinner) view.findViewById(R.id.spinTipoPratoCompPrato);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.tipos_pratos_pesquisar, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTipoPratoCompPrato.setAdapter(adapter);
        spinTipoPratoCompPrato.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String stgTipoPrato = parent.getItemAtPosition(position).toString();
                switch (stgTipoPrato){
                    case "Normal":
                        tipoPrato = 1;
                        lerPratosFiltro();
                        break;
                    case "Low Carb":
                        tipoPrato = 2;
                        lerPratosFiltro();
                        break;
                    case "Vegetariano":
                        tipoPrato = 3;
                        lerPratosFiltro();
                        break;
                    case "Vegano":
                        tipoPrato = 4;
                        lerPratosFiltro();
                        break;
                    case "Todos":
                        tipoPrato = 4;
                        lerPratos();
                        break;
                    default:
                        tipoPrato = 4;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lerNomeComprador();

        lerPratos();


        return view;
    }


    private void lerPratos() {

        Query query;
        query = databaseReference.child("pratos");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Prato> listPratos = new ArrayList<Prato>();
                for (DataSnapshot objSnapShot : dataSnapshot.getChildren()){
                    Prato p = objSnapShot.getValue(Prato.class);

                    listPratos.add(p);
                }
                setAdapterPratos(listPratos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void lerPratosFiltro(){
        Query query;
        query = databaseReference.child("pratos");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Prato> listPratos = new ArrayList<Prato>();
                for (DataSnapshot objSnapShot : dataSnapshot.getChildren()){
                    Prato p = objSnapShot.getValue(Prato.class);

                    if(p.getTipoPrato()== tipoPrato){
                        listPratos.add(p);
                    }

                }
                setAdapterPratos(listPratos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setAdapterPratos(List<Prato> listPratos) {

        pratoAdapter = new PratoAdapter(listPratos, this);
        mRecyclerView.setAdapter(pratoAdapter);
    }


    @Override
    public void onCustomClick(Object object) {


        Prato pratoAtual = (Prato) object;

        //lerNomeVendedor(pratoAtual.getIdVendedor());

        Intent comprarPrato = new Intent(getContext(), ComprarPratoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("nome", pratoAtual.getNome());
        bundle.putString("descricao", pratoAtual.getDescricao());
        bundle.putString("idVendedor", pratoAtual.getIdVendedor());
        bundle.putFloat("preco", pratoAtual.getPreco());
        bundle.putString("uidPrato", pratoAtual.getUidPrato());
        bundle.putInt("tipoPrato", pratoAtual.getTipoPrato());
        //bundle.putString("nomeVendedor", nomeVendedor);
        bundle.putString("nomeComprador", nomeComprador);
        bundle.putString("imgPratoUrl", pratoAtual.getImgPratoUrl());
        comprarPrato.putExtras(bundle);

        startActivity(comprarPrato);

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
                nomeComprador = listUsers.get(0).getNome();
                //txtNomeVendedor.setText(nomeVendedor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
