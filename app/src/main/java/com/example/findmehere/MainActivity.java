package com.example.findmehere;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    TabLayout tlNav;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    protected void init(){
        tlNav=findViewById(R.id.tlNav);
        viewPager=findViewById(R.id.viewPager);

        tlNav.setupWithViewPager(viewPager);
        MyAdaptorViewPage vpAdaptor=new MyAdaptorViewPage(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        vpAdaptor.addFragment(new homeFragment(),"Home");
        vpAdaptor.addFragment(new newPostFragment(),"Post");
//        vpAdaptor.addFragment(new MessegesContact(),"Message");
//        vpAdaptor.addFragment(new Profile1_fragment(),"Profile");

        viewPager.setAdapter(vpAdaptor);

        tlNav.setTabTextColors(ContextCompat.getColor(this, R.color.unselected_tab_color), ContextCompat.getColor(this, R.color.selected_tab_color));
    }

}