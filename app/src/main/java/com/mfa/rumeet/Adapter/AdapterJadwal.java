package com.mfa.rumeet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mfa.rumeet.Model.Booked;
import com.mfa.rumeet.Model.SharedPrefManager;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.Rest.ApiInterface;
import com.mfa.rumeet.activity.MainActivity;
import com.mfa.rumeet.activity.NotificationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterJadwal extends RecyclerView.Adapter<AdapterJadwal.MyViewHolder> {

    private List<Booked> mBookedList;
    private Context con;
    private ApiInterface mApiInterface;

    public AdapterJadwal(List<Booked> mBookedList, Context con) {
        this.mBookedList = mBookedList;
        this.con = con;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_jadwal, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterJadwal.MyViewHolder holder, final int i) {

        holder.tvNama.setText(mBookedList.get(i).getNama_ruangan());
        holder.tvTanggal.setText(mBookedList.get(i).getTanggal());
        holder.tvDariJam.setText(mBookedList.get(i).getDari_jam());
        holder.tvSampaiJam.setText(mBookedList.get(i).getSampai_jam());
        if(mBookedList.get(i).getStatus().equals("Pending")){
            holder.status.setBackgroundResource(R.drawable.bg_status_pending);
            holder.status.setText("Pending");
            holder.hiasan.setBackgroundColor(Color.parseColor("#2242e9"));
        }
        else if(mBookedList.get(i).getStatus().equals("Accepted")){
            holder.status.setBackgroundResource(R.drawable.bg_status_accepted);
            holder.status.setText("Accepted");
            holder.hiasan.setBackgroundColor(Color.parseColor("#06af06"));
        }
        else{
            holder.status.setBackgroundResource(R.drawable.bg_status_rejected);
            holder.status.setText("Rejected");
            holder.hiasan.setBackgroundColor(Color.parseColor("#ff030e"));
            holder.cancel.setVisibility(View.GONE);
        }

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_confirm(mBookedList.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBookedList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNama, tvTanggal, tvDariJam, tvSampaiJam, status, cancel;
        private RelativeLayout hiasan;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvNama = (TextView) itemView.findViewById(R.id.tvNamaRuangan);
            tvTanggal = (TextView) itemView.findViewById(R.id.tvTanggal);
            tvDariJam = (TextView) itemView.findViewById(R.id.tvDariJam);
            tvSampaiJam = (TextView) itemView.findViewById(R.id.tvSampaiJam);
            status = (TextView) itemView.findViewById(R.id.status);
            hiasan = (RelativeLayout) itemView.findViewById(R.id.hiasan);
            cancel = (TextView) itemView.findViewById(R.id.cancel);
        }
    }

    public void show_confirm(final Booked id){
        new SweetAlertDialog(con, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Booking akan di cancel!")
                .setConfirmText("Ya!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<ResponseBody> responseBodyCall = mApiInterface.cancelBooking(id.getId());
                        responseBodyCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                mBookedList.remove(id);
                                AdapterJadwal.this.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

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


}
