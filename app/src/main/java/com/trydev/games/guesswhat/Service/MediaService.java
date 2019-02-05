package com.trydev.games.guesswhat.Service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.trydev.games.guesswhat.R;

import java.io.IOException;

public class MediaService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{

    public static final String ACTION_PACKAGE = "com.trydev.action";
    public static final String ACTION_PLAY = "com.trydev.action.PLAY";
    public static final String ACTION_STOP = "com.trydev.action.STOP";
    public static final String ACTION_CREATE = "com.trydev.action.CREATE";
    public static final String ACTION_PAUSE = "com.trydev.action.PAUSE";

    int currentSeek;

    MediaPlayer mediaPlayer = null;

    int serviceId = 777;
    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void init(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
        AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(R.raw.bgm);

        try {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e){
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        String action = intent.getAction();
        switch (action){
            case ACTION_CREATE:
                init();
                break;
            case ACTION_PLAY:
                if (!mediaPlayer.isPlaying()){
                    mediaPlayer.prepareAsync();
                }
                break;
            case ACTION_STOP:
                mediaPlayer.stop();
                break;
            case ACTION_PAUSE:
                if (mediaPlayer!=null){
                    currentSeek = mediaPlayer.getCurrentPosition();
                    mediaPlayer.stop();
                }
                break;
            default:
                break;
        }
        return flags;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (currentSeek>0){
            mediaPlayer.seekTo(currentSeek);
        }
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer!=null) mediaPlayer.release();
    }
}
