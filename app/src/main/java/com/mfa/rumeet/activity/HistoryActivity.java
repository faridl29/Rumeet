package com.mfa.rumeet.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mfa.rumeet.Adapter.AdapterHistory;
import com.mfa.rumeet.Model.Booked;
import com.mfa.rumeet.Model.GetBooked;
import com.mfa.rumeet.Model.SharedPrefManager;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.Rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private Context con;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPrefManager sharedPrefManager;
    private ApiInterface mApiInterface;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        toolbar= (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView.setLayoutManager(layoutManager);

        con = this;

        sharedPrefManager = new SharedPrefManager(this);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        refresh();

    }

    public void refresh(){
        Call<GetBooked> bookedCall = mApiInterface.getHistory(sharedPrefManager.getSpUniqueId());
        bookedCall.enqueue(new Callback<GetBooked>() {
            @Override
            public void onResponse(Call<GetBooked> call, Response<GetBooked> response) {
                List<Booked> bookedList = response.body().getListBooked();

                AdapterHistory adapterHistory = new AdapterHistory(bookedList, con);
                recyclerView.setAdapter(adapterHistory);
            }

            @Override
            public void onFailure(Call<GetBooked> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
