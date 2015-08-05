package com.serien.android.androidserienprojekt.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.adapter.CustomSeriesExpandableListAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//Dies ist die SeriesSeasonActivity, in welcher Serien mitsamt aller Staffeln und Episoden angezeigt werden und mithilfe einer Checkbox markiert werden können
public class SeriesSeasonActivity extends AppCompatActivity {

    List<String> seasonList;
    List<String> episodeList;
    Map<String, List<String>> seriesCollection;
    ExpandableListView expListView;
    TextView seriesNameTextView;
    Intent intent;
    String seriesName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_season);
        intent = getIntent();
        setupTestData();
        initUI();
        initAdapter();
    }

    //erstellt einen ExpandableListViewAdapter und verknüpft diesen mit der erstellten ExpandableList
    private void initAdapter() {
        final CustomSeriesExpandableListAdapter expListAdapter = new CustomSeriesExpandableListAdapter(this, seasonList, seriesCollection);
        expListView.setAdapter(expListAdapter);
    }

    //erstellt die UI Elemente
    private void initUI() {
        expListView = (ExpandableListView) findViewById(R.id.series_expandableList);
        seriesNameTextView = (TextView) findViewById(R.id.nameTextView);
        seriesNameTextView.setText(seriesName);
    }

    //erstellt einen Testseriendatensatz
    private void setupTestData() {
        seriesName = intent.getStringExtra("SeriesName");
        setupSeasonList();
        setupCollection();
    }

    private void setupCollection() {
        String[] seasonOne = { "Episode 1 - Pilot", "Episode 2 - Mitte",
                "Episode 3 - Finale" };
        String[] seasonTwo = { "Episode 1 - Pilot", "Episode 2 - Mitte",
                "Episode 3 - Finale" };
        String[] seasonThree = { "Episode 1 - Pilot", "Episode 2 - Mitte",
                "Episode 3 - Finale" };
        String[] seasonFour = { "Episode 1 - Pilot", "Episode 2 - Mitte",
                "Episode 3 - Finale" };
        String[] seasonFive = { "Episode 1 - Pilot", "Episode 2 - Mitte",
                "Episode 3 - Finale" };
        String[] seasonSix = { "Episode 1 - Pilot", "Episode 2 - Mitte",
                "Episode 3 - Finale" };

        seriesCollection = new LinkedHashMap<String, List<String>>();

        for (String compareString : seasonList) {
            if (compareString.equals("Staffel 1")) {
                loadChild(seasonOne);
            } else if (compareString.equals("Staffel 2"))
                loadChild(seasonTwo);
            else if (compareString.equals("Staffel 3"))
                loadChild(seasonThree);
            else if (compareString.equals("Staffel 4"))
                loadChild(seasonFour);
            else if (compareString.equals("Staffel 5"))
                loadChild(seasonFive);
            else
                loadChild(seasonSix);

            seriesCollection.put(compareString, episodeList);
        }
    }

    private void loadChild(String[] laptopModels) {
        episodeList = new ArrayList<String>();
        for (String model : laptopModels)
            episodeList.add(model);
    }

    private void setupSeasonList() {
        seasonList = new ArrayList<String>();
        seasonList.add("Staffel 1");
        seasonList.add("Staffel 2");
        seasonList.add("Staffel 3");
        seasonList.add("Staffel 4");
        seasonList.add("Staffel 5");
        seasonList.add("Staffel 6");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_series_season, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
