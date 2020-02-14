package com.mfa.rumeet.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mfa.rumeet.Adapter.RecyclerViewAdapter;
import com.mfa.rumeet.Model.GetRuangan;
import com.mfa.rumeet.Model.Ruangan;
import com.mfa.rumeet.Model.SharedPrefManager;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.Rest.ApiInterface;
import com.mfa.rumeet.utils.Koneksi;

import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFavorite extends Fragment {

    private Context con;
    ApiInterface mApiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SharedPrefManager sharedPrefManager;
    Toolbar toolbar;

    LinearLayout layout;

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        v = inflater.inflate(R.layout.fragment_favorite, container, false);

        mRecyclerView = v.findViewById(R.id.recyclerView);
        layout = v.findViewById(R.id.empty_layout);
        toolbar = v.findViewById(R.id.toolbar);

        cekKoneksi();

        return v;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);

        initControl();

    }

    public void initControl(){
        final FragmentActivity activity = getActivity();

        toolbar.setTitle("Favorite Room");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(activity);

        con = getContext();

        refresh();
    }

    public void cekKoneksi(){
        if(!Koneksi.isNetworkAvailable(Objects.requireNonNull(getActivity()))){
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Cek koneksi anda!")
                    .show();
        }
    }

    public void refresh(){

        Call<GetRuangan> responseBodyCall = mApiInterface.getFavorite(sharedPrefManager.getSpUniqueId());
        responseBodyCall.enqueue(new Callback<GetRuangan>() {
            @Override
            public void onResponse(Call<GetRuangan> call, Response<GetRuangan> response) {
                List<Ruangan> ruanganList = response.body().getListRuangan();
                if(ruanganList.size() == 0){
                    layout.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }else{
                    layout.setVisibility(View.GONE);
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(ruanganList, con);
                    mRecyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<GetRuangan> call, Throwable t) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Tidak dapat terhubung ke server!")
                        .show();
            }
        });
    }



}
