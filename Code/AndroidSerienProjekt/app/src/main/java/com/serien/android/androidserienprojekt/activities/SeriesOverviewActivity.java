package com.serien.android.androidserienprojekt.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TabHost;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.adapter.MyFragmentPagerAdapter;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.SeriesRepository;

import java.util.ArrayList;
import java.util.List;

//Activity which contains Tabs, Fragments and the necessary Data
public class SeriesOverviewActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener{

    private SeriesRepository db;
    ViewPager viewPager;
    TabHost tabHost;
    Intent intent;
    SeriesItem specificSeries;
    static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_overview);
        initDB();
        getData();
        active = true;
        System.out.println("Overview aktiv???????????????????????? "+active);


        initViewPager();
        initTabHost();
    }


    private void initDB() {
        db = new SeriesRepository(this);
        db.open();
    }

    @Override
    protected void onDestroy() {
        active = false;
        System.out.println("Overview aktiv???????????????????????? "+active);
        db.close();
        super.onDestroy();
    }

    public boolean getActivityStatus(){
        return active;
    }

    private void initTabHost() {
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        String[] tabName = {"Overview", "Seasons"};

        for (String aTabName : tabName) {
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(aTabName);
            tabSpec.setIndicator(aTabName);
            tabSpec.setContent(new FakeContent(getApplicationContext()));
            tabHost.addTab(tabSpec);
        }
        tabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int selectedItem) {
        tabHost.setCurrentTab(selectedItem);

    }

    //viewPager Listener
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //tabHost Listener
    @Override
    public void onTabChanged(String tabId) {
        int selectedItem = tabHost.getCurrentTab();
        viewPager.setCurrentItem(selectedItem);

    }

    public String getSeriesID() {
        return specificSeries.getImdbID();
    }

    public void getData() {
        intent = getIntent();
        specificSeries = (SeriesItem) intent.getSerializableExtra("seriesItem");

    }

    public void deleteSeries(String name) {
        db.deleteSeries(name);
    }


    public class FakeContent implements TabHost.TabContentFactory {

        Context context;
        public FakeContent(Context context){
            this.context = context;
        }

        @Override
        public View createTabContent(String tag) {
            View fakeView = new View(context);
            fakeView.setMinimumHeight(0);
            fakeView.setMinimumWidth(0);
            return fakeView;
        }
    }

    private void initViewPager() {

        getIntent().putExtra("itemForFrag", specificSeries);

        viewPager = (ViewPager) findViewById(R.id.overview_view_pager);
        SeriesDetailFragment seriesDetailsFragment = new SeriesDetailFragment();
        SeriesSeasonActivity seriesSeasonsFragment = new SeriesSeasonActivity();

        List<Fragment> listFrag = new ArrayList<>();
        listFrag.add(seriesDetailsFragment);
        listFrag.add(seriesSeasonsFragment);

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), listFrag);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }
}
