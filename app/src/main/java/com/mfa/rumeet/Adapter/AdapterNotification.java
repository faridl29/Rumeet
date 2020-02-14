package com.mfa.rumeet.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfa.rumeet.Model.Notifikasi;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.Rest.ApiInterface;
import com.mfa.rumeet.activity.MainActivity;
import com.mfa.rumeet.activity.NotificationActivity;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.MyViewHolder> {

    private List<Notifikasi> mNotifikasiList;
    private NotificationActivity con;

    public AdapterNotification(List<Notifikasi> mNotifikasiList, NotificationActivity con) {
        this.mNotifikasiList = mNotifikasiList;
        this.con = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_notifikasi, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        holder.tvJudul.setText(mNotifikasiList.get(i).getTitle());
        holder.tvPesan.setText(mNotifikasiList.get(i).getMessages());
        holder.tvTanggal.setText(mNotifikasiList.get(i).getWaktu());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> editIsClicked = holder.mApiInterface.editClicked(mNotifikasiList.get(i).getId());
                editIsClicked.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        holder.wasRead.setVisibility(View.GONE);

                        Intent intent = new Intent(con, MainActivity.class);
                        intent.putExtra("cek", "notif");
                        con.startActivity(intent);
                        ((Activity)con).finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                notifyDataSetChanged();
            }
        });

        if(mNotifikasiList.get(i).getIsClicked().equals("1")){
            holder.wasRead.setVisibility(View.GONE);
        }else{
            holder.wasRead.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mNotifikasiList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvJudul, tvPesan, tvTanggal;
        private ImageView wasRead;
        private ApiInterface mApiInterface;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvPesan = itemView.findViewById(R.id.tvPesan);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            wasRead = itemView.findViewById(R.id.wasRead);
            mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        }
    }

    public void delete(){
        notifyDataSetChanged();
    }
}
