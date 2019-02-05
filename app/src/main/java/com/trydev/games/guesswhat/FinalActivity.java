package com.trydev.games.guesswhat;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.trydev.games.guesswhat.MainActivity.seek;

public class FinalActivity extends AppCompatActivity implements View.OnClickListener{

    TextView final_result;
    ImageButton menu, home;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        final_result = (TextView) findViewById(R.id.final_result);
        menu = (ImageButton) findViewById(R.id.menu);
        home = (ImageButton) findViewById(R.id.home_button);

        String result = getIntent().getStringExtra("RESULT");

        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setLooping(true);
        start();


        final_result.setText(result);

        menu.setOnClickListener(this);
        home.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu:
                Intent intent;
                intent = new Intent(getApplicationContext(), CategoryActivity.class);
                FinalActivity.this.finish();
                startActivity(intent);
                break;
            case R.id.home_button:
                FinalActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    @Override
    protected void onStop() {
        mediaPlayer.pause();
        seek = mediaPlayer.getCurrentPosition();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        start();
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.pause();
        seek = mediaPlayer.getCurrentPosition();
        super.onBackPressed();
    }

    private void start(){
        seek = 0;
        mediaPlayer.seekTo(seek);
        mediaPlayer.start();
    }
}
