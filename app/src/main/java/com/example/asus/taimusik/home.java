package com.example.asus.taimusik;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {

    DatabaseReference databaseReference, databaseReference2;
    RecyclerView recyle, recycle2;
    EditText gets;
    ImageView serch;
    private ProgressDialog progressDialog;


    public home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference("Audio");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Kategori");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Mengambil Data");

       // recyle = (RecyclerView) view.findViewById(R.id.rcyhome);
        recycle2 = (RecyclerView) view.findViewById(R.id.rcyhome2);
        gets = (EditText) view.findViewById(R.id.yangdicari);
        serch= (ImageView) view.findViewById(R.id.tombolcari);
        //recyle.setHasFixedSize(true);
        //recyle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycle2.setHasFixedSize(true);
        recycle2.setLayoutManager(new GridLayoutManager(getActivity(),3));

        gets.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    switch (i){
                        case  KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String key = gets.getText().toString();
                            if (key.isEmpty()){
                                gets.setError("Cari Kosong");
                            }else{
                                Intent sc = new Intent(getActivity(), cari.class);
                                sc.putExtra("cari",key);
                                startActivity(sc);

                            }
                    }
                }
                return false;
            }
        });

        Cari();
    }

    private void Cari(){
        serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = gets.getText().toString();

                if(key.isEmpty()){
                    gets.setError("Cari Kosong");
                }else {
                    Intent sc = new Intent(getActivity(), cari.class);
                    sc.putExtra("cari",key);
                    startActivity(sc);
                }
            }
        });
    }

   /* public static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ViewHolder(View itemView) {
            super(itemView);

           mView = itemView;


        }

        public void setJudul(String judul) {
            TextView nama_view = mView.findViewById(R.id.card);
            nama_view.setText(judul);
        }

        public void setGambar (String gbr){
            CircleImageView gambar = mView.findViewById(R.id.ci1);
            Glide.with(gambar.getContext())
                    .load(gbr)
                    .into(gambar);
        }


    }*/


    @Override
    public void onStart() {
        super.onStart();

       /* FirebaseRecyclerAdapter<user_homelist, home.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<user_homelist, home.ViewHolder>(
                user_homelist.class,
                R.layout.home_list,
                home.ViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(home.ViewHolder viewHolder, user_homelist model, int position) {
                final String postkey = getRef(position).getKey();
                viewHolder.setJudul(model.getJudul());
                viewHolder.setGambar(model.getImageUrl());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.show();
                        Intent in = new Intent(getActivity(), putar_lagu1.class);
                        in.putExtra("id_audio", postkey);
                        startActivity(in);
                        getActivity().finish();
                        progressDialog.dismiss();
                    }
                });
            }
        };
        recyle.setAdapter(firebaseRecyclerAdapter);*/

        FirebaseRecyclerAdapter<kategoriItems, home.ViewHolder2> firebaseRecyclerAdapter2 = new FirebaseRecyclerAdapter<kategoriItems, home.ViewHolder2>(
                kategoriItems.class,
                R.layout.grid,
                home.ViewHolder2.class,
                databaseReference2
        ) {
            @Override
            protected void populateViewHolder(home.ViewHolder2 viewHolder, kategoriItems model, int position) {
                final String postkey = getRef(position).getKey();
                viewHolder.setNamaKategori(model.getNamaKategori());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(getActivity(),daftarlagu1.class);
                        in.putExtra("id_audiooo", postkey);
                        startActivity(in);
                        getActivity().finish();
                    }
                });
            }
        };
        recycle2.setAdapter(firebaseRecyclerAdapter2);
    }


    public static class ViewHolder2 extends RecyclerView.ViewHolder {

        View mView;

        public ViewHolder2(View itemView) {
            super(itemView);

            mView = itemView;


        }

        public void setNamaKategori(String namaKategori) {
            TextView nama_view = mView.findViewById(R.id.namaKategoriView);
            nama_view.setText(namaKategori);
        }


    }
}
