package com.mfa.rumeet.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mfa.rumeet.Model.SharedPrefManager;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.Rest.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tanggal, dari_jam, sampai_jam;
    EditText keterangan;
    Button btnBooking;
    DatePickerDialog date;
    TimePickerDialog mTimePicker;
    Calendar calendar;
    SimpleDateFormat dateFormatter;
    SharedPrefManager sharedPrefManager;
    ProgressDialog loading;
    Context mContext;

    String stanggal = "";
    String sdari_jam = "";
    String ssampai_jam = "";

    private ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tanggal = (TextView) findViewById(R.id.tanggal);
        dari_jam = (TextView) findViewById(R.id.dari_jam);
        sampai_jam = (TextView) findViewById(R.id.sampai_jam);
        keterangan = (EditText) findViewById(R.id.etKeterangan);
        btnBooking = (Button) findViewById(R.id.btnBook);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("nama_ruangan"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        calendar = Calendar.getInstance();

        sharedPrefManager = new SharedPrefManager(this);


        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampil_tanggal();
            }
        });

        dari_jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dari_jam();
            }
        });

        sampai_jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sampai_jam();
            }
        });

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cek_jam();
            }
        });


        /** end after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_WEEK, +7);

/** start before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_WEEK, +1);

        final HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                SimpleDateFormat dateFormatter2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                calendar = Calendar.getInstance();
                tanggal.setText("Tanggal ("+dateFormatter2.format(horizontalCalendar.getSelectedDate())+")");
                stanggal = dateFormatter.format(horizontalCalendar.getSelectedDate());
            }
        });
    }

    private void tampil_tanggal(){
        date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                tanggal.setText(dateFormatter.format(calendar.getTime()));
                stanggal = String.valueOf(dateFormatter.format(calendar.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        long now = System.currentTimeMillis() + (1000*60*60*24);
        date.getDatePicker().setMinDate(now);
        date.getDatePicker().setMaxDate(now+(1000*60*60*24*6)); //After 7 Days from Now
        date.show();
    }

    private void dari_jam(){
        Calendar mCurrentTime = Calendar.getInstance();
        int jam = mCurrentTime.get(Calendar.HOUR);
        int menit = mCurrentTime.get(Calendar.MINUTE);

        mTimePicker = new TimePickerDialog(BookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dari_jam.setText(String.format("%02d:%02d", hourOfDay, minute));
                sdari_jam = (String.format("%02d:%02d", hourOfDay, minute));
            }
        },jam, menit, true);
        mTimePicker.setTitle("Pilih jam mulai meeting");
        mTimePicker.show();

    }

    private void sampai_jam(){
        Calendar mCurrentTime = Calendar.getInstance();
        int jam = mCurrentTime.get(Calendar.HOUR);
        int menit = mCurrentTime.get(Calendar.MINUTE);

        mTimePicker = new TimePickerDialog(BookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                sampai_jam.setText(String.format("%02d:%02d", hourOfDay, minute));
                ssampai_jam = (String.format("%02d:%02d", hourOfDay, minute));
            }
        },jam, menit, true);
        mTimePicker.setTitle("Pilih jam mulai meeting");
        mTimePicker.show();

    }

    private void cek_jam(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        if(stanggal != "" && sdari_jam != "" && ssampai_jam != ""){
            try {
                Date one = dateFormat.parse(sdari_jam);
                Date two = dateFormat.parse (ssampai_jam);

                if(one.compareTo(two) >= 0){
                    Toast.makeText(getApplicationContext(), "cek kembali jam booking!", Toast.LENGTH_LONG).show();
                }else{
                    kirim_data();
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {
            Toast.makeText(mContext, "Data belum lengkap!" , Toast.LENGTH_SHORT).show();
        }


    }

    private void kirim_data(){

        new SweetAlertDialog(BookingActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Ruangan akan di booking!")
                .setConfirmText("Booking!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {

                        //loading dialog
                        final SweetAlertDialog pDialog = new SweetAlertDialog(BookingActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Melakukan booking ...");
                        pDialog.setCancelable(true);
                        pDialog.show();

                        Call<ResponseBody> postData = mApiInterface.bookingRequest(
                                sharedPrefManager.getSpUniqueId(),
                                stanggal,
                                sdari_jam,
                                ssampai_jam,
                                keterangan.getText().toString(),
                                getIntent().getStringExtra("id"));

                        postData.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){
                                    Log.i("debug", "onResponse: BERHASIL");
                                    try {
                                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                        if (jsonRESULTS.getString("error").equals("false")){
                                            startActivity(new Intent(mContext, MainActivity.class)
                                                    .putExtra("cek", "berhasil")
                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                            finish();
                                            pDialog.dismissWithAnimation();
                                            sDialog.dismissWithAnimation();

                                        } else {
                                            pDialog.dismissWithAnimation();
                                            sDialog.dismissWithAnimation();
                                            String error_message = jsonRESULTS.getString("error_msg");
                                            new SweetAlertDialog(BookingActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Oops...")
                                                    .setContentText(error_message)
                                                    .show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    pDialog.dismissWithAnimation();
                                    sDialog.dismissWithAnimation();
                                    Log.i("debug", "onResponse: GA BERHASIL");
                                    loading.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                pDialog.dismissWithAnimation();
                                sDialog.dismissWithAnimation();
                                Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                                new SweetAlertDialog(BookingActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Koneksi internet bermasalah!")
                                        .show();
                            }
                        });
                    }
                })
                .setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
