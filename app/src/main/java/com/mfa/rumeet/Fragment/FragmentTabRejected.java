package com.mfa.rumeet.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.mfa.rumeet.Adapter.AdapterJadwal;
import com.mfa.rumeet.Model.Booked;
import com.mfa.rumeet.Model.GetBooked;
import com.mfa.rumeet.Model.SharedPrefManager;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.Rest.ApiInterface;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTabRejected extends Fragment {

    ApiInterface mApiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    SharedPrefManager sharedPrefManager;
    SkeletonScreen skeletonScreen;
    private Context con;
    FragmentActivity activity;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout tvDataKosong;
    private TextView textDataKosong;

    public FragmentTabRejected(){

    }

    public static FragmentTabRejected getInstance(){
        return new FragmentTabRejected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_layout);
        tvDataKosong = (RelativeLayout) v.findViewById(R.id.data_kosong);
        textDataKosong = (TextView) v.findViewById(R.id.text_data_kosong);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        initControl();

        skeletonScreen = Skeleton.bind(mRecyclerView)
                .load(R.layout.load_jadwal)
                .show();
    }

    private void initControl(){
        activity = getActivity();

        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

        sharedPrefManager = new SharedPrefManager(activity);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                textDataKosong.setText("");
                tvDataKosong.setVisibility(View.GONE);

                skeletonScreen = Skeleton.bind(mRecyclerView)
                        .load(R.layout.load_jadwal)
                        .show();
                // Handler untuk menjalankan jeda selama 5 detik
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        // Berhenti berputar/refreshing
                        swipeRefreshLayout.setRefreshing(false);

                        refresh();

                    }
                }, 1000);
            }
        });

        con = getContext();

        refresh();

    }

    private void refresh(){

        Call<GetBooked> bookedCall = mApiInterface.getJadwal(sharedPrefManager.getSpUniqueId(),"Rejected");
        bookedCall.enqueue(new Callback<GetBooked>() {
            @Override
            public void onResponse(Call<GetBooked> call, Response<GetBooked> response) {
                List<Booked> bookedList = response.body().getListBooked();
                Log.d("Retrofit get", "ini tab rejected");
                AdapterJadwal adapter = new AdapterJadwal(bookedList, con);
                if(bookedList.size() == 0){
                    textDataKosong.setText("Tidak ada data booking rejected");
                    tvDataKosong.setVisibility(View.VISIBLE);
                    skeletonScreen.hide();
                }else {
                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<GetBooked> call, Throwable t) {
                Log.d("Retrofit Get", t.toString());
                skeletonScreen.hide();
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Tidak dapat terhubung ke server!")
                        .show();
            }
        });
    }


//        @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//        }
//    }

}
