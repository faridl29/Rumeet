package com.mfa.rumeet.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mfa.rumeet.Model.Booked;
import com.mfa.rumeet.R;
import java.util.List;

public class AdapterBooked extends RecyclerView.Adapter<AdapterBooked.MyViewHolder> {

    private List<Booked> mBookedList;
    private Context con;

    public AdapterBooked(List<Booked> mBookedList, Context con) {
        this.mBookedList = mBookedList;
        this.con = con;
    }

    @Override
    public AdapterBooked.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_booked, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(mView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterBooked.MyViewHolder holder,final int i) {
        holder.tvid.setText(String.valueOf(holder.getAdapterPosition()+1));
        holder.tvnama.setText(mBookedList.get(i).getNama());
        holder.tvtanggal.setText(mBookedList.get(i).getTanggal());
        holder.tvjam.setText("("+ mBookedList.get(i).getDari_jam()+" - "+mBookedList.get(i).getSampai_jam()+ ")");
    }

    @Override
    public int getItemCount() {
        return mBookedList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvid, tvnama, tvtanggal, tvjam;
        private MyViewHolder(View itemView) {
            super(itemView);
            tvid = (TextView) itemView.findViewById(R.id.idBooked);
            tvnama = (TextView) itemView.findViewById(R.id.namaBooked);
            tvtanggal = (TextView) itemView.findViewById(R.id.tanggalbooked);
            tvjam = (TextView) itemView.findViewById(R.id.jambooked);
        }
    }
}
