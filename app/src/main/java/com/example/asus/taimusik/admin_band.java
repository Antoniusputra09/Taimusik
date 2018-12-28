package com.example.asus.taimusik;

import android.content.Intent;
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

public class admin_band extends AppCompatActivity {

    CardView tb;
    RecyclerView recyle;
    TextView textv;
    DatabaseReference databaseReference;
    Typeface fonte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_band);
        tb = (CardView) findViewById(R.id.card5);
        textv = (TextView) findViewById(R.id.txt3) ;
        recyle = (RecyclerView) findViewById(R.id.rcyband);


        fonte = Typeface.createFromAsset(this.getAssets(),"fonts/font4.TTF");
        textv.setTypeface(fonte);


        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_band.this, tambah_band.class));
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

        public void setNamaBand(String namaBand) {
            nama_view = mView.findViewById(R.id.text6);
            nama_view.setText(namaBand);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference("Band");

        FirebaseRecyclerAdapter<banditems, admin_band.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<banditems, admin_band.ViewHolder>(banditems.class, R.layout.manage_band, admin_band.ViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(admin_band.ViewHolder viewHolder, banditems model, int position) {
                viewHolder.setNamaBand(model.getNamaBand());
                final String id = model.getNamaBand();
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
                    String key = data.child("namaBand").getValue().toString();
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
