package com.example.asus.taimusik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class tambah_kategori extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    Button btn;
    EditText txt;
    StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kategori);

        btn=findViewById(R.id.btn_kategori_kirim);
        txt=findViewById(R.id.edit_txt1);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = txt.getText().toString().trim();
                FirebaseDatabase.getInstance().getReference("Kategori").push().child("namaKategori").setValue(nama);

                startActivity(new Intent(tambah_kategori.this, admin_kategori.class));
            }
        });
    }
}
