package com.example.asus.taimusik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class setelan_pass extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setelan_pass);

        pindah();
    }

    private void pindah(){
        Button btn = (Button) findViewById(R.id.kirim);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(setelan_pass.this,biodata.class));
            }
        });
    }
}
