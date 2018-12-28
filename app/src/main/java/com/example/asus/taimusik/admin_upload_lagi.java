package com.example.asus.taimusik;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;

public class admin_upload_lagi extends AppCompatActivity {

    private StorageMetadata imgf;
    private Uri file;
    ImageView images;
    ImageView images2;
    EditText text;
    EditText text2;
    MediaController md;
    VideoView vv;
    Spinner sp1;
    Spinner sp2;
    Button btn;
    private Uri audioUri, imgUri;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    StorageReference mStorageRef;

    private ArrayList<String> band = new ArrayList<String>();
    private Object audio;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload_lagi);
        images = (ImageView) findViewById(R.id.ambil_lagu);
        text = (EditText) findViewById(R.id.edit1);
        text2 = (EditText) findViewById(R.id.edit2);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap Tunggu!");
        sp1 = (Spinner) findViewById(R.id.spinner1);
        sp2 = (Spinner) findViewById(R.id.spinner2);
        btn = (Button) findViewById(R.id.btn_upload_konten);
        images2 = (ImageView) findViewById(R.id.ambil_fotolagu);



        DatabaseReference bandref = FirebaseDatabase.getInstance().getReference("Band");
        bandref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp: dataSnapshot.getChildren()){
                    String bd = dsp.child("namaBand").getValue().toString();
                    band.add(bd);
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(admin_upload_lagi.this,android.R.layout.simple_spinner_item, band);
                sp2.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(admin_upload_lagi.this, "Gagal memuat kategori : " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });

        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Audio"),89);

                /*Toast.makeText(admin_upload_lagi.this, "blabla", Toast.LENGTH_LONG).show();
                Intent intent =new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI);
                intent.setType("audio/*");
                startActivityForResult(Intent.createChooser(intent, "Pilih Video"), 89);*/
            }

        });

        images2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gambar();
            }
        });




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                final String judulLagu = text.getText().toString().trim();
                final String lirikLagu = text2.getText().toString().trim();

                final String pil_band = sp2.getSelectedItem().toString();
                final StorageReference audioRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Audio/*").child("lagu.mp3").child(judulLagu);
                final StorageReference imgRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Images/*").child("fotolagu.jpg").child(judulLagu);
                final UploadTask Uploadaudio = audioRef.child(judulLagu).putFile(audioUri);
                final UploadTask Uploadgambar = audioRef.child(judulLagu + "Gambar").putFile(imgUri);
                Uploadaudio.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     Uploadgambar.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                         @Override
                         public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                             audioRef.child(judulLagu).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                 @Override
                                 public void onSuccess(final Uri uri) {
                                     audioRef.child(judulLagu+"Gambar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                         @Override
                                         public void onSuccess(Uri uri2) {
                                             DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Audio").push();
                                             postRef.child("judul").setValue(judulLagu);
                                             postRef.child("lirik").setValue(lirikLagu);
                                             postRef.child("Band").setValue(pil_band);
                                             postRef.child("UID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                             postRef.child("audioUrl").setValue(uri.toString());
                                             postRef.child("imageUrl").setValue(uri2.toString());
                                             progressDialog.dismiss();
                                             Toast.makeText(admin_upload_lagi.this, "upload success", Toast.LENGTH_LONG).show();
                                             startActivity(new Intent(admin_upload_lagi.this,home_admin.class));
                                             finish();
                                         }
                                     });


                                 }
                             });
                         }
                     });


                    }
                });
            }
        });

    }

    private void gambar (){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAutoZoomEnabled(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(admin_upload_lagi.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri uri = result.getUri();
                imgUri = uri;
                images2.setImageURI(uri);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();

                if(BuildConfig.DEBUG) error.printStackTrace();
            }
        }

        if (resultCode == RESULT_OK){
            if(requestCode == 89){
                Uri select = data.getData();
                audioUri = select;


            }
        }

    }

}
