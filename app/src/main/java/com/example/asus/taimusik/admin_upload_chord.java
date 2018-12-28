package com.example.asus.taimusik;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class admin_upload_chord extends AppCompatActivity {

    ImageView img;
    EditText edit;
    Button btn;
    Uri gambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload_chord);
        img = (ImageView) findViewById(R.id.gambarchord);
        edit = (EditText) findViewById(R.id.namachord);
        btn = (Button) findViewById(R.id.simpanchord);

        gambar();
        simpan();

    }

    public void simpan(){

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String namachord = edit.getText().toString().trim();
                final StorageReference audioRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Images/*").child("chord.jpg").child(namachord);

                audioRef.putFile(gambar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        audioRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Chord").push();
                                postRef.child("namaChord").setValue(namachord);
                                postRef.child("gambarUrl").setValue(uri.toString());

                                Toast.makeText(admin_upload_chord.this, "upload success", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(admin_upload_chord.this,home_admin.class));
                                finish();
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
                .start(admin_upload_chord.this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri uri = result.getUri();
                gambar = uri;
                img.setImageURI(uri);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();

                if(BuildConfig.DEBUG) error.printStackTrace();
            }
        }
    }
}
