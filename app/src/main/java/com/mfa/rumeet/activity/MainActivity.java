package com.mfa.rumeet.activity;




import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.mfa.rumeet.Fragment.FragmentFavorite;
import com.mfa.rumeet.Fragment.FragmentJadwal;
import com.mfa.rumeet.Fragment.FragmentProfile;
import com.mfa.rumeet.Fragment.FragmentRuangan;
import com.mfa.rumeet.R;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity {

    final Fragment fragment11 = new FragmentRuangan();
    final Fragment fragment22 = new FragmentJadwal();
    final Fragment fragment33 = new FragmentFavorite();
    final Fragment fragment44 = new FragmentProfile();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String cek_data = getIntent().getStringExtra("cek");

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        if(cek_data == null){
            fm.beginTransaction().hide(active).replace(R.id.fl_container, fragment11, "1").show(fragment11).commit();
            active = fragment11;
        }else if(cek_data.equals("notif")){
            fm.beginTransaction().hide(active).replace(R.id.fl_container, fragment22, "2").addToBackStack(null).show(fragment22).commit();
            active = fragment22;
            View view = bottomNavigationView.findViewById(R.id.navigation_jadwal);
            view.performClick();
        }else if(cek_data.equals("profile")){
            fm.beginTransaction().hide(active).replace(R.id.fl_container, fragment44, "4").show(fragment44).commit();
            active = fragment44;
            View view = bottomNavigationView.findViewById(R.id.navigation_profile);
            view.performClick();
        }else if(cek_data.equals("jadwal")) {
            fm.beginTransaction().hide(active).replace(R.id.fl_container, fragment22, "2").show(fragment22).commit();
            active = fragment22;
            View view = bottomNavigationView.findViewById(R.id.navigation_jadwal);
            view.performClick();
        }else if(cek_data.equals("berhasil")){
            fm.beginTransaction().hide(active).replace(R.id.fl_container, fragment11, "1").show(fragment11).commit();
            active = fragment11;
            new SweetAlertDialog(MainActivity.this)
                    .setTitleText("Berhasil melakukan booking!")
                    .show();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

               switch (menuItem.getItemId()){
                   case R.id.navigation_home:

                       if(active != fragment11){

                           fm.beginTransaction().hide(active).replace(R.id.fl_container, fragment11, "1").show(fragment11).commit();
                           active = fragment11;
                       }
                       return true;

                   case R.id.navigation_jadwal:

                       fm.beginTransaction().hide(active).replace(R.id.fl_container, fragment22, "2").addToBackStack(null).show(fragment22).commit();
                       active = fragment22;
                       return true;

                   case R.id.navigation_favorite:

                       fm.beginTransaction().hide(active).replace(R.id.fl_container, fragment33, "3").show(fragment33).commit();
                       active = fragment33;
                       return true;

                   case R.id.navigation_profile:

                       fm.beginTransaction().hide(active).replace(R.id.fl_container, fragment44, "4").show(fragment44).commit();
                       active = fragment44;
                       return true;
               }
               return false;
           }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}