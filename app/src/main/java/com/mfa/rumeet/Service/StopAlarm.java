package com.mfa.rumeet.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.mfa.rumeet.Model.AlarmControl;

public class StopAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "alarm is turned off", Toast.LENGTH_SHORT).show();
        AlarmControl.getInstance(context).stopMusic();
    }
}
