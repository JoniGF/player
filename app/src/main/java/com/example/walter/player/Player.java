package com.example.walter.player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener{
    int position;
    SeekBar seekBar;
    Button play, sig, ant, ffsig, ffant;
    ArrayList<File> mySong;
   MediaPlayer mp;
    Uri u;
    Thread tr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        play=(Button)findViewById(R.id.play);
        sig=(Button)findViewById(R.id.sig);
        ant=(Button)findViewById(R.id.ant);
        ffsig=(Button)findViewById(R.id.ffsig);
        ffant=(Button)findViewById(R.id.ffant);
        seekBar=(SeekBar)findViewById(R.id.seekBar);

        play.setOnClickListener(this);
        sig.setOnClickListener(this);
        ant.setOnClickListener(this);
        ffsig.setOnClickListener(this);
        ffant.setOnClickListener(this);

        tr = new Thread(){
            @Override
            public void run() {
                int total = mp.getDuration();
                int acposition=0;
                while (acposition<total){
                    try {
                        sleep(500);
                        acposition=mp.getCurrentPosition();
                        seekBar.setProgress(acposition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //super.run();
            }
        };

        if (mp != null){
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySong =(ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos",0);

        u = Uri.parse(mySong.get(position).toString());
        mp=MediaPlayer.create(this,u);
        mp.start();
        seekBar.setMax(mp.getDuration());
        tr.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.play:
                if (mp.isPlaying()){
                    mp.pause();
                    play.setText(">");
                }else if (mp != null && !mp.isPlaying()){
                    mp.start();
                    play.setText("||");
                }
                break;
            case R.id.ffsig:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.ffant:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.sig:
                mp.stop();
                mp.release();
                position=(position+1)%mySong.size();
                u = Uri.parse(mySong.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                seekBar.setMax(mp.getDuration());
                break;
            case R.id.ant:
                mp.stop();
                mp.release();
                position=(position-1<0)?mySong.size()-1:position-1;
                u = Uri.parse(mySong.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                seekBar.setMax(mp.getDuration());
                break;

        }

    }
}
