package com.example.asus.taimusik;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class tambah_band extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    Button btn;
    EditText txt;
    Spinner sp1;
    StorageReference mStorageRef;
    ImageView foto;
    Uri img;
    private ArrayList<String> kategori = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_band);
        btn=findViewById(R.id.btn_band_kirim);
        txt=findViewById(R.id.edit_txt_band);
        sp1=findViewById(R.id.spinner1);


        fotoapp();

        DatabaseReference katref = FirebaseDatabase.getInstance().getReference("Kategori");
        katref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String kat = ds.child("namaKategori").getValue().toString();
                    kategori.add(kat);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(tambah_band.this,android.R.layout.simple_spinner_item, kategori);
                sp1.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final StorageReference imgRef = mStorageRef.child("images/").child("fotoband.jpg");
                final String kategori_band = sp1.getSelectedItem().toString();
                final String nama = txt.getText().toString().trim();

                final StorageReference imgRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Images/*").child("fotoBAnd.jpg").child(nama);

                imgRef.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Band").push();
                                databaseReference.child("namaBand").setValue(nama);
                                databaseReference.child("namaKategori").setValue(kategori_band);
                                databaseReference.child("ImgUrl").setValue(uri.toString());
                                startActivity(new Intent(tambah_band.this, admin_band.class));

                            }
                        });
                    }
                });

            }
        });


    }

    private void fotoapp(){
        foto = (ImageView) findViewById(R.id.ambil_gambar1);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pindahfoto();
            }
        });
    }

    private void pindahfoto(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAutoZoomEnabled(true)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(tambah_band.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri uri = result.getUri();
                img = uri;
                foto.setImageURI(uri);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();

                if(BuildConfig.DEBUG) error.printStackTrace();
            }
        }
    }


}
