package com.example.asus.taimusik;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class akun extends Fragment {
    TextView tf;
    TextView out;
    TextView kota;
    TextView tanggal;
    TextView nama1;
    TextView nama2;
    ImageView foto;
    ImageView foto2;
    StorageReference mStorageRef;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;


    public akun() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akun, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tf = (TextView) view.findViewById(R.id.ganti_pass);
        out = (TextView) view.findViewById(R.id.keluar);
        kota =(TextView) view.findViewById(R.id.kota_tampil);
        tanggal = (TextView) view.findViewById(R.id.tanggal_tampil);
        nama1=(TextView)view.findViewById(R.id.namaasli_tampil);
        nama2 = (TextView) view.findViewById(R.id.namapenguna_tampil);
        foto = (ImageView) view.findViewById(R.id.pp1);
        foto2=(ImageView) view.findViewById(R.id.gambarpic);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        mStorageRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        auth = FirebaseAuth.getInstance();


       StorageReference storageReference = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("images/profile.jpg");
       storageReference.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
           @Override
           public void onSuccess(byte[] bytes) {
               Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
               DisplayMetrics displayMetrics = new DisplayMetrics();



               foto.setImageBitmap(bitmap);


               foto2.setImageBitmap(bitmap);
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

           }
       });

        final TextView textView2 = nama1;
        final TextView textView3 = nama2;
        final TextView textView = kota;
        final TextView textView1 = tanggal;
        databaseReference.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String namaas = (String) dataSnapshot.child("nama_asli").getValue(String.class);
                String namapg = (String) dataSnapshot.child("nama_user").getValue(String.class);
                String Kota = (String) dataSnapshot.child("kota").getValue(String.class);
                String Tanggal = (String) dataSnapshot.child("tanggal").getValue(String.class);

                textView2.setText(namaas);
                textView3.setText(namapg);
                textView.setText(Kota);
                textView1.setText(Tanggal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        pindah();
        logout();


    }
    private void pindah(){
        tf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),edit_profile.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void logout() {
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutlagi();
            }
        });

    }

    private void logoutlagi(){
        final FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signOut();
        Intent intent = new Intent(getActivity(),halmasuk.class);
        startActivity(intent);
    }



}
