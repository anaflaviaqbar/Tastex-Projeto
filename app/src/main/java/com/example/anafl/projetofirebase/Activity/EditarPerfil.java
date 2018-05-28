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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class EditarPerfil extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;

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

    private EditText edtDescricaoPerfil;

    private ProgressBar progressBar;

    private String idPerfil;

    private Button btnAlteracaoFeita;

    private DatabaseReference mDatabaseRefImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();

        //imagem
        edtDescricaoPerfil = (EditText) findViewById(R.id.edtDescricaoPerfil);
        btnAlteracaoFeita = (Button) findViewById(R.id.btnAlteracaoFeita);
        btnEscolherImagem = (Button) findViewById(R.id.btnEscolherImagem);
        btnUploadImagem = (Button) findViewById(R.id.btnUploadImagem);
        imageView = (ImageView) findViewById(R.id.imagemEscolhida);
        mStorageRef = FirebaseStorage.getInstance().getReference("Imagens Perfil");
        mDatabaseRefImg = FirebaseDatabase.getInstance().getReference("Dados Perfil");
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

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
                    Toast.makeText(EditarPerfil.this, "Imagem sendo carregada", Toast.LENGTH_SHORT).show();
                }
                uploadImagem();
            }
        });
        btnAlteracaoFeita.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                     FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("descricao").setValue(edtDescricaoPerfil.getText().toString());
                                                     Toast.makeText(EditarPerfil.this, "Alteração realizada com sucesso!", Toast.LENGTH_SHORT).show();
                                                     finish();
                                                 }
                                             }
        );
    }

    public void abrirImagens(){
        try {
            if (ActivityCompat.checkSelfPermission(EditarPerfil.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditarPerfil.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
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
                    Toast.makeText(EditarPerfil.this, "Habilite permissão", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(EditarPerfil.this, "Imagem adicionada", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("imagemPerfil").setValue(taskSnapshot.getDownloadUrl().toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditarPerfil.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void inicializaIdPerfil(){
        idPerfil = mDatabase.child("perfil").push().getKey();
}
}
