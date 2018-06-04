package com.example.anafl.projetofirebase.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.anafl.projetofirebase.Activity.AdicionarPratoActivity;
import com.example.anafl.projetofirebase.Activity.EditarPerfil;
import com.example.anafl.projetofirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;


public class AlterarPerfil extends Fragment {


    private Button btnDescricao;

    private View view;


    private OnFragmentInteractionListener mListener;

    public AlterarPerfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_alterar_perfil, container, false);


        final TextView tvwDescricao = (TextView) view.findViewById(R.id.edtDescricao);
        DatabaseReference descricaoReferencia = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("descricao");

        final ImageView imageView2 = (ImageView) view.findViewById(R.id.imageView2);

        DatabaseReference imagemReferencia = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid()).child("imagemPerfil");

        final TextView tvwNome = (TextView) view.findViewById(R.id.edtNome);
        DatabaseReference nomeDescricao = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("nome");

        //Glide.with(this).asBitmap().load(url).into(imageView2);

        //DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(url2);

        //Glide.with(this).asBitmap().load(url2).into(imageView2);


        //Glide.with(imageView2.getContext()).asBitmap().load("https://firebasestorage.googleapis.com/v0/b/meuappfirebase-e8b2b.appspot.com/o/Imagens%20Perfil%2F1527747943609.jpg?alt=media&token=34df4023-676a-44e0-b1fd-21d12f1bf090").into(new SimpleTarget<Bitmap>() {
         //   @Override
         //   public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

         //   }
      //  });

        ValueEventListener nomeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String descricao = dataSnapshot.getValue(String.class);
                tvwNome.setText(descricao);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
        nomeDescricao.addValueEventListener(nomeListener);

        ValueEventListener descricaoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String descricao = dataSnapshot.getValue(String.class);
                tvwDescricao.setText(descricao);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
        descricaoReferencia.addValueEventListener(descricaoListener);


        ValueEventListener imageListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String imagemUri = dataSnapshot.getValue(String.class);

                Glide.with(imageView2.getContext()).asBitmap().load(imagemUri).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        imageView2.setImageBitmap(resource);
                   }
               });
           }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
        imagemReferencia.addValueEventListener(imageListener);





        btnDescricao = (Button) view.findViewById(R.id.btnDescricao);
        btnDescricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EditarPerfil.class);
                startActivity(i);
            }
        });



        return view;

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
