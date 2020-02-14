package com.mfa.rumeet.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.mfa.rumeet.Adapter.AdapterBookingToday;
import com.mfa.rumeet.Adapter.RecyclerViewAdapter;
import com.mfa.rumeet.Model.Booked;
import com.mfa.rumeet.Model.GetBooked;
import com.mfa.rumeet.Model.GetNotifikasi;
import com.mfa.rumeet.Model.GetRuangan;
import com.mfa.rumeet.Model.Notifikasi;
import com.mfa.rumeet.Model.Ruangan;
import com.mfa.rumeet.Model.SharedPrefManager;
import com.mfa.rumeet.R;
import com.mfa.rumeet.Rest.ApiClient;
import com.mfa.rumeet.Rest.ApiInterface;
import com.mfa.rumeet.activity.NotificationActivity;
import com.mfa.rumeet.utils.Koneksi;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;

public class FragmentRuangan extends Fragment {

    NotificationBadge badge;
    private Context con;

    TextView greeting_text;
    ImageView greeting_img;

    ApiInterface mApiInterface;
    private RecyclerView mRecyclerView, listBookingToday;
    private RecyclerView.LayoutManager mLayoutManager , mLayoutManagerBookingToday;
    private CardView cardView;
    public static RecyclerViewAdapter adapter;
    public static AdapterBookingToday adapterBookingToday;
    public static FragmentRuangan ma;
    Toolbar toolbar;
    RelativeLayout layout;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SharedPrefManager sharedPrefManager;
    SkeletonScreen skeletonScreen;
    MaterialSearchView searchView;

    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        v = inflater.inflate(R.layout.fragment_ruangan, container, false);

        sharedPreferences = v.getContext().getSharedPreferences("cekShowCase", 0);
        editor = sharedPreferences.edit();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        listBookingToday = (RecyclerView) v.findViewById(R.id.listBookingToday);
        greeting_text = (TextView) v.findViewById(R.id.greeting_text  );
        greeting_img = (ImageView) v.findViewById(R.id.greeting_img);
        cardView = (CardView) v.findViewById(R.id.cardview);
        layout = (RelativeLayout) v.findViewById(R.id.card);

        skeletonScreen = Skeleton.bind(mRecyclerView)
                .load(R.layout.load_ruangan)
                .show();

        cekKoneksi();

