package com.example.asus.taimusik;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class haldaftar extends AppCompatActivity {

    String email, password, password2;
    private Button btn;
    private EditText inputnama, inputpenguna, inputemail, inputpassword, inputpassword2;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haldaftar);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        btn = (Button) findViewById(R.id.tomboldaftar2);
        inputemail = (EditText) findViewById(R.id.email);
        inputpassword = (EditText) findViewById(R.id.password1);
        inputpassword2 = (EditText) findViewById(R.id.password2);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap Tunggu");

        daftar();
    }

    private void daftar() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                awal();
            }
        });
    }

    private void awal() {

        email = inputemail.getText().toString().trim();
        password = inputpassword.getText().toString().trim();
        password2 = inputpassword2.getText().toString().trim();

        if (!syarat()) {
            progressDialog.dismiss();
            Toast.makeText(this, "Daftar gagal", Toast.LENGTH_SHORT).show();
        } else {
            sukses();
        }
    }

    private void sukses() {
        final FirebaseUser user = auth.getCurrentUser();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(haldaftar.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        Toast.makeText(haldaftar.this, "Tunggu", Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            Toast.makeText(haldaftar.this, "Daftar gagal" + task.getException(), Toast.LENGTH_SHORT).show();
                        } else {
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(haldaftar.this, "Cek Email Anda", Toast.LENGTH_SHORT).show();
                                        try {
                                            Thread.sleep(2000);
                                            startActivity(new Intent(haldaftar.this, loginregister.class));
                                            finish();
                                        } catch (Exception e) {
                                        }
                                    } else {
                                        Toast.makeText(haldaftar.this,"Berhasil", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(haldaftar.this, "Email Tidak dapat diverifikasi", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
    }

    private boolean syarat() {
        boolean benar = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Masukkan Email yang benar", Toast.LENGTH_SHORT).show();
            benar = false;

            if (password.isEmpty() && password2.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Isi password!", Toast.LENGTH_SHORT).show();
                benar = false;
            }
            if (!password2.equals(password)) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Password tak sama", Toast.LENGTH_SHORT).show();
                benar = false;
            }
            return benar;
        }

        return benar;
    }
}
