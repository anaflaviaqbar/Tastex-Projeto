package com.example.anafl.projetofirebase.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.anafl.projetofirebase.Entidades.Usuario;
import com.example.anafl.projetofirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompletarCadastroActivity extends AppCompatActivity {


    private EditText edtNomeCompCad;
    private EditText edtEmailCompCad;
    private EditText edtCepCompCad;
    private EditText edtDataNascCompCad;
    private EditText edtContatoCompCad;

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
        edtCepCompCad = (EditText) findViewById(R.id.edtCepCompCad);
        edtDataNascCompCad = (EditText) findViewById(R.id.edtDataNascCompCad);
        edtContatoCompCad = (EditText) findViewById(R.id.edtContatoCompCad);

        sexoCompCad = (RadioGroup) findViewById(R.id.sexoCompCad);
        //rbFeminino = (RadioButton) findViewById(R.id.rbFemininoCompCad);
        //rbMasculino = (RadioButton) findViewById(R.id.rbMasculinoCompCad);


        edtEmailCompCad.setText(email);

        btnGravarCompCad = (Button) findViewById(R.id.btnGravarCompCad);
        btnGravarCompCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewUser(uid);
                Intent mainAct = new Intent(CompletarCadastroActivity.this, MainActivity.class);
                startActivity(mainAct);
            }
        });
    }

    private void writeNewUser(String userId) {
        Usuario usuario = new Usuario();

        usuario.setNome(edtNomeCompCad.getText().toString());
        usuario.setEmail(edtEmailCompCad.getText().toString());
        usuario.setCep(edtCepCompCad.getText().toString());
        usuario.setTelefone(edtDataNascCompCad.getText().toString());
        usuario.setDataNasc(edtContatoCompCad.getText().toString());
        usuario.setSexo(radioButton.getText().toString());
        usuario.setId(userId);
        usuario.setDescricao("");
        usuario.setImagemPerfil("");

        mDatabase.child("users").child(userId).setValue(usuario);
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
}