        return v;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }


    @Override
    public void onStart() {
        super.onStart();
        initControl();
    }

    private void initControl(){
        final FragmentActivity activity = getActivity();


        toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mLayoutManagerBookingToday = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        listBookingToday.setLayoutManager(mLayoutManagerBookingToday);
        listBookingToday.setOnFlingListener(null);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(listBookingToday);

        searchView = activity.findViewById(R.id.search_view);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                layout.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {
                layout.setVisibility(View.VISIBLE);
            }
        });

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        ma = this;

        sharedPrefManager = new SharedPrefManager(activity);
        con = getContext();

        refresh();
        booking_today();
        badge_notifikasi();
        greeting();
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

        Call<GetRuangan> kontakCall = mApiInterface.getRuangan();
        kontakCall.enqueue(new Callback<GetRuangan>() {
            @Override
            public void onResponse(Call<GetRuangan> call, Response<GetRuangan> response) {
                if (response.isSuccessful()){

                    if(sharedPreferences.getBoolean("sudahTampil",false) == false){
                        ShowIntro("Booking Today", "Menampilkan jadwal meeting pada hari ini", getActivity().findViewById(R.id.listBookingToday), 1);
                    }

                    List<Ruangan> kontakList = response.body().getListRuangan();
                    adapter = new RecyclerViewAdapter(kontakList, con);
                    skeletonScreen.hide();
                    mRecyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<GetRuangan> call, Throwable t) {
                Log.d("Retrofit Get", t.toString());
                skeletonScreen.hide();

            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Call<GetRuangan> kontakCall = mApiInterface.getSearch(query);
                kontakCall.enqueue(new Callback<GetRuangan>() {
                    @Override
                    public void onResponse(Call<GetRuangan> call, Response<GetRuangan> response) {
                        List<Ruangan> kontakList = response.body().getListRuangan();
                        Log.d("Retrofit get", "Jumlah data Ruangan: "+
                                String.valueOf(kontakList.size()));
                        adapter = new RecyclerViewAdapter(kontakList, con);
                        skeletonScreen.hide();
                        mRecyclerView.setAdapter(adapter);


                    }

                    @Override
                    public void onFailure(Call<GetRuangan> call, Throwable t) {
                        Log.d("Retrofit Get", t.toString());
                        skeletonScreen.hide();
                        Toast.makeText(getContext(),"Tidak ada koneksi ke server", Toast.LENGTH_SHORT).show();
                    }
                });

                return false;
            }
        });
    }

    public void booking_today(){
        Call<GetBooked> getBookedCall = mApiInterface.getBookingToday(sharedPrefManager.getSpUniqueId());
        getBookedCall.enqueue(new Callback<GetBooked>() {
            @Override
            public void onResponse(Call<GetBooked> call, Response<GetBooked> response) {
                List<Booked> bookedList = response.body().getListBooked();

                if(bookedList.size() > 0){
                    adapterBookingToday = new AdapterBookingToday(bookedList,ma);
                    listBookingToday.setAdapter(adapterBookingToday);
                    cardView.setVisibility(View.GONE);
                    adapterBookingToday.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<GetBooked> call, Throwable t) {

            }
        });
    }

    public void badge_notifikasi(){
        Call<GetNotifikasi> getNotifikasiCall = mApiInterface.getNotif(sharedPrefManager.getSpUniqueId());
        getNotifikasiCall.enqueue(new Callback<GetNotifikasi>() {
            @Override
            public void onResponse(Call<GetNotifikasi> call, Response<GetNotifikasi> response) {
                List<Notifikasi> notifikasiList = response.body().getListNotifikasi();
                if(notifikasiList.size() > 0){
                    badge.setNumber(notifikasiList.size());
                }

            }

            @Override
            public void onFailure(Call<GetNotifikasi> call, Throwable t) {

            }
        });
    }

    private void greeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12){
            greeting_text.setText("Selamat Pagi\n"+sharedPrefManager.getSPNama());
            greeting_img.setImageResource(R.drawable.img_default_half_morning);
        } else if (timeOfDay >= 12 && timeOfDay < 15) {
            greeting_text.setText("Selamat Siang\n"+sharedPrefManager.getSPNama());
            greeting_img.setImageResource(R.drawable.img_default_half_afternoon);
        } else if (timeOfDay >= 15 && timeOfDay < 18) {
            greeting_text.setText("Selamat Sore\n"+sharedPrefManager.getSPNama());
            greeting_img.setImageResource(R.drawable.img_default_half_without_sun);
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            greeting_text.setText("Selamat Malam\n"+sharedPrefManager.getSPNama());
            greeting_text.setTextColor(Color.WHITE);
            greeting_img.setImageResource(R.drawable.img_default_half_night);
        }
    }

    private void ShowIntro(String title, String text, View viewId, final int type) {

        new GuideView.Builder(getContext())
                .setTitle(title)
                .setContentText(text)
                .setTargetView(viewId)
                .setGravity(GuideView.Gravity.center)
                .setContentTextSize(14)//optional
                .setTitleTextSize(16)//optional
                .setDismissType(GuideView.DismissType.anywhere) //optional - default dismissible by TargetView
                .setGuideListener(new GuideView.GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        if (type == 1) {
                            ShowIntro("Ruangan", "Klik ruangan untuk mengetahui detail ruangan dan melakukan booking", mRecyclerView.getLayoutManager().findViewByPosition(0), 6);
                        } else if (type == 6) {
                            editor.putBoolean("sudahTampil", true);
                            editor.commit();
                        }
                    }
                })
                .build()
                .show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);

        final MenuItem menuItem = menu.findItem(R.id.action_notification);
        MenuItem item = menu.findItem(R.id.action_search);

        searchView.setMenuItem(item);

        View actionView = MenuItemCompat.getActionView(menuItem);
        badge = actionView.findViewById(R.id.badge);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case R.id.action_notification:
                Intent a = new Intent(getActivity(), NotificationActivity.class);
                startActivity(a);
                return true;
            default:
                break;
        }

        return false;
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}
