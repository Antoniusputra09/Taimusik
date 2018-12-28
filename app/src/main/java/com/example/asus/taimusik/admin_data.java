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
public class admin_data extends Fragment {

    RecyclerView cy;
    DatabaseReference dbReference;

    public admin_data() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflate.inflate(R.layout.fragment_admin_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cy = (RecyclerView) view.findViewById(R.id.rcydata);
        cy.setHasFixedSize(true);
        cy.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setNama(String nama){
            TextView nama_view = mView.findViewById(R.id.card);
            nama_view.setText(nama);
        }

        public void setFotopp(String fotopp){
            CircleImageView gambar = mView.findViewById(R.id.ci1);
            Glide.with(gambar.getContext())
                    .load(fotopp)
                    .into(gambar);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        dbReference = FirebaseDatabase.getInstance().getReference("Audio");
        FirebaseRecyclerAdapter<user_homelist, admin_data.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter
                <user_homelist, admin_data.ViewHolder>(
                user_homelist.class,
                R.layout.home_list,
                admin_data.ViewHolder.class,
                dbReference
        ) {
            @Override
            protected void populateViewHolder(admin_data.ViewHolder viewHolder, user_homelist model, int position) {

                viewHolder.setNama(model.getJudul());
                viewHolder.setFotopp(model.getImageUrl());

                //       viewHolder.setImgUrl(model.getImgUrl());

            }
        };
        cy.setAdapter(firebaseRecyclerAdapter);

    }
}
