package com.example.asus.taimusik;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class putar_lagu1 extends AppCompatActivity {

    private TextView tvjudul;
    private TextView tvlirik;
    private String urldatabase;
    private ImageView kembali;
    private ImageView paused, cepat, lambat, unduhan;
    MediaPlayer mp = null;
    private SeekBar seekBar;
    private Runnable runnable;
    private Handler handler;
    private boolean pp = true;
    final MediaPlayer mediaPlayer = new MediaPlayer();
    private ProgressDialog progressDialog;
    private String audioUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_putar_lagu1);
        tvjudul = (TextView) findViewById(R.id.jodol);
        tvlirik = (TextView) findViewById(R.id.lirik);
        //  play = (ImageView) findViewById(R.id.tombolplay);
        paused = (ImageView) findViewById(R.id.tombolpaused);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        kembali = (ImageView) findViewById(R.id.kembali2);
        cepat = (ImageView) findViewById(R.id.tombolnext);
        lambat = (ImageView) findViewById(R.id.tombolprevious);
        //unduhan= (ImageView) findViewById(R.id.tombol_download);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap Tunggu");
        handler = new Handler();
        urldatabase = getIntent().getExtras().getString("id_audio");
        //play.setImageResource(R.drawable.pause_icon);
        pindah();
        mauputar();




    }

    private void pindah() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Audio").child(urldatabase);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Jodol = dataSnapshot.child("judul").getValue().toString();
                String lirik = dataSnapshot.child("lirik").getValue().toString();
                String url = dataSnapshot.child("imageUrl").getValue().toString();

                tvjudul.setText(Jodol);
                tvlirik.setText(lirik);

                CircleImageView gambar = findViewById(R.id.gambarartis2);
                Glide.with(gambar.getContext())
                        .load(url)
                        .into(gambar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void mauputar(){
        paused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    progressDialog.show();
                    paused.setImageResource(R.drawable.pause_icon);
                    putar();
                    progressDialog.dismiss();

            }
        });
    }

    private void putar() {
        //progressDialog.show();

        //final MediaPlayer mediaPlayer = new MediaPlayer();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Audio").child(urldatabase);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    audioUrl = dataSnapshot.child("audioUrl").getValue().toString();
                    mediaPlayer.setDataSource(audioUrl);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            seekBar.setMax(mediaPlayer.getDuration());
                            mediaPlayer.start();
                            progressDialog.dismiss();
                            changeSeekbar();
                        }


                    });

                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            if (b) {
                                mediaPlayer.seekTo(i);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    mediaPlayer.prepare();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            private void changeSeekbar() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                if (mediaPlayer.isPlaying()) {
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            changeSeekbar();
                        }
                    };

                    handler.postDelayed(runnable, 1000);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    Toast.makeText(getApplicationContext(),"Matikan Lagu",Toast.LENGTH_SHORT).show();
                } else {
                    final Intent intent = new Intent(putar_lagu1.this, halhome1.class);
                    //intent.putExtra("id_audio",keyband);
                    startActivity(intent);
                    finish();
                }
            }
        });

        cepat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
            }
        });

        lambat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
            }
        });

      /*  unduhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                StorageReference sr = FirebaseStorage.getInstance().getReferenceFromUrl(audioUrl);
                try {
                    File anyar = File.createTempFile("audio","mp3");
                    sr.getFile(anyar).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Download Sukses",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });*/

        paused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tombolpaused:
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            paused.setImageResource(R.drawable.play_icon);
                        } else {
                            mediaPlayer.start();
                            paused.setImageResource(R.drawable.pause_icon);
                            changeSeekbar();
                        }
                        break;

                    case R.id.tombolnext:
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
                        break;
                    case R.id.tombolprevious:
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
                        break;
                }
            }

            private void changeSeekbar() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                if (mediaPlayer.isPlaying()) {
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            changeSeekbar();
                        }
                    };

                    handler.postDelayed(runnable, 1000);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //MediaPlayer mediaPlayer = new MediaPlayer();
       if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();

            final Intent intent = new Intent(putar_lagu1.this, halhome1.class);
            //intent.putExtra("id_audio",keyband);
            startActivity(intent);
            finish();


        }else
       {
           final Intent intent = new Intent(putar_lagu1.this, halhome1.class);
           //intent.putExtra("id_audio",keyband);
           startActivity(intent);
           finish();
       }


    }
}