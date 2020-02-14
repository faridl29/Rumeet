package com.mfa.rumeet.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mfa.rumeet.Adapter.SectionPagerAdapter;
import com.mfa.rumeet.R;
import com.mfa.rumeet.activity.HistoryActivity;
import com.mfa.rumeet.utils.Koneksi;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FragmentJadwal extends Fragment {

    TabLayout tabLayout;
    Toolbar toolbar;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jadwal, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        cekKoneksi();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("FragmentJadwal","Ini fragment jadwal");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar.setTitle("My Schedulle");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new FragmentTabPending(), "Pending");
        adapter.addFragment(new FragmentTabAccepted(), "Accepted");
        adapter.addFragment(new FragmentTabRejected(), "Rejected");


        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_history:
                Intent a = new Intent(getContext(), HistoryActivity.class);
                startActivity(a);
                return true;
            default:
                break;
        }

        return false;
    }

    public void cekKoneksi(){
        if(!Koneksi.isNetworkAvailable(Objects.requireNonNull(getActivity()))){
            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Cek koneksi anda!")
                    .show();
        }
    }


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//        }
//    }

}




