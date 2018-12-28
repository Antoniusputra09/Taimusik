package com.example.asus.taimusik;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Admin_upload2 extends Fragment {

    TextView tf;
    TextView tv;
    TextView tw;
    TextView tz;



    public Admin_upload2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_upload2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tf = (TextView) view.findViewById(R.id.pil2);
        tv = (TextView) view.findViewById(R.id.pil3);
        tw = (TextView) view.findViewById(R.id.pil4);
        tz = (TextView) view.findViewById(R.id.pil5);
        musik();
        cord();
        kategori();
        band();
    }

    private void musik(){
        tf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),admin_upload_lagi.class);
                getActivity().startActivity(intent);

            }
        });
    }

    private void cord(){
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),admin_upload_chord.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void kategori(){
        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),admin_kategori.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void band (){
        tz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),admin_band.class);
                getActivity().startActivity(intent);
            }
        });
    }

}
