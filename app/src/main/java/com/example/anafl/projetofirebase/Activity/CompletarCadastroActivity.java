package com.example.anafl.projetofirebase.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.anafl.projetofirebase.Entidades.Usuario;
import com.example.anafl.projetofirebase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class CompletarCadastroActivity extends AppCompatActivity {


    private EditText edtNomeCompCad;
    private EditText edtEmailCompCad;
    private EditText edtDataNascCompCad;
    private EditText edtContatoCompCad;
    private EditText edtEndereçoCompCad;

    private RadioGroup sexoCompCad;
    private RadioButton rbMasculino;
    private RadioButton rbFeminino;
    private RadioButton radioButton;

    private boolean isFeminino = false;
    private boolean isMasculino = false;

    private String uid;
    private String email;

    private Button btnGravarCompCad;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_cadastro);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            email = user.getEmail();
        } else {
            uid = null;
            email = null;
        }


        edtNomeCompCad = (EditText) findViewById(R.id.edtNomeCompCad);
        edtEmailCompCad = (EditText) findViewById(R.id.edtEmailCompCad);
        edtDataNascCompCad = (EditText) findViewById(R.id.edtDataNascCompCad);
        edtEndereçoCompCad = (EditText) findViewById(R.id.edtEndereçoCompCad);

        sexoCompCad = (RadioGroup) findViewById(R.id.sexoCompCad);
        rbFeminino = (RadioButton) findViewById(R.id.rbFemininoCompCad);
        rbMasculino = (RadioButton) findViewById(R.id.rbMasculinoCompCad);


        edtEmailCompCad.setText(email);

        int radioId2 = sexoCompCad.getCheckedRadioButtonId();

        radioButton = (RadioButton) findViewById(radioId2);

        lerNomeComprador(uid);

        btnGravarCompCad = (Button) findViewById(R.id.btnGravarCompCad);
        btnGravarCompCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewUser(uid);
                Intent mainAct = new Intent(CompletarCadastroActivity.this, MainActivity.class);
                startActivity(mainAct);
                finish();
            }
        });
    }

    private void writeNewUser(String userId) {
        Usuario usuario = new Usuario();

        usuario.setNome(edtNomeCompCad.getText().toString());
        usuario.setEmail(edtEmailCompCad.getText().toString());
        if(rbMasculino.isChecked()){
            usuario.setSexo(rbMasculino.getText().toString());
        }else if(rbFeminino.isChecked()){
            usuario.setSexo(rbFeminino.getText().toString());
        }
        //usuario.setSexo(radioButton.getText().toString());
        usuario.setDataNasc(edtDataNascCompCad.getText().toString());
        usuario.setEndereco(edtEndereçoCompCad.getText().toString());
        usuario.setLatitudeLongitude(this);
        usuario.setId(userId);
        usuario.setDescricao("");
        usuario.setImagemPerfil("");

        mDatabase.child("users").child(userId).setValue(usuario);
        //Task escreverUsuarioGmailTask = mDatabase.child("users").child(userId).setValue(usuario);
        /*
        escreverUsuarioGmailTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Intent mainAct = new Intent(CompletarCadastroActivity.this, MainActivity.class);
                startActivity(mainAct);
                finish();
            }
        });
        escreverUsuarioGmailTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CompletarCadastroActivity.this, "Cadastro sem sucesso!", Toast.LENGTH_LONG).show();
            }
        });*/
//        mDatabase.child("users").child(userId).setValue(new Usuario());
//        mDatabase.child("users").child(userId).setValue(new Usuario());
//        mDatabase.child("users").child(userId).setValue(new Usuario());
//        mDatabase.child("users").child(userId).setValue(new Usuario());
//        mDatabase.child("users").child(userId).setValue(new Usuario());
//        mDatabase.child("users").child(userId).setValue(new Usuario());
    }

    public void checaSexo(View v) {
        int radioId = sexoCompCad.getCheckedRadioButtonId();

        radioButton = (RadioButton) findViewById(radioId);

    }

    private void lerNomeComprador(String id){

        Query queryV = mDatabase.child("users").child(id);
        queryV.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuarioComCad = dataSnapshot.getValue(Usuario.class);
                //txtNomeVendedor.setText(nomeVendedor);

                if(!(usuarioComCad == null)){
                    if (!(usuarioComCad.getNome() == null)) {
                        edtNomeCompCad.setText(usuarioComCad.getNome());
                    }
                    if (!(usuarioComCad.getDataNasc() == null)) {
                        edtDataNascCompCad.setText(usuarioComCad.getDataNasc());
                    }
                    if (!(usuarioComCad.getSexo() == null)) {
                        if (usuarioComCad.getSexo().equals("Masculino")) {
                            rbMasculino.setChecked(true);
                            rbFeminino.setChecked(false);
                        } else {
                            rbFeminino.setChecked(true);
                            rbMasculino.setChecked(false);
                        }
                    }
                    if (!(usuarioComCad.getEndereco() == null)) {
                        edtEndereçoCompCad.setText(usuarioComCad.getEndereco());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
