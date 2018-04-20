package com.example.frescogif.activity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.frescogif.R;
import com.example.frescogif.baseActvity.BaseActivity;

import java.io.File;
import java.io.IOException;

/**
 * Created by GG on 2017/6/27.
 */
public class PullScaleActivity extends BaseActivity implements SoundPool.OnLoadCompleteListener {

    private AssetFileDescriptor receive;
    private AssetFileDescriptor complete;
    private MediaPlayer mp;
    private AssetManager am;
    private AudioManager mAudioManager;
    private SoundPool sndPool;
    private Vibrator vibrator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_scale);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
//        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,max,AudioManager.FLAG_VIBRATE);

        sndPool = new SoundPool(16, AudioManager.STREAM_MUSIC,0 );
        sndPool.setOnLoadCompleteListener(this);

        vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
        am = getAssets();
        mp = new MediaPlayer();

    }

    public void playOne(View view) throws IOException {

        try {
            if(!mp.isPlaying()){
            receive = am.openFd("receive.mp3");
            mp.setDataSource(receive.getFileDescriptor(),receive.getStartOffset(), receive.getLength());
            mp.setLooping(true);//循环播放
            mp.prepare();
            mp.start();
            long[] patter = {1000, 1000, 2000, 50};
            vibrator.vibrate(patter,0);
            } else {
                vibrator.cancel();
                mp.stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playTwo(View view) throws IOException {

//         sndPool.load( this , R.raw.complete , 1 ) ;
        try {
            if(!mp.isPlaying()) {
                complete = am.openFd("complete.mp3");
                mp.setDataSource(complete.getFileDescriptor(), complete.getStartOffset(), complete.getLength());
                mp.setLooping(true);//循环播放
                mp.prepare();
                mp.start();
            }else {
                mp.stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        sndPool.play( sampleId, (float)0.8,(float)0.8, 16, 10, (float)1.0) ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp.isPlaying()){
            mp.stop();
        }
        vibrator.cancel();
        mp.release();
    }
}
