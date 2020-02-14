package com.mfa.rumeet.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.mfa.rumeet.Model.AlarmControl;
import com.mfa.rumeet.R;
import com.mfa.rumeet.activity.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    MediaPlayer player = null;

    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmControl.getInstance(context).playMusic();

        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo_splash)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText("Sekarang saatnya meeting");

        //clicked notification
        Intent notificationIntent = new Intent(context,MainActivity.class);
        PendingIntent conPendingIntent = PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(conPendingIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(0, mBuilder.build());


    }


}