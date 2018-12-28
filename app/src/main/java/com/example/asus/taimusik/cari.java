package com.example.asus.taimusik;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class cari extends AppCompatActivity {

    private RecyclerView rc;
    private String urldatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari);
        rc = (RecyclerView) findViewById(R.id.rcycari);
        urldatabase = getIntent().getExtras().getString("cari");
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(cari.this));


    }
    @Override
    protected void onStart() {
        super.onStart();

                Query databaseReference = FirebaseDatabase.getInstance().getReference("Audio").orderByChild("judul"). startAt(urldatabase).endAt(urldatabase + "\uf8ff");
                FirebaseRecyclerAdapter<user_homelist, cari.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<user_homelist, cari.ViewHolder>(
                        user_homelist.class,
                        R.layout.pilihjudul,
                        cari.ViewHolder.class,
                        databaseReference
                ) {
                    @Override
                    protected void populateViewHolder(cari.ViewHolder viewHolder, user_homelist model, int position) {
                        final String postkey = getRef(position).getKey();
                        viewHolder.setNamaJudul(model.getJudul());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent in = new Intent(cari.this, putar_lagu2.class);
                                in.putExtra("id_audiooooo", postkey);
                                startActivity(in);

                                finish();
                            }
                        });
                    }
                };
                rc.setAdapter(firebaseRecyclerAdapter);




    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


        }

        public void setNamaJudul(String namaKategori) {
            TextView nama_view = mView.findViewById(R.id.banditem);
            nama_view.setText(namaKategori);
        }
    }


}
