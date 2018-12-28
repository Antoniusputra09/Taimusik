package com.example.asus.taimusik;

import android.app.ProgressDialog;
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

public class daftarlirik extends AppCompatActivity {


    private RecyclerView recyclerband;
    String keykategori, keyband;
    TextView nm;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftarlirik);

        nm = (TextView) findViewById(R.id.tampilband);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap Tunggu");
        recyclerband = (RecyclerView) findViewById(R.id.rcyband2);
        recyclerband.setHasFixedSize(true);
        recyclerband.setLayoutManager(new LinearLayoutManager(daftarlirik.this));
        keykategori = getIntent().getExtras().getString("id_audiooo");
        keyband = getIntent().getExtras().getString("id_audio");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference("Band").child(keyband).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Band = dataSnapshot.child("namaBand").getValue().toString();

                nm.setText(Band);

                Query databaseReference = FirebaseDatabase.getInstance().getReference("Audio").orderByChild("Band").startAt(Band).endAt(Band + "\uf8ff");
                FirebaseRecyclerAdapter<user_homelist, daftarlirik.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<user_homelist, daftarlirik.ViewHolder>(
                        user_homelist.class,
                        R.layout.pilihjudul,
                        daftarlirik.ViewHolder.class,
                        databaseReference
                ) {
                    @Override
                    protected void populateViewHolder(daftarlirik.ViewHolder viewHolder, user_homelist model, int position) {
                        final String postkey = getRef(position).getKey();
                        viewHolder.setNamaJudul(model.getJudul());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressDialog.show();
                                Intent in = new Intent(daftarlirik.this, putar_lagu2.class);
                                in.putExtra("id_audiooooo", postkey);
                                startActivity(in);
                                progressDialog.dismiss();
                                finish();
                            }
                        });
                    }
                };
                recyclerband.setAdapter(firebaseRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final Intent intent = new Intent(daftarlirik.this, halhome1.class);
        //intent.putExtra("id_audio",keyband);
        startActivity(intent);
        finish();
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
