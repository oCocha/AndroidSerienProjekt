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
    private TextView noSeriesInList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initDB();
        initUI();
        initListener();
        initAdapter();
    }


    //Initializes the local database
    private void initDB() {
        db = new SeriesRepository(this);
        db.open();
    }


    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }


    //Initialises the UI of the activity to show the seriesitems
    private void initUI() {
        listView = (ListView) findViewById(R.id.list_general);
        noSeriesInList = (TextView) findViewById(R.id.no_list_available);
        if(db.getAllSeriesItems().isEmpty()){
            noSeriesInList.setVisibility(View.VISIBLE);
        }
    }


    //Initializes listener for each shown seriesitem
    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tempTextView = (TextView) view.findViewById(R.id.series_title_row);
                String seriesName = tempTextView.getText().toString();
                SeriesItem tempItem = db.getSeriesItem(seriesName);
                startIntent(tempItem);
            }
        });
    }


    //Initializes the adapter for this activity. Gets the necessary data like Series and the names of the Series inside the local database
    private void initAdapter() {
        ArrayList<SeriesItem> seriesList = db.getAllSeriesItems();
        ArrayList<String> seriesNames = db.getAllSeriesNames();
        CustomListAdapter customListAdapter = new CustomListAdapter(this, seriesList, seriesNames);
        listView.setAdapter(customListAdapter);
    }


    //Starts a intent and passes a Object to the SeriesOverviewActivity
    private void startIntent(SeriesItem series) {
        Intent startSeriesOverviewActivity = new Intent(this, SeriesOverviewActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("seriesItem", series);
        startSeriesOverviewActivity.putExtras(mBundle);
        startActivity(startSeriesOverviewActivity);
    }


    //Restarts the activity if its called again
    @Override
    protected void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}
