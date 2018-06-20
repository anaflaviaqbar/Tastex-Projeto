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
import android.widget.Toast;

import com.example.anafl.projetofirebase.Activity.PaginaVendedor;
import com.example.anafl.projetofirebase.Entidades.Usuario;
import com.example.anafl.projetofirebase.Listas.ClickRecyclerViewInterfaceVendedor;
import com.example.anafl.projetofirebase.Listas.VendedorAdapter;
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

public class Favoritos extends Fragment implements ClickRecyclerViewInterfaceVendedor {

    private String uidComprador;

    private View view;
    private RecyclerView mRecyclerView;
    private VendedorAdapter vendedorAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference databaseReference;

    private List<String> listaIdFavoritos = new ArrayList<>();
    private Usuario comprador;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_favoritos, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvVendedoresFav);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        inicializarUsuario();
        lerComprador();




        return view;
    }

    private void inicializarUsuario() {
        uidComprador = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uidComprador = user.getUid();
        }
    }



    private void lerComprador(){

        DatabaseReference compradorReferencia = databaseReference.child("users").child(uidComprador);

        compradorReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuarioAux = dataSnapshot.getValue(Usuario.class);

                lerUsuariosFavoritos(usuarioAux);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void lerUsuariosFavoritos(Usuario usuarioAux) {
        final List<String> lista = usuarioAux.getListaIdFavoritos();

        Query query;
        query = databaseReference.child("users").orderByChild("id");

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Usuario> listFavoritos = new ArrayList<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Usuario usuario = snapshot.getValue(Usuario.class);

                    if(lista.contains(usuario.getId())){
                        listFavoritos.add(usuario);
                    }
                }
                Log.d("Favoritos", "Tamanho: "+listFavoritos.size());
                setAdapter(view, listFavoritos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void setAdapter(View view, List<Usuario> listUsuario){

        vendedorAdapter = new VendedorAdapter(listUsuario, this);
        mRecyclerView.setAdapter(vendedorAdapter);

    }


    @Override
    public void onCustomClick(Object object) {
        Usuario usuarioAtual = (Usuario) object;

        Intent paginaVendedor = new Intent(getContext(), PaginaVendedor.class);
        Bundle bundle = new Bundle();
        bundle.putString("idVendedor", usuarioAtual.getId());
        paginaVendedor.putExtras(bundle);
        startActivity(paginaVendedor);
    }
}
