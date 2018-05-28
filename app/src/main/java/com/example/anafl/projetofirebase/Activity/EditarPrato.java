package com.example.anafl.projetofirebase.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anafl.projetofirebase.Entidades.Prato;
import com.example.anafl.projetofirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditarPrato extends AppCompatActivity {


    private DatabaseReference mDatabaseReference;

    private String uid;


    private String nomePrato;
    private String descPrato;
    private float precoPrato;
    private String idVendedor;
    private String uidPrato;
    private int tipoPrato;
    private Spinner spinTipoPrato;

    private EditText edtNomePrato;
    private EditText edtDescPrato;
    private EditText edtPrecoPrato;
    //private EditText edtIdVendedorPrato;

    private Button excluirPrato;
    private Button editarPrato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_prato);

        //instanciarFirebase();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        nomePrato = bundle.getString("nome");
        descPrato = bundle.getString("descricao");
        precoPrato = bundle.getFloat("preco");
        idVendedor = bundle.getString("idVendedor");
        uidPrato = bundle.getString("uidPrato");
        tipoPrato = 0;

        edtNomePrato = (EditText)findViewById(R.id.edtNomePratoEditAct);
        edtNomePrato.setText(nomePrato);
        edtDescPrato = (EditText)findViewById(R.id.edtDescPratoEditAct);
        edtDescPrato.setText(descPrato);
        edtPrecoPrato = (EditText)findViewById(R.id.edtPrecoPratoEditAct);
        edtPrecoPrato.setText(precoPrato + "");
        //edtIdVendedorPrato = (EditText)findViewById(R.id.edtIdVendedorEditAct);
        //edtIdVendedorPrato.setText(idVendedor);


        excluirPrato = (Button)findViewById(R.id.btnExcluirPrato);
        excluirPrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.child("pratos").child(uidPrato).removeValue();
                Toast.makeText(EditarPrato.this, "Prato foi excluído!", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        editarPrato = (Button)findViewById(R.id.btnEditarPrato);
        editarPrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Prato pratoEditado = new Prato();

                pratoEditado.setUidPrato(uidPrato);
                pratoEditado.setNome(edtNomePrato.getText().toString());
                pratoEditado.setDescricao(edtDescPrato.getText().toString());
                pratoEditado.setPreco(Float.parseFloat(edtPrecoPrato.getText().toString()));
                //pratoEditado.setIdVendedor(edtIdVendedorPrato.getText().toString());

                mDatabaseReference.child("pratos").child(uidPrato).setValue(pratoEditado);

                Toast.makeText(EditarPrato.this, "Prato editado!", Toast.LENGTH_SHORT).show();
            }
        });



        // Configuração do Spinner
        spinTipoPrato = (Spinner) findViewById(R.id.spinTipoPratoEditarPrato);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos_pratos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTipoPrato.setAdapter(adapter);
        spinTipoPrato.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String stgTipoPrato = parent.getItemAtPosition(position).toString();
                switch (stgTipoPrato){
                    case "Sem Classificação":
                        tipoPrato = 0;
                        break;
                    case "Normal":
                        tipoPrato = 1;
                        break;
                    case "Low Carb":
                        tipoPrato = 2;
                        break;
                    case "Vegetariano":
                        tipoPrato = 3;
                        break;
                    case "Vegano":
                        tipoPrato = 4;
                        break;
                    default:
                        tipoPrato = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tipoPrato = 0;
            }
        });
        //Fim Configuração Spinner



    }

    private void instanciarFirebase(){
        uid = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uid = user.getUid();
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }
}
