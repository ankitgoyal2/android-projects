package com.example.inventory.serviceMan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.inventory.R;
import com.example.inventory.serviceMan.fragments.ServicemanHistoryFragment;
import com.example.inventory.serviceMan.fragments.ServicemanHomeFragment;
import com.example.inventory.serviceMan.fragments.ServicemanNotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity {
    private ServicemanHistoryFragment servicemanHistoryfragment;
    private ServicemanHomeFragment servicemanHomefragment;
    private ServicemanNotificationFragment servicemanNotificationfragment;
    private SProfileFragment sProfileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceman_bottom_nav);
        BottomNavigationView bottomNavigationView = findViewById(R.id.s_bottom_bar);
        servicemanHomefragment = new ServicemanHomeFragment();
        servicemanHistoryfragment = new ServicemanHistoryFragment();
        servicemanNotificationfragment= new ServicemanNotificationFragment();
        sProfileFragment = new SProfileFragment();
        bottomNavigationView.setItemIconTintList(null);
        setOurFragment(servicemanHomefragment);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        setOurFragment(servicemanHomefragment);
                        return true;

                    case R.id.history:
                        setOurFragment(servicemanHistoryfragment);
                        return true;

                    case R.id.notification:
                        setOurFragment(servicemanNotificationfragment);
                        return true;

                    case R.id.profile:
                        setOurFragment(sProfileFragment);
                        return true;

                    default:
                        return false;
                }

            }
        });
    }
    private void setOurFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainframe,fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment uploadType = getSupportFragmentManager().findFragmentById(R.id.mainframe);
        if (uploadType != null) {
            uploadType.onActivityResult(12, resultCode, data);
        }
    }
}

