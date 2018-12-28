package com.example.asus.taimusik;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class edit_profile extends AppCompatActivity {

    private EditText namaas;
    private EditText namapg;
    private EditText kota;
    private EditText tanggal;
    private TextView kirim;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;
    ImageView img;
    ImageView img2;
    Uri imgprofile;
    TextView result;
    StorageReference mStorageRef;

    private void showDateDialog() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int i, int i1, int i2) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(i, i1, i2);
                result.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        kota.requestFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        img =(ImageView) findViewById(R.id.gambarpic_edit);
        img2=(ImageView) findViewById(R.id.view2);
        namaas = (EditText) findViewById(R.id.namaasli_edit);
        namapg = (EditText) findViewById(R.id.namapenguna_edit);
        kota = (EditText) findViewById(R.id.edit_kota);
        tanggal = (EditText) findViewById(R.id.edit_tanggal);
        kirim = (TextView) findViewById(R.id.edit_kirim);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        mStorageRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("images/profile.jpg");
        storageReference.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                DisplayMetrics displayMetrics = new DisplayMetrics();



                img2.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pindahfoto();
            }
        });

        edit();
        tanggalan();

    }

    private void pindahfoto(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAutoZoomEnabled(true)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(edit_profile.this);
    }


    private void edit() {
        databaseReference.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Nama = (String) dataSnapshot.child("nama_asli").getValue(String.class);
                String Username = (String) dataSnapshot.child("nama_user").getValue(String.class);
                String Tanggal = (String) dataSnapshot.child("tanggal").getValue(String.class);
                String Kotaa = (String) dataSnapshot.child("kota").getValue(String.class);

                namaas.setText(Nama);
                namapg.setText(Username);
                tanggal.setText(Tanggal);
                kota.setText(Kotaa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                final StorageReference imgRef = mStorageRef.child("images/").child("profile.jpg");

                imgRef.putFile(imgprofile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String MasukFoto = taskSnapshot.getDownloadUrl().toString();
                        String MasukNama = namaas.getText().toString().trim();
                        String MasukUser = namapg.getText().toString().trim();
                        String MasukTanggal = tanggal.getText().toString().trim();
                        String MasukKota = kota.getText().toString().trim();
                        profileinformation pi = new profileinformation(MasukFoto,MasukNama, MasukUser, MasukTanggal, MasukKota);
                        FirebaseUser User = auth.getCurrentUser();

                        databaseReference.child(User.getUid()).setValue(pi);
                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            }
                        });
                    }
                });
                Toast.makeText(edit_profile.this,"Data Tersimpan",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(edit_profile.this,halhome1.class);
                startActivity(intent);
            }
        });
    }
    private void tanggalan() {
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tanggal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDateDialog();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri uri = result.getUri();
                imgprofile = uri;
                img2.setImageURI(uri);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();

                if(BuildConfig.DEBUG) error.printStackTrace();
            }
        }
    }
}
