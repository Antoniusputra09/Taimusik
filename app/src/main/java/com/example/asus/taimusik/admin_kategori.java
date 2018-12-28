package com.example.asus.taimusik;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class admin_kategori extends AppCompatActivity {
    CardView tb;
    RecyclerView recyle;
    DatabaseReference databaseReference;
    TextView text, text2;
    Typeface fonte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kategori);
        tb = (CardView) findViewById(R.id.card3);
        text2 = (TextView) findViewById(R.id.txt1) ;
        recyle = (RecyclerView) findViewById(R.id.rcykategori);
        recyle.setHasFixedSize(true);
        recyle.setLayoutManager(new LinearLayoutManager(this));
       fonte = Typeface.createFromAsset(this.getAssets(),"fonts/font4.TTF");
       text2.setTypeface(fonte);


        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_kategori.this, tambah_kategori.class));
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView text;
        ImageView img;
        Typeface fonte;
        TextView nama_view;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            text = itemView.findViewById(R.id.delete2);

        }

        public void setNamaKategori(String namaKategori) {
            nama_view = mView.findViewById(R.id.text6);
            //fonte = Typeface.createFromAsset(this.getClass(),"fonts/font1.TIF");

            nama_view.setText(namaKategori);
           // nama_view.setTypeface(fonte);
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference("Kategori");

        FirebaseRecyclerAdapter<kategoriItems, admin_kategori.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<kategoriItems, admin_kategori.ViewHolder>(
                kategoriItems.class,
                R.layout.manage_band,
                admin_kategori.ViewHolder.class,
                databaseReference


        ) {
            @Override
            protected void populateViewHolder(admin_kategori.ViewHolder viewHolder, kategoriItems model, int position) {
                viewHolder.setNamaKategori(model.getNamaKategori());
                final String id = model.getNamaKategori();
                viewHolder.text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete(id);
                    }
                });
                viewHolder.nama_view.setTypeface(fonte);
            }
        };
        recyle.setAdapter(firebaseRecyclerAdapter);
    }

    private void delete(final String id) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot data : dataSnapshot.getChildren()) {
                    String key = data.child("namaKategori").getValue().toString();
                    if (key.equals(id)) {
                        data.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}