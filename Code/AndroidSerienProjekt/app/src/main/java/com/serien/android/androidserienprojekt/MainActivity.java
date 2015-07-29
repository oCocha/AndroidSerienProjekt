package com.serien.android.androidserienprojekt;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.serien.android.androidserienprojekt.activities.FriendsActivity;
import com.serien.android.androidserienprojekt.activities.SearchActivity;
import com.serien.android.androidserienprojekt.activities.TestActivity;
import com.serien.android.androidserienprojekt.activities.Top30Activity;
import com.serien.android.androidserienprojekt.adapter.CustomSeriesItemAdapter;
import com.serien.android.androidserienprojekt.domain.SeriesItem;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ArrayList<SeriesItem> seriesList = new ArrayList<SeriesItem>();
    CustomSeriesItemAdapter customSeriesItemAdapter;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        TESTfillArrayListTEST();
        initAdapter();
    }

    private void TESTfillArrayListTEST() {
        for(int i = 0; i < 5; i++) {
            SeriesItem tempSeriesItem = new SeriesItem("TestSerie", "TestJahr", "TestSchauspieler", "TestWertung", "TestPlot", "TestPfad");
            seriesList.add(tempSeriesItem);
        }
    }

    private void initAdapter() {
        customSeriesItemAdapter = new CustomSeriesItemAdapter(this, seriesList);
        gridView.setAdapter(customSeriesItemAdapter);
    }

    private void initUI() {
        gridView = (GridView) findViewById(R.id.seriesGridView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.series_Search) {
            Intent startSearchActivity = new Intent(this, SearchActivity.class);
            startActivity(startSearchActivity);
            MainActivity.this.finish();
            return true;
        }else if (id == R.id.series_Top_30) {
            Intent startTop30Activity = new Intent(this, Top30Activity.class);
            startActivity(startTop30Activity);
            MainActivity.this.finish();
            return true;
        }else if (id == R.id.series_Friend) {
            Intent startFriendsActivity = new Intent(this, FriendsActivity.class);
            startActivity(startFriendsActivity);
            MainActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
