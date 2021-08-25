package com.example.covidnotifier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.covidnotifier.adapters.WalkThroughPagerAdapter;


public class WalkThroughActivity extends AppCompatActivity {
    /*-----Variable Define------*/
    private ViewPager viewPager;
    private WalkThroughPagerAdapter walkThroughPagerAdapter;
    TextView txtskip;
    LinearLayout first, second, third;
    ImageView next;
    int CURRENTPAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // when this activity is about to be launch we need to check if its openened before or not
        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(mainActivity);
            finish();
        }

        setContentView(R.layout.activity_walk_through);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        txtskip = findViewById(R.id.txtskip);
        next = findViewById(R.id.next);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        viewPager = findViewById(R.id.viewpager);

        txtskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HomeIntent = new Intent(WalkThroughActivity.this, MainActivity.class);
                savePrefsData();
                startActivity(HomeIntent);
                finish();
            }
        });

        walkThroughPagerAdapter = new WalkThroughPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(walkThroughPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CURRENTPAGE = position;
                setcompletedStates(CURRENTPAGE);
                if (viewPager.getCurrentItem() == 2) {
                    txtskip.setVisibility(View.GONE);
                } else {
                    txtskip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() != 2) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else {
                    Intent HomeIntent = new Intent(WalkThroughActivity.this, MainActivity.class);
                    savePrefsData();
                    startActivity(HomeIntent);
                    finish();
                }
            }
        });
    }

    /*swipe to change line color method is here*/
    private void setcompletedStates(int CURRENTPAGE) {
        if (CURRENTPAGE == 0) {
            first.setBackgroundResource(R.drawable.dark_black_circle);
            second.setBackgroundResource(R.drawable.dark_grey_circle);
            third.setBackgroundResource(R.drawable.dark_grey_circle);
        }
        if (CURRENTPAGE == 1) {
            first.setBackgroundResource(R.drawable.dark_black_circle);
            second.setBackgroundResource(R.drawable.dark_black_circle);
            third.setBackgroundResource(R.drawable.dark_grey_circle);
        }
        if (CURRENTPAGE == 2) {
            first.setBackgroundResource(R.drawable.dark_black_circle);
            second.setBackgroundResource(R.drawable.dark_black_circle);
            third.setBackgroundResource(R.drawable.dark_black_circle);
        }
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend", false);
        return isIntroActivityOpnendBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.commit();
    }
}