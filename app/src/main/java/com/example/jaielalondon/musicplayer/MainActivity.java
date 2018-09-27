package com.example.jaielalondon.musicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Button play, pause;
    private TextView fastForward, rewind;
    private SeekBar seekBar;

    private double startTime = 0;
    private double finalTime = 0;

    public static int oneTimeOnly = 0;

    private Handler myHandler = new Handler();
    private int forwardTime = 15000;
    private  int backwardTime = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creates mediaPlayer and links it to the mozart song
        mediaPlayer = MediaPlayer.create(this, R.raw.mozart);

        //find buttons by the ID's play and pause
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);

        //find TextView's by Id's fast_forward and rewind
        fastForward = findViewById(R.id.fast_forward);
        rewind = findViewById(R.id.rewind);

        //find seek bar by id
        //set it un-clickable
        seekBar = findViewById(R.id.seekBar);
        seekBar.setClickable(true);

        /**
         *  Starts playing song when play button is clicked
         *  then disables the play button
         */
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.start();
                play.setEnabled(false);

                startTime = mediaPlayer.getCurrentPosition();
                finalTime = mediaPlayer.getDuration();

                if (oneTimeOnly == 0) {
                    seekBar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }

                seekBar.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);
            }
        });

        /**
         * Pauses music when pause button is clicked
         * then enables play button
         */
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.pause();
                play.setEnabled(true);
            }
        });

        /**
         * When the fastForward button is clicked
         * Music goes forwards 15 seconds
         */
        fastForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(), "You have Jumped forward 15 " +
                            "seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot jump forward 15 " +
                            "seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /**
         * When the rewind button is clicked
         * Music goes back 15 seconds
         */
        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(), "You have Jumped backward 15 " +
                            "seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 15 " +
                            "seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(MainActivity.this,"I'm Done!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            seekBar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

}
