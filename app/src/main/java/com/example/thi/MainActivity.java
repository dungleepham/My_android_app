package com.example.thi;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mNavigationView;
    private ViewPager mViewPager;
    FirebaseUser fuser;
    String id = "";

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mNavigationView  = findViewById(R.id.bottomNav);
        mViewPager = findViewById(R.id.view_pager);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser != null){
            id = fuser.getUid();
        }
        else{
            id = "";
        }


        setUpViewPager();

        mNavigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.action_chat:
                    if(id != ""){
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        mViewPager.setCurrentItem(1);
                        break;
                    }


                case R.id.action_account:
                    mViewPager.setCurrentItem(2);
                    break;
            }
            return true;
        });


    }
    private void setUpViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        if(id != ""){
                            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            mNavigationView.getMenu().findItem(R.id.action_chat).setChecked(true);
                            break;}

                    case 2:
                        mNavigationView.getMenu().findItem(R.id.action_account).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}