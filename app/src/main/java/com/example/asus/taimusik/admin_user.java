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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class admin_user extends Fragment {
RecyclerView cy;
DatabaseReference databaseReference;

    public admin_user() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cy = (RecyclerView) view.findViewById(R.id.rcy1);
        cy.setHasFixedSize(true);
        cy.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setNama_user(String nama_user){
            TextView nama_view = mView.findViewById(R.id.tv2);
            nama_view.setText(nama_user);
        }
        public void setTanggal(String tanggal){
            TextView tanggal_view = mView.findViewById(R.id.tv3);
            tanggal_view.setText(tanggal);
        }

        public void setNama_asli (String nama_asli){
            TextView nama_asli_view = mView.findViewById(R.id.tv1);
            nama_asli_view.setText(nama_asli);
        }

        public void setKota (String kota){
            TextView kota_view = mView.findViewById(R.id.tv5);
            kota_view.setText(kota);
        }
        public void setFotopp(String fotopp){
           CircleImageView gambar = mView.findViewById(R.id.gbr1);
            Glide.with(gambar.getContext())
                    .load(fotopp)
                    .into(gambar);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        FirebaseRecyclerAdapter<UserIItems, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter
                <UserIItems, ViewHolder>(
                UserIItems.class,
                R.layout.manage_user,
                ViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, UserIItems model, int position) {
                viewHolder.setFotopp(model.getFotopp());
                viewHolder.setNama_user(model.getNama_user());
                viewHolder.setTanggal(model.getTanggal());
                viewHolder.setKota(model.getKota());
                viewHolder.setNama_asli(model.getNama_asli());

         //       viewHolder.setImgUrl(model.getImgUrl());

            }
        };
        cy.setAdapter(firebaseRecyclerAdapter);

    }
}
