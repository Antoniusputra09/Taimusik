package com.example.asus.taimusik;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class halmasuk2 extends AppCompatActivity {

    private Button in;
    private EditText inputEmail, inputPassword;
    String  email, password;
    private FirebaseAuth auth;
    private TextView lupa;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halmasuk2);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password1);
        in = (Button) findViewById(R.id.tombolmasuk2);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap Tunggu");



        daftar();
        ganti();

    }

    private  void daftar(){
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                cocok();
            }
        });
    }

    private void cocok(){
        if(TextUtils.isEmpty(email)){
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Masukkan Email!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Masukkan Password!!",Toast.LENGTH_SHORT).show();
            return;
        }
        pindah();
    }

    private void pindah(){
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(halmasuk2.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (email.equals("antonius@gmail.com") && password.equals("iniadmin")) {
                            progressDialog.dismiss();
                            Intent intent = new Intent (halmasuk2.this, home_admin.class);
                            startActivity(intent);
                        }
                        else {
                            if(!task.isSuccessful()) {
                                if (password.length()<5){
                                    progressDialog.dismiss();
                                    inputPassword.setError("Terlalu pendek!");
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Daftar gagal, periksa Email dan Password atau Daftar",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                progressDialog.dismiss();
                                Intent intent= new Intent(halmasuk2.this,halhome1.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                    }
                });
    }

    private void ganti(){
        TextView lupa =(TextView) findViewById(R.id.lupa_pass);
        lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(halmasuk2.this,lupapass1.class));
            }
        });
    }
}

