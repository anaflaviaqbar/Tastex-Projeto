package com.example.anafl.projetofirebase.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.anafl.projetofirebase.Fragments.AlterarPerfil;
import com.example.anafl.projetofirebase.Fragments.Comprar;
import com.example.anafl.projetofirebase.Fragments.Compras;
import com.example.anafl.projetofirebase.Fragments.SolicitacoesCompra;
import com.example.anafl.projetofirebase.Fragments.SolicitacoesVenda;
import com.example.anafl.projetofirebase.Fragments.Vendas;
import com.example.anafl.projetofirebase.Fragments.Vender;
import com.example.anafl.projetofirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements SolicitacoesCompra.OnFragmentInteractionListener, Comprar.OnFragmentInteractionListener,
        Vender.OnFragmentInteractionListener,Vendas.OnFragmentInteractionListener,
        Compras.OnFragmentInteractionListener,SolicitacoesVenda.OnFragmentInteractionListener, AlterarPerfil.OnFragmentInteractionListener,
                NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DatabaseReference imagemReferencia = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid()).child("imagemPerfil");

        DatabaseReference nomeReferencia = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid()).child("nome");


        imagemReferencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String imagemUri = dataSnapshot.getValue(String.class);
                final ImageView imagemPerfil = findViewById(R.id.profileImageView);

                Glide.with(imagemPerfil.getContext()).asBitmap().load(imagemUri).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        imagemPerfil.setImageBitmap(resource);
                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        });

        nomeReferencia.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String nome = dataSnapshot.getValue(String.class);
                final TextView nomePerfil = findViewById(R.id.textNome);
                nomePerfil.setText(nome);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        });


        setTitle("Comprar");
        Comprar fragment = new Comprar();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fram, fragment, "Comprar");
        fragmentTransaction.commit();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.framCompra) {
            // Handle the camera action
            setTitle("Comprar");
            Comprar fragment = new Comprar();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "Comprar");
            fragmentTransaction.commit();
        } else if (id == R.id.framVenda) {
            setTitle("Vender");
            Vender fragment = new Vender();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "Vender");
            fragmentTransaction.commit();
        } else if (id == R.id.framSolicitacoesCompra) {
            setTitle("Solicitações Compra");
            SolicitacoesCompra fragment = new SolicitacoesCompra();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "Solicitações Compra");
            fragmentTransaction.commit();
        } else if (id == R.id.framSolicitacoesVenda) {
            setTitle("Solicitações Venda");
            SolicitacoesVenda fragment = new SolicitacoesVenda();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "Solicitações Venda");
            fragmentTransaction.commit();

        } else if (id == R.id.framHistoricoC) {
            setTitle("Historico de Compras");
            Compras fragment = new Compras();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "Compras");
            fragmentTransaction.commit();

        } else if (id == R.id.framHistoricoV) {
            setTitle("Historico de Vendas");
            Vendas fragment = new Vendas();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fram, fragment, "Vendas");
            fragmentTransaction.commit();
        }else if (id == R.id.framCadastro) {
                setTitle("Vender");
                AlterarPerfil fragment = new AlterarPerfil();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fram, fragment, "Alterar perfil");
                fragmentTransaction.commit();
        } else if (id == R.id.logout) {
            logout();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}