package com.example.anafl.projetofirebase.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.anafl.projetofirebase.Entidades.Usuario;
import com.example.anafl.projetofirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.common.StringUtils;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtCadEmail;
    private EditText edtCadNome;
    private EditText edtCadContato;
    private EditText edtCadDataNasc;
    private EditText edtCadSenha;
    private EditText edtConfSenha;
    private RadioButton rbMasculino;
    private RadioButton rbFeminino;
    private EditText edtCadCep;
    private Button btnGravar;
//    private Usuario usuario;

    private FirebaseAuth mAuth;
    //private DatabaseReference database;
    private DatabaseReference mDatabase;

    private String uid;

    private boolean isFeminino = false;
    private boolean isMasculino = false;
    private boolean res= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();
        //database = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        edtCadEmail = (EditText) findViewById(R.id.edtCadEmail);
        edtCadNome = (EditText) findViewById(R.id.edtCadNome);
        edtCadCep = (EditText) findViewById(R.id.edtCadCep);
        edtCadDataNasc = (EditText) findViewById(R.id.edtCadDataNasc);
        edtCadSenha = (EditText) findViewById(R.id.edtCadSenha);
        edtConfSenha = (EditText) findViewById(R.id.edtConfSenha);
        rbFeminino = (RadioButton) findViewById(R.id.rbFeminino);
        rbMasculino = (RadioButton) findViewById(R.id.rbMasculino);
        edtCadContato = (EditText) findViewById(R.id.edtCadContato);


        btnGravar = (Button) findViewById(R.id.btnGravar);
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validaCampos(res)==false){
                    if (edtCadSenha.getText().toString().equals(edtConfSenha.getText().toString())) {
                        cadastrarUsuario(edtCadEmail.getText().toString(), edtCadSenha.getText().toString());
                    } else {
                        Toast.makeText(CadastroActivity.this, "Senhas diferentes!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void cadastrarUsuario(String email, String password) {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                writeNewUser(user.getUid());
                                Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_LONG).show();
                                finish();
                                //updateUI(user);
                            }
                        if(!task.isSuccessful()) {
                            try{
                                throw task.getException();
                            }catch(FirebaseAuthWeakPasswordException e) {
                                edtCadSenha.setError("Senha curta");
                            }catch(FirebaseAuthInvalidCredentialsException e){
                                edtCadEmail.setError("Email inválido");
                            }catch(FirebaseAuthUserCollisionException e){
                                edtCadEmail.setError("Email já existe");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CadastroActivity.this, "Authentication failed",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                        // ...
                    }
                });
    }

    /*
    public void abrirLoginUsuario() {
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void AbreRedefine() {
        Intent intentAbreRedefine = new Intent(CadastroActivity.this, EsqueceuSenha.class);
        startActivity(intentAbreRedefine);
        finish();
    }*/

   /* public void checaSexo(View v) {
        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()) {
            case R.id.rbFeminino:
                if (checked) {
                    isFeminino = true;
                    isMasculino = false;
                }
                break;
            case R.id.rbMasculino:
                if (checked) {
                    isFeminino = false;
                    isMasculino = true;
                }
        }
    }*/
    /*
        private void escreverNovoUsuario(String userId) {
            Usuario usuario = new Usuario();

            usuario.setNome(edtCadNome.getText().toString());
            usuario.setEmail(edtCadEmail.getText().toString());
            usuario.setCep(edtCadCep.getText().toString());
            usuario.setTelefone(edtCadContato.getText().toString());
            usuario.setDataNasc(edtCadDataNasc.getText().toString());
            usuario.setSenha(edtCadSenha.getText().toString());

            if(isFeminino){
                usuario.setSexo("Feminino");
            }else if(isMasculino){
                usuario.setSexo("Masculino");
            }
            usuario.setId(userId);

            database.child("users").child(userId).setValue(usuario);
        }
        */
    private void writeNewUser(String userId) {
        Usuario usuario = new Usuario();

        usuario.setNome(edtCadNome.getText().toString());
        usuario.setEmail(edtCadEmail.getText().toString());
        usuario.setCep(edtCadCep.getText().toString());
        usuario.setTelefone(edtCadContato.getText().toString());
        usuario.setDataNasc(edtCadDataNasc.getText().toString());
        usuario.setSenha(edtCadSenha.getText().toString());
        if(isFeminino){
            usuario.setSexo("Feminino");
        }else if(isMasculino){
            usuario.setSexo("Masculino");
        }
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
    public boolean validaCampos(boolean res) {
        String nome = edtCadNome.getText().toString();
        String email = edtCadEmail.getText().toString();
        String senha = edtCadSenha.getText().toString();
        String confsenha = edtConfSenha.getText().toString();
        String cep = edtCadCep.getText().toString();
        String datanasc = edtCadDataNasc.getText().toString();
        String telefone = edtCadContato.getText().toString();

        if (this.res = campoVazio(nome)) {
            edtCadNome.requestFocus();
        } else if (this.res = campoVazio(cep)) {
            edtCadCep.requestFocus();
        } else if (this.res = campoVazio(email)) {
            edtCadEmail.requestFocus();
        } else if (this.res = campoVazio(senha)) {
            edtCadSenha.requestFocus();
        } else if (this.res = campoVazio(confsenha)) {
            edtConfSenha.requestFocus();
        } else if (this.res = campoVazio(datanasc)) {
            edtCadDataNasc.requestFocus();
        } else if (this.res = campoVazio(telefone)) {
            edtCadContato.requestFocus();

        }

        if (this.res) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Aviso");
            dlg.setMessage("Preencha todos os campos");
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
        return this.res;
    }
    private boolean campoVazio(String valor){
        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }
}
