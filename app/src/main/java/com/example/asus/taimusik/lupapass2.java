package com.example.asus.taimusik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class lupapass2 extends AppCompatActivity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupapass2);

                pindah();
    }

    private void pindah(){
        Button btn =(Button) findViewById(R.id.masuk_lupa);
        btn.setOnClickListener(     new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(lupapass2.this,halhome1.class));
            }
        });
    }
}
