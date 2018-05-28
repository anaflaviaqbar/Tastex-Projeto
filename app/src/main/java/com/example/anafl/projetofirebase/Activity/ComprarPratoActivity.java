package com.example.anafl.projetofirebase.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anafl.projetofirebase.R;

public class ComprarPratoActivity extends AppCompatActivity {

    private String nomePrato;
    private String descPrato;
    private float precoPrato;

    private TextView nomePratoComprarPrato;
    private TextView descPratoComprarPrato;
    private TextView precoPratoComprarPrato;

    private Button comprarPrato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_prato);


        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        nomePrato = bundle.getString("nome");
        descPrato = bundle.getString("descricao");
        precoPrato = bundle.getFloat("preco");

        nomePratoComprarPrato = (TextView) findViewById(R.id.txtNomePratoComprarPrato);
        descPratoComprarPrato = (TextView) findViewById(R.id.txtDescricaoPratoComprarPrato);
        precoPratoComprarPrato = (TextView) findViewById(R.id.txtPrecoPratoComprarPrato);

        nomePratoComprarPrato.setText(nomePrato);
        descPratoComprarPrato.setText(descPrato);
        precoPratoComprarPrato.setText(precoPrato + " R$");

        comprarPrato = (Button) findViewById(R.id.btnComprarPrato);
        comprarPrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ComprarPratoActivity.this, "Falta implementar", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
