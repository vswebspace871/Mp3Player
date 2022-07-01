package com.example.mp3player;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicActivity extends AppCompatActivity {

    private TextView textViewFileNameMusic,textViewProgress, textViewTotalTime;
    private SeekBar music_seek_bar;

    private ImageView previous, next, pausePlay, musicIcon;
    ArrayList<AudioModel> songsList;
    AudioModel currentSong;

    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x = 0;

    /*private


    String title, filePath;
    int position;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        getSupportActionBar().hide();

        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        pausePlay = findViewById(R.id.pause);
        musicIcon = findViewById(R.id.musicIcon);

        textViewFileNameMusic = findViewById(R.id.textViewFileNameMusic);
        textViewProgress = findViewById(R.id.textViewProgress);
        textViewTotalTime = findViewById(R.id.textViewTotalTime);

        music_seek_bar = findViewById(R.id.music_seek_bar);
        textViewFileNameMusic.setSelected(true);

        songsList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("List");

        setResourcesWithMusic();

        MusicActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    music_seek_bar.setProgress(mediaPlayer.getCurrentPosition());
                    textViewProgress.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));

                    if (mediaPlayer.isPlaying()){
                        pausePlay.setImageResource(R.drawable.ic_baseline_pause_24);
                        musicIcon.setRotation(x++);
                    }else {
                        pausePlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                        musicIcon.setRotation(0);
                    }
                }
                new Handler().postDelayed(this, 100);
            }
        });
        // jab ek song poora ho jaayega to dusra song apne aap chalne lagega, is function se
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(MusicActivity.this, "Changing Song", Toast.LENGTH_SHORT).show();
                playNextSong();
            }
        });



        //if user change seekbar backward and forward
        music_seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*




        title = getIntent().getStringExtra("title");
        filePath = getIntent().getStringExtra("filepath");
        position = getIntent().getIntExtra("position", 0);
        list = getIntent().getStringArrayListExtra("list");*/
    }

    void setResourcesWithMusic(){
        currentSong = songsList.get(MyMediaPlayer.currentIndex);
        textViewFileNameMusic.setText(currentSong.getTitle()); // song name print

        textViewTotalTime.setText(convertToMMSS(currentSong.getDuration())); // time shld be in string format

        //click listener on Player buttons/controls
        pausePlay.setOnClickListener(view -> pausePlay());
        next.setOnClickListener(view -> playNextSong());
        previous.setOnClickListener(view -> playPreviousSong());

        playMusic();

    }

    private void playMusic(){

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();

            //seekbar
            music_seek_bar.setProgress(0);
            music_seek_bar.setMax(mediaPlayer.getDuration());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void playNextSong(){
        if (MyMediaPlayer.currentIndex == songsList.size()-1)
            return;
        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }
    private void playPreviousSong(){

        if (MyMediaPlayer.currentIndex == 0)
            return;
        MyMediaPlayer.currentIndex -= 1;
        mediaPlayer.reset();
        setResourcesWithMusic();



    }
     private void pausePlay(){
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
            else
                mediaPlayer.start();
    }

    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
                );
    }
}