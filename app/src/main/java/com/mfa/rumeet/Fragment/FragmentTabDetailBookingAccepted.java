package com.mfa.rumeet.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.mfa.rumeet.Adapter.AdapterBooked;
import com.mfa.rumeet.Adapter.AdapterJadwal;
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

public class FragmentTabDetailBookingAccepted extends Fragment {

    private ApiInterface mApiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SharedPrefManager sharedPrefManager;
    private Context con;
    private FragmentActivity activity;

    private TextView tvDataKosong;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_booking_detail, container, false);

        tvDataKosong = (TextView) v.findViewById(R.id.data_kosong);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        initControl();
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

        con = getContext();

        refresh();

    }

    private void refresh(){

        Call<GetBooked> bookedCall = mApiInterface.getBookedAccepted(sharedPrefManager.getSpIdRuangan());
        bookedCall.enqueue(new Callback<GetBooked>() {
            @Override
            public void onResponse(Call<GetBooked> call, Response<GetBooked> response) {
                List<Booked> bookedList = response.body().getListBooked();
                Log.d("Retrofit get", "ini tab detail accepted");
                AdapterBooked adapter = new AdapterBooked(bookedList, con);
                if(bookedList.size() == 0){
                    tvDataKosong.setVisibility(View.VISIBLE);
                }else {
                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<GetBooked> call, Throwable t) {
                Log.d("Retrofit Get", t.toString());
            }
        });
    }


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//        }
//    }


//    @Override
//    public void setMenuVisibility(boolean menuVisible) {
//        super.setMenuVisibility(menuVisible);
//        if(menuVisible){
//            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//        }
//    }

}
