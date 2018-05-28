package com.example.anafl.projetofirebase.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.anafl.projetofirebase.Activity.AdicionarPratoActivity;
import com.example.anafl.projetofirebase.Activity.EditarPrato;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Vender.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Vender extends Fragment implements ClickRecyclerViewInterfacePrato{

    private DatabaseReference mDatabaseReference;

    private String uid;

    private RecyclerView mRecyclerView;
    private PratoAdapter pratoAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton btnAdicionarPrato;

    //private List<Prato> listPratos = new ArrayList<Prato>();

    private OnFragmentInteractionListener mListener;

    private View view;

    public Vender() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_vender, container, false);


        instanciarFirebase();

        lerPratos();




        btnAdicionarPrato = (FloatingActionButton) view.findViewById(R.id.btnAdicionarPrato);
        btnAdicionarPrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AdicionarPratoActivity.class);
                startActivity(i);
            }
        });


        return view;
    }



    private void instanciarFirebase(){
        uid = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uid = user.getUid();
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void lerPratos(){

        Query query;
        query = mDatabaseReference.child("pratos").orderByChild("idVendedor").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Prato> listPratos = new ArrayList<Prato>();
                for (DataSnapshot objSnapShot:dataSnapshot.getChildren()){
                    Prato p = objSnapShot.getValue(Prato.class);

                    listPratos.add(p);
                }
                instanciarRecyclerView(view, listPratos);
                Toast.makeText(getContext(), "Leu os dados", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void instanciarRecyclerView(View view, List<Prato> listPratos){

        //Aqui é instanciado o Recyclerview

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvPratosFragVender);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        pratoAdapter = new PratoAdapter(listPratos, this);
        mRecyclerView.setAdapter(pratoAdapter);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCustomClick(Object object) {
        Prato pratoAtual = (Prato) object;

        Intent intent = new Intent(getContext(), EditarPrato.class);
        Bundle bundle = new Bundle();
        bundle.putString("nome", pratoAtual.getNome());
        bundle.putString("descricao", pratoAtual.getDescricao());
        bundle.putString("idVendedor", pratoAtual.getIdVendedor());
        bundle.putFloat("preco", pratoAtual.getPreco());
        bundle.putString("uidPrato", pratoAtual.getUidPrato());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
