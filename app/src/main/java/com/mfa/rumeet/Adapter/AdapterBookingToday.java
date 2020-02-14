package com.mfa.rumeet.Adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mfa.rumeet.Model.Booked;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Service.AlarmReceiver;
import com.mfa.rumeet.Service.StopAlarm;
import com.mfa.rumeet.Fragment.FragmentRuangan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterBookingToday extends RecyclerView.Adapter<AdapterBookingToday.MyViewHolder> {

    List<Booked> mBookedList;
    FragmentRuangan con;

    RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams((int)RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);

    RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams((int)(600),
            RelativeLayout.LayoutParams.WRAP_CONTENT);

    public AdapterBookingToday(List<Booked> mBookedList, FragmentRuangan con) {
        this.mBookedList = mBookedList;
        this.con = con;
    }

    @Override
    public AdapterBookingToday.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_booking_today, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(mView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterBookingToday.MyViewHolder holder,final int i) {
        holder.tvNo.setText(String.valueOf(i+1));
        holder.tvJam.setText(mBookedList.get(i).getDari_jam());
        holder.tvRuangan.setText(mBookedList.get(i).getNama_ruangan());
        if(mBookedList.size() > 1){
            holder.layout.setLayoutParams(lp2);
        }else{
            holder.layout.setLayoutParams(lp1);
        }
        holder.counter(Integer.valueOf(mBookedList.get(i).getTimer()) * 1000, holder.aktif);

        holder.mySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.mySwitch.isChecked()){

                    holder.editor.putString(mBookedList.get(i).getId(), mBookedList.get(i).getId());
                    holder.editor.putBoolean("isSwitched"+mBookedList.get(i).getId(), true);
                    holder.editor.commit();

                    holder.aktif = true;
                    holder.mySwitch.setText("Matikan alarm");
                    try {
                        holder.cal.setTime(holder.sdf.parse(mBookedList.get(i).getTanggal()+" "+mBookedList.get(i).getDari_jam()));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.i("AdapterBookingToday", "time = " + holder.cal.getTime());
                    holder.setAlarm(holder.cal, holder.aktif, Integer.valueOf(mBookedList.get(i).getId()));

                    holder.mySwitch.setChecked(true);

                }else{

                    holder.editor.putString(mBookedList.get(i).getId(), mBookedList.get(i).getId());
                    holder.editor.putBoolean("isSwitched"+mBookedList.get(i).getId(), false);
                    holder.editor.commit();

                    holder.aktif = false;
                    holder.mySwitch.setText("Aktifkan alarm");
                    holder.setAlarm(holder.cal, holder.aktif, Integer.valueOf(mBookedList.get(i).getId()));

                    holder.mySwitch.setChecked(false);
                }
            }
        });

        Boolean cekSwitch = holder.sharedPreferences.getBoolean("isSwitched"+mBookedList.get(i).getId(),false);

        if(cekSwitch == false){
            holder.mySwitch.setChecked(false);
            holder.mySwitch.setText("Aktifkan alarm");
        }else{
            holder.mySwitch.setChecked(true);
            holder.mySwitch.setText("Matikan alarm");
        }

    }

    @Override
    public int getItemCount() {
        return mBookedList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        String id;
        Boolean isSwitched;

        private TextView tvNo, tvJam, tvRuangan, tvTimer;
        private Switch mySwitch, stopSwitch;
        private Boolean aktif = false;
        private RelativeLayout layout;
        SimpleDateFormat sdf;
        Calendar cal;

        private MyViewHolder(View itemView) {
            super(itemView);

            sharedPreferences = itemView.getContext().getSharedPreferences("cekSwitch", 0);
            editor = sharedPreferences.edit();

            tvNo = itemView.findViewById(R.id.nomor);
            tvJam = itemView.findViewById(R.id.jam);
            tvRuangan = itemView.findViewById(R.id.ruangan);
            layout = itemView.findViewById(R.id.layout);
            tvTimer = itemView.findViewById(R.id.timer);
            mySwitch = itemView.findViewById(R.id.mySwitch);
            stopSwitch = itemView.findViewById(R.id.stopAlarm);

            sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            cal = Calendar.getInstance();

        }

        private void counter(long min, boolean aktif) {
            final CountDownTimer timer = new CountDownTimer(min, 1000) {
                public void onTick(long millisUntilFinished) {
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                    tvTimer.setText(String.format("%d:%d:%d", hours, minutes, seconds));
                }
                public void onFinish() {
//                    mySwitch.setVisibility(View.GONE);
                    tvTimer.setText("Sedang berlangsung");
                    tvTimer.setTextSize(15);
                }
            };
            timer.start();
        }

        private void setAlarm(Calendar targetCal, Boolean aktif, int id) {

            Intent intent = new Intent(itemView.getContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    itemView.getContext(), id , intent,  PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) con.getContext().getSystemService(Context.ALARM_SERVICE);

            if(aktif){
                Toast.makeText(itemView.getContext(), "alarm set on "+ targetCal.getTime(), Toast.LENGTH_SHORT).show();

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                        pendingIntent);

            }else{
                alarmManager.cancel(pendingIntent);
                Intent intent2 = new Intent(itemView.getContext(), StopAlarm.class);
                itemView.getContext().sendBroadcast(intent2);
            }
        }
    }

}
