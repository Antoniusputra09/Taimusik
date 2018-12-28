package com.example.asus.taimusik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class halmasuk extends AppCompatActivity {

    Button tomboldaftar;
    Button buatmasuk;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halmasuk);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser!=null){
            startActivity(new Intent(halmasuk.this,halhome1.class));
            //startActivity(Intent);
            finish();
        }

        pindah();
        ganti();
    }

    private void pindah(){
        Button tomboldaftar = (Button)  findViewById(R.id.tomboldaftar);
        tomboldaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(halmasuk.this,haldaftar.class));
            }
        });
    }

    private void ganti(){
        Button buatmasuk = (Button) findViewById(R.id.tombolmasuk);
        buatmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(halmasuk.this,halmasuk2.class));
            }
        });
    }
}
