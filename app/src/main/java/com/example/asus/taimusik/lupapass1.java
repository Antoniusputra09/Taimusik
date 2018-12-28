package com.example.asus.taimusik;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class lupapass1 extends AppCompatActivity {

    private EditText username, email;
    private Button btn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupapass1);
        auth = FirebaseAuth.getInstance();

        pindah();
    }

    public void pindah() {

        username = (EditText) findViewById(R.id.username);
        btn = (Button) findViewById(R.id.lupa_lajut);
        email = (EditText) findViewById(R.id.email);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usermail = email.getText().toString().trim();
                if (username.equals("") && email.equals("")) {
                    Toast.makeText(lupapass1.this, "Login dahulu", Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(usermail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(lupapass1.this, "Lihat Email Anda", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(lupapass1.this, halmasuk2.class));
                            } else {
                                Toast.makeText(lupapass1.this, "Gagal!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                startActivity(new Intent(lupapass1.this,halmasuk2.class));
            }

        });


    }
}
