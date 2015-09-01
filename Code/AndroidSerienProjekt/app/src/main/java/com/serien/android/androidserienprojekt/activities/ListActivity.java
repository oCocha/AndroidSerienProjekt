package com.serien.android.androidserienprojekt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.adapter.CustomListAdapter;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.SeriesRepository;

import java.util.ArrayList;

//Dies ist die ListActivity, in welcher die eigenen Serien angezeigt werden
public class ListActivity extends AppCompatActivity {

    private SeriesRepository db;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initDB();
        initUI();
        initAdapter();
    }

    //
    private void initDB() {
        db = new SeriesRepository(this);
        db.open();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }


    //Das Gridview wird mit einem Adapter verkn�pft, welcher zu Testzwecken die oben bef�llte Arrayliste benutzt
    private void initAdapter() {
        ArrayList<SeriesItem> seriesList = db.getAllSeriesItems();
        CustomListAdapter customListAdapter = new CustomListAdapter(this, seriesList);
        listView.setAdapter(customListAdapter);
    }

    //Die UI Elemente werden initialisiert und mit Listenern belegt
    private void initUI() {
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tempTextView = (TextView) view.findViewById(R.id.series_title);
                String seriesName = tempTextView.getText().toString();
                SeriesItem tempItem = db.getSeriesItems(seriesName);
                startIntent(tempItem.getImdbID());
            }
        });
    }

    //ein Intent mit StringExtra wird gestartet
    private void startIntent(String seriesName) {
        Intent startSeriesSeasonActivity = new Intent(this, SeriesSeasonActivity.class);
        startSeriesSeasonActivity.putExtra("SeriesName", seriesName);
        startActivity(startSeriesSeasonActivity);
    }
}
