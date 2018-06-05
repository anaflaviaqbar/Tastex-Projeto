package com.example.anafl.projetofirebase.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anafl.projetofirebase.Entidades.Prato;
import com.example.anafl.projetofirebase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditarPrato extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private DatabaseReference mDatabaseReference;

    private String uid;


    private String idVendedor;
    private String nomePrato;
    private float precoPrato;
    private String descPrato;
    private String uidPrato;
    private int tipoPrato;
    private String imgPratoUrl;
    private Spinner spinTipoPrato;

    private EditText edtNomePrato;
    private EditText edtDescPrato;
    private EditText edtPrecoPrato;
    //private EditText edtIdVendedorPrato;

    private Button excluirPrato;
    private Button editarPrato;

    //Imagem
    private String imgPratoUrlNova;
    private Uri mImageUri;

    private ImageView imageView;

    private Button btnEscolherImagem;
    private Button btnUploadImagem;

    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_prato);

        //instanciarFirebase();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        idVendedor = bundle.getString("idVendedor");
        nomePrato = bundle.getString("nome");
        precoPrato = bundle.getFloat("preco");
        descPrato = bundle.getString("descricao");
        uidPrato = bundle.getString("uidPrato");
        tipoPrato = bundle.getInt("tipoPrato");
        imgPratoUrl = bundle.getString("imgPratoUrl");
        imgPratoUrlNova = " ";

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

                pratoEditado.setIdVendedor(idVendedor);
                pratoEditado.setNome(edtNomePrato.getText().toString());
                pratoEditado.setPreco(Float.parseFloat(edtPrecoPrato.getText().toString()));
                pratoEditado.setDescricao(edtDescPrato.getText().toString());
                pratoEditado.setUidPrato(uidPrato);
                pratoEditado.setTipoPrato(tipoPrato);
                if(imgPratoUrlNova == " ") {
                    pratoEditado.setImgPratoUrl(imgPratoUrl);
                }else{
                    pratoEditado.setImgPratoUrl(imgPratoUrlNova);
                }
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
        selecionarPosicaoSpinner();
        spinTipoPrato.setSelection(adapter.getPosition(selecionarPosicaoSpinner()));
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

        //Imagem
        btnEscolherImagem = (Button) findViewById(R.id.btnEscolherImagemEditarPrato);
        btnUploadImagem = (Button) findViewById(R.id.btnUploadImagemEditarPrato);
        imageView = (ImageView) findViewById(R.id.imagemEscolhidaEditarPrato);
        mStorageRef = FirebaseStorage.getInstance().getReference("Imagens Prato");
        //mDatabaseRefImg = FirebaseDatabase.getInstance().getReference("Imagens Prato");
        progressBar = (ProgressBar) findViewById(R.id.progressBarEditarPrato);

        btnEscolherImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirImagens();
            }
        });

        btnUploadImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(EditarPrato.this, "Imagem sendo carregada", Toast.LENGTH_SHORT).show();
                }
                uploadImagem();
            }
        });



    }

    //Imagens
    public void uploadImagem(){
        if(mImageUri != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            mUploadTask= fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 5000);
                            //Upload upload = new Upload(taskSnapshot.getDownloadUrl().toString(), idPrato);
                            imgPratoUrlNova = taskSnapshot.getDownloadUrl().toString();
                            //String uploadId = mDatabaseRefImg.push().getKey();
                            //mDatabaseRefImg.child(uploadId).setValue(upload);
                            Toast.makeText(EditarPrato.this, "Imagem adicionada", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditarPrato.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });

        }else {
            Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show();
        }
    }

    public void abrirImagens(){
        try {
            if (ActivityCompat.checkSelfPermission(EditarPrato.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditarPrato.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(imageView);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                } else {
                    Toast.makeText(EditarPrato.this, "Habilite permissão", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    //Fim Imagens

    private String selecionarPosicaoSpinner() {
        String spinTipoPrato;

        switch (tipoPrato){
            case 0:
                spinTipoPrato = "Sem Classificação";
                break;
            case 1:
                spinTipoPrato = "Normal";
                break;
            case 2:
                spinTipoPrato = "Low Carb";
                break;
            case 3:
                spinTipoPrato = "Vegetariano";
                break;
            case 4:
                spinTipoPrato = "Vegano";
                break;
            default:
                spinTipoPrato = "Sem Classificação";
        }

        return spinTipoPrato;
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
