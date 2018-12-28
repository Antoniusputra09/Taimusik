package com.example.asus.taimusik;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
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

public class putar_lagu2 extends AppCompatActivity {

    private TextView tvjudul;
    private TextView tvlirik;
    private String urldatabase;
    private  String urlaudio;
    private ImageView play;
    private ImageView paused;
    private ImageView kembali;
    private ImageView next;
    private ImageView previous, download;
//    MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Runnable runnable;
    private Handler handler;
    private boolean pp = true ;
    private ProgressDialog progressDialog;
    final MediaPlayer mediaPlayer = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_putar_lagu2);
        tvjudul = (TextView) findViewById(R.id.jodol2);
        tvlirik = (TextView) findViewById(R.id.lirik2);
       // play = (ImageView) findViewById(R.id.tombolplay2);
        paused = (ImageView) findViewById(R.id.tombolpaused2);
        seekBar = (SeekBar) findViewById(R.id.seekBar2);
        kembali = (ImageView) findViewById(R.id.kembali);
        next = (ImageView) findViewById(R.id.tombolnext2);
        previous=(ImageView) findViewById(R.id.tombolprevious2);
        download = (ImageView) findViewById(R.id.tomboldownload2);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap Tunggu");
        handler = new Handler();
        urldatabase = getIntent().getExtras().getString("id_audiooooo");
        //play.setImageResource(R.drawable.pause_icon);

       if (mediaPlayer.isPlaying()) {
           mediaPlayer.pause();
       }


        pindah();

       mauputar();
       // putar();
        unduh();
    }

    private void pindah() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Audio").child(urldatabase);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Jodol = dataSnapshot.child("judul").getValue().toString();
                String lirik = dataSnapshot.child("lirik").getValue().toString();
                String url = dataSnapshot.child("imageUrl").getValue().toString();
                urlaudio = dataSnapshot.child("audioUrl").getValue().toString();

                tvjudul.setText(Jodol);
                tvlirik.setText(lirik);

                CircleImageView gambar = findViewById(R.id.gambarartis);
                Glide.with(gambar.getContext())
                       .load(url)
                        .into(gambar);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void unduh(){
        download.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressDialog.show();
                StorageReference st = FirebaseStorage.getInstance().getReferenceFromUrl(urlaudio);
                File rootPath = new File(Environment.getExternalStorageDirectory(), "I Musik Downloader");
                if(!rootPath.exists()){
                    rootPath.mkdirs();
                }
                final File locaFile = new File(rootPath, tvjudul.getText().toString()+ ".mp3");
                st.getFile(locaFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(putar_lagu2.this, "Audio Sudah di Download :"+ locaFile.toString(), Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(putar_lagu2.this,"gagal download",Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void putar(){

        //final MediaPlayer mediaPlayer = new MediaPlayer();
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Audio").child(urldatabase);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        try{
                            String audioUrl = dataSnapshot.child("audioUrl").getValue().toString();
                            mediaPlayer.setDataSource(audioUrl);
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    seekBar.setMax(mediaPlayer.getDuration());

                                    // mediaPlayer.stop();
                                    //  progressDialog.show();
                                    progressDialog.dismiss();
                                    mediaPlayer.start();

                                    changeSeekbar();
                                }


                            });



                            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                    if (b){
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

                        }catch (IOException e){
                            e.printStackTrace();
                        }


                    }

                    private void changeSeekbar() {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());

                        if (mediaPlayer.isPlaying()){
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
                    final Intent intent = new Intent(putar_lagu2.this, halhome1.class);
                    //intent.putExtra("id_audio",keyband);
                    startActivity(intent);
                    finish();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
            }
        });

        paused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                switch (view.getId()){
                    case R.id.tombolpaused2:
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.pause();

                            paused.setImageResource(R.drawable.play_icon);
                            progressDialog.dismiss();
                        }else{
                            mediaPlayer.start();
                            paused.setImageResource(R.drawable.pause_icon);
                            changeSeekbar();
                            progressDialog.dismiss();
                        }
                        break;


                    case R.id.tombolnext2:
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
                        break;
                    case R.id.tombolprevious2:
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
                        break;

                }
            }



            private void changeSeekbar() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                if (mediaPlayer.isPlaying()){
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
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();

            final Intent intent = new Intent(putar_lagu2.this, halhome1.class);
            //intent.putExtra("id_audio",keyband);
            startActivity(intent);
            finish();


      }else{
            final Intent intent = new Intent(putar_lagu2.this, halhome1.class);
            //intent.putExtra("id_audio",keyband);
            startActivity(intent);
            finish();
        }


         /* Intent a = new Intent(Intent.ACTION_MAIN);
          a.addCategory(Intent.CATEGORY_HOME);
          a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(a);
          finish();*/


    }

}
