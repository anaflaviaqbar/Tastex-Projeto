package com.example.anafl.projetofirebase.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.anafl.projetofirebase.Entidades.NoUsuario;
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

public class Recomendacoes extends Fragment implements ClickRecyclerViewInterfaceVendedor {

    private String uidComprador;

    private DatabaseReference databaseReference;

    private  int altura;

    private View view;

    private NoUsuario noUsu;
    private List<NoUsuario> listaNoUsuario = new ArrayList<>();

    private List<String> listaIdFavUsuarioAtual = new ArrayList<>();


    private RecyclerView mRecyclerView;
    private VendedorAdapter vendedorAdapter;
    private RecyclerView.LayoutManager mLayoutManager;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_recomendacoes, container, false);

        altura = 0;

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvVendedoresRec);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        inicializarUsuarioAtual();

        inicializarGrafo(uidComprador); // carregar usuario atual no grafo


        return view;
    }

    private void inicializarGrafo(String id) {
        lerUsuario(id);
    }

    private void inicializarUsuarioAtual() {
        uidComprador = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uidComprador = user.getUid();
        }
    }

    private void lerUsuario(String id){

        DatabaseReference usuarioReference = databaseReference.child("users").child(id);

        usuarioReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuarioAux = dataSnapshot.getValue(Usuario.class);

                gravarEmGrafo(new NoUsuario(usuarioAux));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void gravarEmGrafo(NoUsuario noUsuAux) {

        //Declaração de variáveis
        this.noUsu = noUsuAux;

        //Fim Declaração de variáveis

        this.listaNoUsuario.add(noUsu);

        //String nome = listaNoUsuario.get(0).getUsuario().getNome();
        //Toast.makeText(getContext(), nome, Toast.LENGTH_LONG).show();

        lerFavoritos(noUsu.getUsuario());

        //Log.d("Recomendacoes", "Tamanho de listaFav depois de lerFavoritos: "+ listaFav.size());

    }

    private void lerFavoritos(Usuario usuario) {

        final List<String> listIdFav = usuario.getListaIdFavoritos();

        if(usuario.getId().equals(uidComprador)){
            listaIdFavUsuarioAtual = listIdFav;
        }


        DatabaseReference usuarioReference = databaseReference.child("users");

        usuarioReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Usuario> listaFav2 = new ArrayList<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Usuario u = snapshot.getValue(Usuario.class);

                    if(listIdFav.contains(u.getId())){
                        listaFav2.add(u);
                    }
                }
                Log.d("Recomendacoes", "Tamanho de listaFav2: "+ listaFav2.size());
                setListaFav(listaFav2);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setListaFav(List<Usuario> list){



        if(list.size() > 0){
            for(int i = 0; i<this.listaNoUsuario.size(); i++){
                if(this.listaNoUsuario.get(i).getUsuario().getId().equals(this.noUsu.getUsuario().getId())){
                    this.listaNoUsuario.get(i).setListaFavoritos(list);
                }
            }
        }


        /*
        Log.d("Recomendacoes", "=====================");
        Log.d("Recomendacoes", "Tamanho da lista de NoUsuario: " + this.listaNoUsuario.size());
        for(int f = 0; f < listaNoUsuario.size(); f++){
            Log.d("Recomendacoes", "Usuario: "+ listaNoUsuario.get(f).getUsuario().getNome());
            for(int g = 0; g < listaNoUsuario.get(f).getListaFavoritos().size(); g++){
                Log.d("Recomendacoes", "Favorito ("+g+"): " + listaNoUsuario.get(f).getListaFavoritos().get(g).getNome());
            }
        }
        Log.d("Recomendacoes", "Altura: "+altura);
        */
        int k = 0;
        if((list.size() > 0)&&(altura<2)){
            while(k < list.size()){
                //Log.d("Recomendacoes", "Gravar em grafo : " + list.get(k).getNome());
                gravarEmGrafo(new NoUsuario(list.get(k)));
                k++;
            }
            altura++;
        }

        List<Usuario> listaUsuario = new ArrayList<>();

        for(int i=1;i<listaNoUsuario.size();i++){
            if(!listaIdFavUsuarioAtual.contains(listaNoUsuario.get(i).getUsuario().getId())){
                listaUsuario.add(listaNoUsuario.get(i).getUsuario());
            }

        }
        setAdapter(view, listaUsuario);

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
