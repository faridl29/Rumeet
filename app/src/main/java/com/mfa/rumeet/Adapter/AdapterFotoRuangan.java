package com.mfa.rumeet.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mfa.rumeet.Model.Foto_ruangan;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.activity.Room_Detail_Activity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFotoRuangan extends RecyclerView.Adapter<AdapterFotoRuangan.MyViewHolder> {

    List<Foto_ruangan> mFotoList;
    Room_Detail_Activity con;

    public AdapterFotoRuangan(List<Foto_ruangan> mFotoList, Room_Detail_Activity con) {
        this.mFotoList = mFotoList;
        this.con = con;
    }

    @Override
    public AdapterFotoRuangan.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_foto_ruangan, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(mView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterFotoRuangan.MyViewHolder holder,final int i) {
        Picasso.with(con).load(ApiClient.IMAGE_URL+mFotoList.get(i).getNama_foto())
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.mipmap.ic_launcher)
                .into(holder.foto);

        Picasso.with(con).invalidate(ApiClient.IMAGE_URL+mFotoList.get(i).getNama_foto());
    }

    @Override
    public int getItemCount() {
        return mFotoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView foto;

        private MyViewHolder(View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.list_foto_ruangan);

        }
    }
}
