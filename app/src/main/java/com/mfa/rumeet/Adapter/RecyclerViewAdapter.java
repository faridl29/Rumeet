package com.mfa.rumeet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfa.rumeet.Model.Ruangan;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.activity.Room_Detail_Activity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<Ruangan> mRuanganList;
    private Context con;

    public RecyclerViewAdapter(List<Ruangan> mRuanganList, Context con) {
        this.mRuanganList = mRuanganList;
        this.con = con;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_ruangan, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder,final int i) {
        holder.tvnama_ruangan.setText(mRuanganList.get(i).getNama());
        holder.tvkapasitas_ruangan.setText("Kapasitas "+mRuanganList.get(i).getKapasitas()+ " orang");
        holder.tvlokasi_ruangan.setText(mRuanganList.get(i).getLokasi());
        holder.tvStatus.setText(mRuanganList.get(i).getStatus());

        if(mRuanganList.get(i).getStatus().equals("available")){
            holder.tvStatus.setBackgroundResource(R.drawable.bg_textview_available);
        }else{
            holder.tvStatus.setBackgroundResource(R.drawable.bg_textview_unavailable);
        }

        Picasso.with(con).load(ApiClient.IMAGE_URL+mRuanganList.get(i).getGambar())
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.mipmap.ic_launcher)
                .into(holder.imgruangan);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kirim = new Intent(con, Room_Detail_Activity.class);
                kirim.putExtra("id", mRuanganList.get(i).getId());
                kirim.putExtra("nama", mRuanganList.get(i).getNama());
                kirim.putExtra("gambar", mRuanganList.get(i).getGambar());
                kirim.putExtra("kapasitas", "Kapasitas "+mRuanganList.get(i).getKapasitas()+ " orang");
                kirim.putExtra("lokasi", mRuanganList.get(i).getLokasi());
                kirim.putExtra("keterangan", mRuanganList.get(i).getKeterangan_lain());
                kirim.putExtra("status", mRuanganList.get(i).getStatus());
                con.startActivity(kirim);
//                ((Activity)con).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRuanganList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgruangan;
        private TextView tvnama_ruangan, tvlokasi_ruangan, tvkapasitas_ruangan, tvStatus;
        private ViewHolder(View itemView) {
            super(itemView);
            imgruangan = (ImageView) itemView.findViewById(R.id.foto);
            tvnama_ruangan = (TextView) itemView.findViewById(R.id.nama_ruangan);
            tvkapasitas_ruangan = (TextView) itemView.findViewById(R.id.kapasitas_ruangan);
            tvlokasi_ruangan = (TextView) itemView.findViewById(R.id.lokasi_ruangan);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
        }
    }
}
