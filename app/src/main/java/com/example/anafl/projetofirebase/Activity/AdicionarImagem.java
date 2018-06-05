package com.example.anafl.projetofirebase.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anafl.projetofirebase.R;
import com.example.anafl.projetofirebase.Upload;
import com.example.anafl.projetofirebase.showImages;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Picasso;

public class AdicionarImagem extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button buttonUpload;
    private Button buttonChoose;
    private TextView showUpload;
    private EditText editText;

    private ImageView imageView;
    private ProgressBar progressBar;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private boolean res= false;

    private StorageTask mUploadTask;

    private Uri mImageUri;

    private String idPrato;

    public AdicionarImagem() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_imagem);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        idPrato = bundle.getString("idPrato");

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        showUpload = (TextView) findViewById(R.id.showUpload);

        mStorageRef = FirebaseStorage.getInstance().getReference("Imagens");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("imagens");


        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirImagens();
            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validaCampos(res)==false){
                } else if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(AdicionarImagem.this, "Imagem sendo carregada", Toast.LENGTH_SHORT).show();
                }
                uploadImagem();
            }
        });
        showUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openImagesActivity();
            }
        });
            }
    private void abrirImagens() {
        try {
            if (ActivityCompat.checkSelfPermission(AdicionarImagem.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AdicionarImagem.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                } else {
                    Toast.makeText(AdicionarImagem.this, "Habilite permissão", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

         //   Picasso.get().load(mImageUri).into(imageView);
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
                    Toast.makeText(AdicionarImagem.this, "Imagem adicionada", Toast.LENGTH_SHORT).show();
                    Upload upload = new Upload(taskSnapshot.getDownloadUrl().toString(), idPrato);
                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdicionarImagem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void openImagesActivity() {
        Intent intent = new Intent(this, showImages.class);
        startActivity(intent);
    }
    public boolean validaCampos(boolean res) {
        String nome = editText.getText().toString();


        if (this.res = campoVazio(nome)) {
            editText.requestFocus();
        }

        if (this.res) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Aviso");
            dlg.setMessage("Preencha o nome da imagem");
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
