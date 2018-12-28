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

public class daftarlagu1 extends AppCompatActivity {

    private RecyclerView recyclerband;
    String keykategori;
    TextView namakategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftarlagu1);
        namakategori = (TextView) findViewById(R.id.namaKategori);
        recyclerband = (RecyclerView) findViewById(R.id.rcyband1);
        recyclerband.setHasFixedSize(true);
        recyclerband.setLayoutManager(new LinearLayoutManager(daftarlagu1.this));
        keykategori = getIntent().getExtras().getString("id_audiooo");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference("Kategori").child(keykategori).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Kategori = dataSnapshot.child("namaKategori").getValue().toString();

                namakategori.setText(Kategori);

                Query databaseReference = FirebaseDatabase.getInstance().getReference("Band").orderByChild("namaKategori").startAt(Kategori).endAt(Kategori + "\uf8ff");
                FirebaseRecyclerAdapter<banditems, daftarlagu1.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<banditems, daftarlagu1.ViewHolder>(
                        banditems.class,
                        R.layout.pilihband,
                        daftarlagu1.ViewHolder.class,
                        databaseReference
                ) {
                    @Override
                    protected void populateViewHolder(daftarlagu1.ViewHolder viewHolder, banditems model, int position) {
                        final String postkey = getRef(position).getKey();
                        viewHolder.setNamaBand(model.getNamaBand());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent in = new Intent(daftarlagu1.this, daftarlirik.class);
                                in.putExtra("id_audio", postkey);
                                in.putExtra("id_audioo",keykategori);
                                startActivity(in);
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
        final Intent intent = new Intent(daftarlagu1.this, halhome1.class);
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

        public void setNamaBand(String namaKategori) {
            TextView nama_view = mView.findViewById(R.id.banditem);
            nama_view.setText(namaKategori);
        }
    }
}
