package com.mfa.rumeet.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mfa.rumeet.Model.Booked;
import com.mfa.rumeet.Model.GetBooked;
import com.mfa.rumeet.Model.SharedPrefManager;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.Rest.ApiInterface;
import com.mfa.rumeet.activity.BookingActivity;
import com.mfa.rumeet.activity.EditProfileActivity;
import com.mfa.rumeet.activity.LoginActivity;
import com.mfa.rumeet.activity.MainActivity;
import com.mfa.rumeet.utils.Koneksi;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends Fragment {

    TextView nama, accepted, pending, rejected;
    TextView tvNama, tvEmail, tvDivisi, tvTelepon;
    ImageView foto_profile;
    Button btnLogout, btnSimpan;
    SharedPrefManager sharedPrefManager;
    ApiInterface mApiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        cekKoneksi();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        initControl();
    }

    public void initControl(){
        final FragmentActivity activity = getActivity();

        foto_profile = activity.findViewById(R.id.foto_profile);
        nama = activity.findViewById(R.id.tvNama);
        accepted = activity.findViewById(R.id.etAccepted);
        pending = activity.findViewById(R.id.etPending);
        rejected = activity.findViewById(R.id.etRejected);
        tvNama = activity.findViewById(R.id.nama);
        tvEmail = activity.findViewById(R.id.email);
        tvDivisi = activity.findViewById(R.id.divisi);
        tvTelepon = activity.findViewById(R.id.telepon);
        btnLogout = getActivity().findViewById(R.id.btnLogout);
        btnSimpan = activity.findViewById(R.id.btnSimpan);

        sharedPrefManager = new SharedPrefManager(activity);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);


        nama.setText(sharedPrefManager.getSPNama());
        tvNama.setText(sharedPrefManager.getSPNama());
        tvEmail.setText(sharedPrefManager.getSPEmail());
        tvDivisi.setText(sharedPrefManager.getSpDivisi());
        tvTelepon.setText(sharedPrefManager.getSpTelepon());

        if(!sharedPrefManager.getSpFotoProfile().equals("null")){
            foto_profile.setPadding(0,0,0,0);
            Picasso.with(activity).load(ApiClient.PROFILE_URL+sharedPrefManager.getSpFotoProfile())
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .error(R.drawable.ic_person)
                    .into(foto_profile);
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(activity, EditProfileActivity.class);
                startActivity(a);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah anda yakin?")
                        .setContentText("Akan keluar dari aplikasi!")
                        .setConfirmText("Logout!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                                startActivity(new Intent(activity, LoginActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                activity.finish();

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
        });

        accepted();
        pending();
        rejected();
    }

    public void cekKoneksi(){
        if(!Koneksi.isNetworkAvailable(Objects.requireNonNull(getActivity()))){
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Cek koneksi anda!")
                    .show();
        }
    }

    public void accepted(){
        Call<GetBooked> getBookedCall = mApiInterface.getJadwal(sharedPrefManager.getSpUniqueId(), "Accepted");
        getBookedCall.enqueue(new Callback<GetBooked>() {
            @Override
            public void onResponse(Call<GetBooked> call, Response<GetBooked> response) {
                List<Booked> bookedList = response.body().getListBooked();
                accepted.setText(String.valueOf(bookedList.size()));
            }

            @Override
            public void onFailure(Call<GetBooked> call, Throwable t) {

            }
        });
    }

    public void pending(){
        Call<GetBooked> getBookedCall = mApiInterface.getJadwal(sharedPrefManager.getSpUniqueId(), "Pending");
        getBookedCall.enqueue(new Callback<GetBooked>() {
            @Override
            public void onResponse(Call<GetBooked> call, Response<GetBooked> response) {
                List<Booked> bookedList = response.body().getListBooked();
                pending.setText(String.valueOf(bookedList.size()));
            }

            @Override
            public void onFailure(Call<GetBooked> call, Throwable t) {

            }
        });
    }

    public void rejected(){
        Call<GetBooked> getBookedCall = mApiInterface.getJadwal(sharedPrefManager.getSpUniqueId(), "Rejected");
        getBookedCall.enqueue(new Callback<GetBooked>() {
            @Override
            public void onResponse(Call<GetBooked> call, Response<GetBooked> response) {
                List<Booked> bookedList = response.body().getListBooked();
                rejected.setText(String.valueOf(bookedList.size()));
            }

            @Override
            public void onFailure(Call<GetBooked> call, Throwable t) {

            }
        });
    }


}
