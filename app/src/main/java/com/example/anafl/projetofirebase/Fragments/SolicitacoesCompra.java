package com.example.anafl.projetofirebase.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anafl.projetofirebase.Activity.PaginaPedidoSolComp;
import com.example.anafl.projetofirebase.Entidades.Pedido;
import com.example.anafl.projetofirebase.Listas.ClickRecyclerViewInterfacePedido;
import com.example.anafl.projetofirebase.Listas.PedidoAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SolicitacoesCompra.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SolicitacoesCompra#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SolicitacoesCompra extends Fragment implements ClickRecyclerViewInterfacePedido {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View view;

    private DatabaseReference mDatabaseReference;

    private String uid;

    private RecyclerView mRecyclerView;
    private PedidoAdapter pedidoAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    public SolicitacoesCompra() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SolicitacoesCompra.
     */
    // TODO: Rename and change types and number of parameters
    public static SolicitacoesCompra newInstance(String param1, String param2) {
        SolicitacoesCompra fragment = new SolicitacoesCompra();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_solicitacoes_compra, container, false);

        instanciarFirebase();

        lerPedidos();



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

    private void lerPedidos(){

        Query query;
        query = mDatabaseReference.child("pedidos").orderByChild("idComprador").equalTo(uid);

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
                instanciarRecyclerView(view, listPedidos);
                //Toast.makeText(getContext(), getContext().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void instanciarRecyclerView(View view, List<Pedido> listPedidos){

        //Aqui é instanciado o Recyclerview

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvPedidosSolCompras);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        pedidoAdapter = new PedidoAdapter(listPedidos, this);
        mRecyclerView.setAdapter(pedidoAdapter);


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


        Pedido pedido = (Pedido) object;

        Bundle bundle = new Bundle();
        bundle.putString("idPedido", pedido.getIdPedido());
        bundle.putString("nomeVendedor", pedido.getNomeVendedor());
        bundle.putString("nomePrato", pedido.getNomePrato());
        bundle.putString("descPrato", pedido.getDescricaoPrato());
        bundle.putString("precoPrato", pedido.getPrecoPrato() + " R$");
        bundle.putString("dataPedido", pedido.getDataPedido());

        Intent paginaPedidoSolComp = new Intent(getContext(), PaginaPedidoSolComp.class);
        paginaPedidoSolComp.putExtras(bundle);

        startActivity(paginaPedidoSolComp);

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
