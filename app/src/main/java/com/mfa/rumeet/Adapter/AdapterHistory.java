package com.mfa.rumeet.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfa.rumeet.Model.Booked;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.Rest.ApiInterface;

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.MyViewHolder> {

    private List<Booked> mHistoryList;
    private Context con;

    public AdapterHistory(List<Booked> mBookedList, Context con) {
        this.mHistoryList = mBookedList;
        this.con = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_history, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {
        holder.tvJudul.setText("Booking Ruangan");
        holder.tvPesan.setText(mHistoryList.get(i).getNama_ruangan()+", "+mHistoryList.get(i).getDari_jam()+" - "+mHistoryList.get(i).getSampai_jam());
        holder.tvTanggal.setText(mHistoryList.get(i).getTanggal());
        holder.tvStatus.setText(mHistoryList.get(i).getStatus());

        if(mHistoryList.get(i).getStatus().equals("Completed")){
            holder.gambar.setImageResource(R.drawable.completed);
            holder.tvStatus.setTextColor(ContextCompat.getColor(con, R.color.colorPrimary));
        }else{
            holder.gambar.setImageResource(R.drawable.expired);
            holder.tvStatus.setTextColor(ContextCompat.getColor(con, R.color.red));
        }

    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvJudul, tvPesan, tvTanggal, tvStatus;
        private ImageView gambar;
        private LinearLayout layout;
        private ApiInterface mApiInterface;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvPesan = itemView.findViewById(R.id.tvPesan);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            gambar = itemView.findViewById(R.id.gambar);
            layout = itemView.findViewById(R.id.linearLayout);
            mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        }
    }
}
