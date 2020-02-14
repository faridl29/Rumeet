package com.mfa.rumeet.Model;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.mfa.rumeet.R;

import java.io.IOException;

public class AlarmControl {
    private static AlarmControl sInstance;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    public AlarmControl(Context context) {
        mContext = context;
    }

    public static AlarmControl getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AlarmControl(context);
        }
        return sInstance;
    }

    public void playMusic() {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(mContext,  Uri.parse("android.resource://"+mContext.getPackageName()+"/"+R.raw.alarm));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            mMediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());
        } else {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        }
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        mMediaPlayer.setLooping(true);
    }

    public void stopMusic() {
        if(mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.seekTo(0);
        }
    }

    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
}
