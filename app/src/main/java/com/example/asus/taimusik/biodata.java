package com.example.asus.taimusik;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.time.MonthDay;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class biodata extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;
    private FirebaseAuth auth;
    Uri img;
    Button tombol_Lanjut;
    TextView result;
    EditText tanggallahir;
    EditText Kota;
    EditText Kelamin;
    EditText namaas;
    EditText namapg;
    RadioGroup gender;
    ImageView foto;
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
        Kota.requestFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        auth = FirebaseAuth.getInstance();
        tanggalan();
        namaasli();
        namapgn();
        kota();
        ganti();

        fotopp();
    }

    private void ganti() {
        result = (TextView) findViewById(R.id.tanggal);
        tombol_Lanjut = (Button) findViewById(R.id.tombol_lanjut);
        mStorageRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());


        tombol_Lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = namapg.getText().toString();
                if (nama.isEmpty()){
                    Toast.makeText(biodata.this, "Isi Semua", Toast.LENGTH_SHORT).show();
                }


                final StorageReference imgRef = mStorageRef.child("images/").child("profile.jpg");

                    imgRef.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String masukGambar = taskSnapshot.getDownloadUrl().toString();
                            String masukkota = Kota.getText().toString().trim();
                            String masukkalender = tanggallahir.getText().toString().trim();
                            String masuknama = namaas.getText().toString().trim();
                            String masuknamapg = namapg.getText().toString().trim();

                            profileinformation Pi = new profileinformation (masukGambar, masuknama, masuknamapg, masukkota, masukkalender);
                            FirebaseUser user = auth.getCurrentUser();
                            databaseReference.child(user.getUid()).setValue(Pi);
                            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                }
                            });
                        }
                    });
                Toast.makeText(biodata.this, "Data Sudah di Save", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(biodata.this, halhome1.class));
                //}


            }
        });

    }

    private void tanggalan() {
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        tanggallahir = (EditText) findViewById(R.id.tanggal);
        tanggallahir.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDateDialog();
                }
            }
        });
    }


    private void kota() {
        Kota = (EditText) findViewById(R.id.kota);
    }


    private void fotopp() {
        foto = (ImageView) findViewById(R.id.foto_profile);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               pindahfoto();
            }
        });
    }

    private void pindahfoto (){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAutoZoomEnabled(true)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(biodata.this);
    }


    private void namaasli(){
        namaas = (EditText) findViewById(R.id.namaasli);

    }
    private void namapgn(){
        namapg= (EditText) findViewById(R.id.namapenguna);
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
