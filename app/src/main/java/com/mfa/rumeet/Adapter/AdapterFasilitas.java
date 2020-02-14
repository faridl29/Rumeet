package com.mfa.rumeet.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfa.rumeet.Model.Fasilitas;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.activity.Room_Detail_Activity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFasilitas extends RecyclerView.Adapter<AdapterFasilitas.MyViewHolder> {

    List<Fasilitas> mListFasilitas;
    Room_Detail_Activity con;

    public AdapterFasilitas(List<Fasilitas> mListFasilitas, Room_Detail_Activity con) {
        this.mListFasilitas = mListFasilitas;
        this.con = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_fasilitas, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Picasso.with(con).load(ApiClient.IMAGE_URL+mListFasilitas.get(i).getIcon())
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.mipmap.ic_launcher)
                .into(holder.icon_fasilitas);

        holder.nama_fasilitas.setText(mListFasilitas.get(i).getNama_fasilitas());
    }

    @Override
    public int getItemCount() {
        return mListFasilitas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon_fasilitas;
        private TextView nama_fasilitas;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            icon_fasilitas = (ImageView) itemView.findViewById(R.id.icon_fasilitas);
            nama_fasilitas = (TextView) itemView.findViewById(R.id.nama_fasilitas);
        }
    }
}
