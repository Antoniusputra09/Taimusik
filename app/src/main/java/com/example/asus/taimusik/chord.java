package com.example.asus.taimusik;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class chord extends Fragment {

    RecyclerView cy;
    DatabaseReference databaseReference;



    public chord() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chord, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cy = (RecyclerView) view.findViewById(R.id.rcychord);
        cy.setHasFixedSize(true);
        cy.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false) );

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setNamaChord(String namaChord){
            TextView nama_view = mView.findViewById(R.id.namachord);
            nama_view.setText(namaChord);
        }

        public void setGambarChord(String gambarChord){
            CircleImageView gambar = mView.findViewById(R.id.gambarchord);
            Glide.with(gambar.getContext())
                    .load(gambarChord)
                    .into(gambar);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chord");
        FirebaseRecyclerAdapter<listchord, chord.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter
                <listchord, chord.ViewHolder>(
                listchord.class,
                R.layout.isichord,
                chord.ViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(chord.ViewHolder viewHolder, listchord model, int position) {
                viewHolder.setNamaChord(model.getNamaChord());
                viewHolder.setGambarChord(model.getGambarUrl());

                //       viewHolder.setImgUrl(model.getImgUrl());

            }
        };
        cy.setAdapter(firebaseRecyclerAdapter);

    }

}
