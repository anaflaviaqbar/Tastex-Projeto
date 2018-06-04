package com.example.anafl.projetofirebase.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anafl.projetofirebase.Entidades.Prato;
//import com.example.anafl.projetofirebase.Manifest;
import com.example.anafl.projetofirebase.R;
import com.example.anafl.projetofirebase.Upload;
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

import java.io.IOException;
import java.util.UUID;

public class AdicionarPratoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button btnAddPrato;
    private EditText nomePrato;
    private EditText descPrato;
    private EditText precoPrato;
    private String idPrato;

    private Button mSelectImage;
    private ImageView imageView;
    private Uri filePath;
    private Button  openImage;
    private Uri mImageUri;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 1;

    //Imagem
    private Button btnEscolherImagem;
    private Button btnUploadImagem;

    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    private ProgressBar progressBar;

    private DatabaseReference mDatabaseRefImg;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_prato);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();

        nomePrato = (EditText) findViewById(R.id.edtNomePrato);
        descPrato = (EditText) findViewById(R.id.edtDescPrato);
        precoPrato = (EditText) findViewById(R.id.edtPrecoPrato);
        //openImage = (Button) findViewById(R.id.openImage);

        //imagem
        btnEscolherImagem = (Button) findViewById(R.id.btnEscolherImagem);
        btnUploadImagem = (Button) findViewById(R.id.btnUploadImagem);
        imageView = (ImageView) findViewById(R.id.imagemEscolhida);
        mStorageRef = FirebaseStorage.getInstance().getReference("Imagens Prato");
        mDatabaseRefImg = FirebaseDatabase.getInstance().getReference("Imagens Prato");
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        inicializaIdPrato();

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
                    Toast.makeText(AdicionarPratoActivity.this, "Imagem sendo carregada", Toast.LENGTH_SHORT).show();
                }
                uploadImagem();
            }
        });





        btnAddPrato = (Button) findViewById(R.id.btnAddPrato);
        btnAddPrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewPrato(firebaseUser.getUid());
                Toast.makeText(AdicionarPratoActivity.this, "Prato cadastrado!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        /*
        openImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadastrarImagemIntent = new Intent(AdicionarPratoActivity.this, AdicionarImagem.class);
                Bundle bundle = new Bundle();
                bundle.putString("idPrato", idPrato);
                cadastrarImagemIntent.putExtras(bundle);
                startActivity(cadastrarImagemIntent);
            }
        });
        */

  /*  mSelectImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (ActivityCompat.checkSelfPermission(AdicionarPratoActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AdicionarPratoActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_INTENT);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_INTENT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
    }
*/
    }

    public void abrirImagens(){
        try {
            if (ActivityCompat.checkSelfPermission(AdicionarPratoActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AdicionarPratoActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                } else {
                    Toast.makeText(AdicionarPratoActivity.this, "Habilite permissão", Toast.LENGTH_LONG).show();
                }
                break;
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
                            Toast.makeText(AdicionarPratoActivity.this, "Imagem adicionada", Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(taskSnapshot.getDownloadUrl().toString(), idPrato);
                            String uploadId = mDatabaseRefImg.push().getKey();
                            mDatabaseRefImg.child(uploadId).setValue(upload);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdicionarPratoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void inicializaIdPrato(){
        idPrato = mDatabase.child("pratos").push().getKey();

    }
    public void writeNewPrato (String userId){

        Prato novoPrato = new Prato();

        novoPrato.setIdVendedor(userId);
        novoPrato.setNome(nomePrato.getText().toString());
        novoPrato.setDescricao(descPrato.getText().toString());
        novoPrato.setPreco(Float.parseFloat(precoPrato.getText().toString()));
        //novoPrato.setUidPrato(UUID.randomUUID().toString());
        novoPrato.setUidPrato(idPrato);

        mDatabase.child("pratos").child(idPrato).setValue(novoPrato);

    }
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case GALLERY_INTENT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_INTENT);
                } else {
                    Toast.makeText(AdicionarPratoActivity.this, "Habilite permissão", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== GALLERY_INTENT && resultCode==RESULT_OK){

            filePath = data.getData();

            StorageReference filepath= mStorage.child("Imagens pratos").child(filePath.getLastPathSegment());
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }catch(IOException e){
                e.printStackTrace();
            }

            filepath.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(AdicionarPratoActivity.this, "Upload feito", Toast.LENGTH_LONG).show();
                }
            });
        }
    }*/
}