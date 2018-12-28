package com.example.asus.taimusik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class admin_upload3 extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload3);
        pindah();
    }

    private void pindah(){
        TextView tv = (TextView)findViewById(R.id.kat1);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(admin_upload3.this,admin_upload_lagi.class);
                        startActivity(intent);
                    }
                });
    }
}
