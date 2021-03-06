package com.example.anafl.projetofirebase.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anafl.projetofirebase.Activity.PaginaVendedor;
import com.example.anafl.projetofirebase.Entidades.Usuario;
import com.example.anafl.projetofirebase.Listas.ClickRecyclerViewInterfaceVendedor;
import com.example.anafl.projetofirebase.Listas.ListaDistanciaUsuarios;
import com.example.anafl.projetofirebase.Listas.VendedorAdapter;
import com.example.anafl.projetofirebase.R;
import com.google.firebase.auth.FirebaseAuth;
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
 * {@link Comprar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Comprar extends Fragment implements ClickRecyclerViewInterfaceVendedor {

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private VendedorAdapter vendedorAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText edtPesquisar;
    private Button btnPesquisar;

    private View view;

    private String pesquisa;

//    private List<Usuario> listVendedores = new ArrayList<>();

    private DatabaseReference databaseReference;


    public Comprar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comprar, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_vendedores);
        edtPesquisar = (EditText) view.findViewById(R.id.edtPesquisar);
        btnPesquisar = view.findViewById(R.id.btnPesquisar);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pesquisa = edtPesquisar.getText().toString();
                Toast.makeText(getContext(), "Pesquisar: "+pesquisa, Toast.LENGTH_SHORT).show();
                pesquisarUsuarios(pesquisa.toLowerCase());

            }
        });


        lerUsuarios();

        if (!((LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Call your Alert message
            pedirLigarGps();
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        lerUsuarios();

    }

    private void pesquisarUsuarios(final CharSequence pesquisa) {

        Query query;
        query = databaseReference.child("users");

        final Context context = getContext();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //List<Usuario> list = new ArrayList<>();
                List<Usuario> list = new ListaDistanciaUsuarios(context);
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Usuario usuario = snapshot.getValue(Usuario.class);

                    if(usuario.getNome().toLowerCase().contains(pesquisa)){
                        list.add(usuario);
                    }

                }

                setAdapter(view, list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void lerUsuarios(){
        Query query;
        query = databaseReference.child("users").orderByChild("id");
        final Context context = getContext();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Usuario> listaUsuarios = new ArrayList<>();
                List<Usuario> listaUsuarios = new ListaDistanciaUsuarios(context);
                for (DataSnapshot objSnapShot:dataSnapshot.getChildren()){
                    Usuario usuario = objSnapShot.getValue(Usuario.class);

                    listaUsuarios.add(usuario);
                }
                setAdapter(view, listaUsuarios);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setAdapter(View view, List<Usuario> listUsuario) {

        if (((LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ((ListaDistanciaUsuarios) listUsuario).ordenarLista();
            vendedorAdapter = new VendedorAdapter(listUsuario, this);
            mRecyclerView.setAdapter(vendedorAdapter);

        }
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
        Usuario usuarioAtual = (Usuario) object;

        Intent paginaVendedor = new Intent(getContext(), PaginaVendedor.class);
        Bundle bundle = new Bundle();
        bundle.putString("idVendedor", usuarioAtual.getId());
        paginaVendedor.putExtras(bundle);
        startActivity(paginaVendedor);
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

    private void pedirLigarGps() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Configuração GPS");
        alertDialog.setMessage("Por favor, ligue o GPS para melhorar a experiência");
        alertDialog.setPositiveButton("Configurações", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getContext().startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

}
