package com.mfa.rumeet.activity;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mfa.rumeet.Adapter.AdapterNotification;
import com.mfa.rumeet.Adapter.RecyclerViewAdapter;
import com.mfa.rumeet.Model.GetNotifikasi;
import com.mfa.rumeet.Model.Notifikasi;
import com.mfa.rumeet.Model.SharedPrefManager;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.Rest.ApiInterface;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    ApiInterface mApiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public static AdapterNotification adapter;
    public static NotificationActivity ma;
    Toolbar toolbar;
    TextView toolbar_title;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        sharedPrefManager = new SharedPrefManager(this);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        ma = this;

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String cek = getIntent().getStringExtra("cek");
        if(cek != null){
            new SweetAlertDialog(NotificationActivity.this)
                    .setTitleText("Notifikasi berhasil dihapus!")
                    .show();
        }

        refresh();
    }

    public void refresh(){

        Call<GetNotifikasi> getNotifikasi = mApiInterface.getNotifikasi(sharedPrefManager.getSpUniqueId());
        getNotifikasi.enqueue(new Callback<GetNotifikasi>() {
            @Override
            public void onResponse(Call<GetNotifikasi> call, Response<GetNotifikasi> response) {
                List<Notifikasi> notificationList = response.body().getListNotifikasi();
                adapter = new AdapterNotification(notificationList, ma);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<GetNotifikasi> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete:
                show_confirm();
                return true;

            default:
                break;
        }

        return false;
    }

    public void show_confirm(){
        new SweetAlertDialog(NotificationActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Notifikasi akan dihapus permanen!")
                .setConfirmText("Hapus!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {
                        Call<ResponseBody> deleteNotifCall = mApiInterface.delNotifikasi(sharedPrefManager.getSpUniqueId());
                        deleteNotifCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                getIntent().putExtra("cek", "yes");
                                recreate();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                new SweetAlertDialog(NotificationActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Notifikasi gagal dihapus!")
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
