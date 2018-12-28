package com.example.asus.taimusik;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class home_admin extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView nv;
    private FrameLayout frameFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        nv = (BottomNavigationView) findViewById(R.id.navigation);
        loadFragment (new admin_user());
        nv.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.navigationadmin1:
                fragment = new admin_user();
                break;

            case R.id.navigationadmin2:
                fragment = new Admin_upload2();
                break;

            case R.id.navigationadmin3:
                fragment = new admin_data();
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment (Fragment fragment){
        if (fragment !=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame2,fragment)
                    .commit();
            return true;
        }

        return false;
    }
}
