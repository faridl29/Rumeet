package com.mfa.rumeet.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arindicatorview.ARIndicatorView;
import com.mfa.rumeet.Adapter.AdapterFasilitas;
import com.mfa.rumeet.Adapter.AdapterFotoRuangan;
import com.mfa.rumeet.Adapter.SectionPagerAdapter;
import com.mfa.rumeet.Adapter.ViewPagerWrapContent;
import com.mfa.rumeet.Fragment.FragmentTabDetailBookingAccepted;
import com.mfa.rumeet.Fragment.FragmentTabDetailBookingPending;
import com.mfa.rumeet.Model.Fasilitas;
import com.mfa.rumeet.Model.Foto_ruangan;
import com.mfa.rumeet.Model.GetFasilitas;
import com.mfa.rumeet.Model.GetFoto;
import com.mfa.rumeet.Model.SharedPrefManager;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.Rest.ApiInterface;
import com.mfa.rumeet.utils.Tools;
import com.mfa.rumeet.utils.ViewAnimation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Room_Detail_Activity extends AppCompatActivity {

    private View lyt_expand_info;
    private Button btnBook;
    private TextView bt_hide_info;
    private NestedScrollView nested_scroll_view;
    private ImageButton  bt_toggle_info;

    TabLayout tabLayout;
    ViewPagerWrapContent viewPager;

    Toolbar toolbar;
    TextView tvNama, tvlokasi, tvkapasitas, tvketerangan, tvStatus;
    ImageView favorite;

    ApiInterface mApiInterface;
    private RecyclerView mRecyclerViewFoto, mRecyclerViewFasilitas;
    private static RecyclerView.LayoutManager mLayoutManagerFoto, mlayoutManagerFasilitas;
    public static Room_Detail_Activity con;
    private ARIndicatorView pageIndicator;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvNama = (TextView) findViewById(R.id.nama_ruangan);
        tvlokasi = (TextView) findViewById(R.id.tvlokasi);
        tvkapasitas = (TextView) findViewById(R.id.tvkapasitas);
        tvketerangan = (TextView) findViewById(R.id.tvketerangan);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btnBook = (Button) findViewById(R.id.btnBook);
        favorite = (ImageView) findViewById(R.id.favorite);
        pageIndicator = (ARIndicatorView) findViewById(R.id.pageIndicator);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPagerWrapContent) findViewById(R.id.view_pager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("nama"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNama.setText(getIntent().getStringExtra("nama"));
        tvlokasi.setText(getIntent().getStringExtra("lokasi"));
        tvkapasitas.setText(getIntent().getStringExtra("kapasitas"));
        tvketerangan.setText(getIntent().getStringExtra("keterangan"));
        tvStatus.setText(getIntent().getStringExtra("status"));

        if(getIntent().getStringExtra("status").equals("available")){
            tvStatus.setBackgroundResource(R.drawable.bg_textview_available);
            btnBook.setEnabled(true);
        }else{
            tvStatus.setBackgroundResource(R.drawable.bg_textview_unavailable);
            btnBook.setEnabled(false);
            btnBook.setBackgroundColor(this.getResources().getColor(R.color.grey_20));
        }


        mRecyclerViewFoto = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManagerFoto = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewFoto.setLayoutManager(mLayoutManagerFoto);

        mRecyclerViewFasilitas = (RecyclerView) findViewById(R.id.recyclerViewFasilitas);
        mlayoutManagerFasilitas = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewFasilitas.setLayoutManager(mlayoutManagerFasilitas);


        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Room_Detail_Activity.this, BookingActivity.class);
                a.putExtra("id", getIntent().getStringExtra("id"));
                a.putExtra("nama_ruangan", getIntent().getStringExtra("nama"));
                startActivity(a);
            }
        });


        //Ambil string
        final String id = getIntent().getStringExtra("id");
        final String nama = getIntent().getStringExtra("nama");
        final String gambar = getIntent().getStringExtra("gambar");
        final String kapasitas = getIntent().getStringExtra("kapasitas");
        final String lokasi = getIntent().getStringExtra("lokasi");
        final String keterangan = getIntent().getStringExtra("keterangan");

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite();
            }
        });

        con = this;
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_RUANGAN, id);

        fotoRuangan();
        fasilitas_ruangan();
        cek_button_favorite();
        expansionpanel();
        tab();
    }

    public void fotoRuangan(){
        Call<GetFoto> fotoCall = mApiInterface.getFotoRuangan(getIntent().getStringExtra("id"));
        fotoCall.enqueue(new Callback<GetFoto>() {
            @Override
            public void onResponse(Call<GetFoto> call, Response<GetFoto> response) {

                List<Foto_ruangan> foto_ruanganList = response.body().getFoto_ruanganList();
                AdapterFotoRuangan adapterFotoRuangan = new AdapterFotoRuangan(foto_ruanganList ,con);
                mRecyclerViewFoto.setAdapter(adapterFotoRuangan);
                pageIndicator.attachTo(mRecyclerViewFoto, true);
                pageIndicator.setNumberOfIndicators(foto_ruanganList.size());
                adapterFotoRuangan.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GetFoto> call, Throwable t) {

            }
        });
    }

    public void fasilitas_ruangan(){
        Call<GetFasilitas> getFasilitasCall = mApiInterface.getFasilitas(getIntent().getStringExtra("id"));
        getFasilitasCall.enqueue(new Callback<GetFasilitas>() {
            @Override
            public void onResponse(Call<GetFasilitas> call, Response<GetFasilitas> response) {
                List<Fasilitas> fasilitasList = response.body().getListFasilitas();
                AdapterFasilitas adapterFasilitas = new AdapterFasilitas(fasilitasList, con);
                mRecyclerViewFasilitas.setAdapter(adapterFasilitas);

                if(fasilitasList.size() <= 4 ){
                    ViewCompat.setNestedScrollingEnabled(mRecyclerViewFasilitas, false);
                    mRecyclerViewFasilitas.setNestedScrollingEnabled(false);
                    mRecyclerViewFasilitas.setLayoutFrozen(true);
                }
            }

            @Override
            public void onFailure(Call<GetFasilitas> call, Throwable t) {

            }
        });
    }


    public void expansionpanel(){

        bt_toggle_info = (ImageButton) findViewById(R.id.bt_toggle_info);
        bt_hide_info = (TextView) findViewById(R.id.bt_hide_info);
        lyt_expand_info = (View) findViewById(R.id.lyt_expand_info);
        bt_toggle_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfo(bt_toggle_info);
            }
        });

        bt_hide_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfo(bt_toggle_info);
            }
        });


        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);
    }

    private void toggleSectionInfo(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_info);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    public void tab(){
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void favorite(){
        Call<ResponseBody> ruanganCall = mApiInterface.cekFavorite(sharedPrefManager.getSpUniqueId(), getIntent().getStringExtra("id"));
        ruanganCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());

                        if (jsonRESULTS.getString("status").equals("tambah")) {

                            favorite.setImageResource(R.drawable.ic_favorite_active);
                        }else{
                            favorite.setImageResource(R.drawable.ic_favorite_no_active);
                        }
                        String error_message = jsonRESULTS.getString("message");
                        Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void cek_button_favorite(){
        Call<ResponseBody> ruanganCall = mApiInterface.cekButtonFavorite(sharedPrefManager.getSpUniqueId(), getIntent().getStringExtra("id"));
        ruanganCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());

                        if (jsonRESULTS.getString("status").equals("ada")) {

                            favorite.setImageResource(R.drawable.ic_favorite_active);
                        }else{
                            favorite.setImageResource(R.drawable.ic_favorite_no_active);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentTabDetailBookingAccepted(), "Accepted");
        adapter.addFragment(new FragmentTabDetailBookingPending(), "Pending");


        viewPager.setAdapter(adapter);
    }



    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(Room_Detail_Activity.this, MainActivity.class);
//        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
