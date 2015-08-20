package com.serien.android.androidserienprojekt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.adapter.CustomSeriesItemAdapter;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.SeriesRepository;

import java.util.ArrayList;

//Dies ist die ListActivity, in welcher die eigenen Serien angezeigt werden
public class ListActivity extends ActionBarActivity {

    private SeriesRepository db;
    private ArrayList<SeriesItem> seriesList = new ArrayList<>();
    CustomSeriesItemAdapter customSeriesItemAdapter;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDB();
        initUI();
        TESTfillArrayListTEST();
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

    //Zu Testzwecken wird eine Arraylist mit Seriesitems bef�llt
    //Diese werden verwendet um per Adapter das Gridview der Mainactivity zu bef�llen
    private void TESTfillArrayListTEST() {
      //  SeriesItem dexter = new SeriesItem("Dexter", "2007-2013", "Michael C. Hall", "8.9", "TestPlot", "http://ia.media-imdb.com/images/M/MV5BMTM5MjkwMTI0MV5BMl5BanBnXkFtZTcwODQwMTc0OQ@@._V1_SX300.jpg", "tt07732622");
      //  db.addSeriesItem(dexter);
         /*   db.addSeriesItem(new SeriesItem("Dexter", "2007-2013", "Michael C. Hall", "8.9", "TestPlot", "http://ia.media-imdb.com/images/M/MV5BMTM5MjkwMTI0MV5BMl5BanBnXkFtZTcwODQwMTc0OQ@@._V1_SX300.jpg", "tt07732622"));
            db.addSeriesItem(new SeriesItem("The Mentalist", "2008-2015", "TestSchauspieler", "TestWertung", "TestPlot", "http://ia.media-imdb.com/images/M/MV5BMTQ5OTgzOTczM15BMl5BanBnXkFtZTcwMDM2OTY4MQ@@._V1_SX300.jpg", "tt07732622"));
            db.addSeriesItem(new SeriesItem("Game of Thrones", "2012-2015", "TestSchauspieler", "TestWertung", "TestPlot", "http://ia.media-imdb.com/images/M/MV5BNTgxOTI4NzY2M15BMl5BanBnXkFtZTgwMjY3MTM2NDE@._V1_SX300.jpg", "tt07732622"));
            db.addSeriesItem(new SeriesItem("Breaking Bad", "2009-2013", "TestSchauspieler", "TestWertung", "TestPlot", "http://ia.media-imdb.com/images/M/MV5BMTQ0ODYzODc0OV5BMl5BanBnXkFtZTgwMDk3OTcyMDE@._V1_SX300.jpg", "tt07732622"));
            db.addSeriesItem(new SeriesItem("Full House", "1995-2003", "TestSchauspieler", "TestWertung", "TestPlot", "http://ia.media-imdb.com/images/M/MV5BMTk2Njk5ODYzNV5BMl5BanBnXkFtZTcwNzUxNDE0MQ@@._V1_SX300.jpg", "tt07732622"));
        */
    }

    //Das Gridview wird mit einem Adapter verkn�pft, welcher zu Testzwecken die oben bef�llte Arrayliste benutzt
    private void initAdapter() {
        seriesList = db.getAllSeriesItems();
        customSeriesItemAdapter = new CustomSeriesItemAdapter(this, seriesList);
        gridView.setAdapter(customSeriesItemAdapter);
    }

    //Die UI Elemente werden initialisiert und mit Listenern belegt
    private void initUI() {
        gridView = (GridView) findViewById(R.id.seriesGridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tempTextView = (TextView) view.findViewById(R.id.nameTextView);
                String seriesName = tempTextView.getText().toString();
                startIntent(seriesName);
            }
        });
    }

    //ein Intent mit StringExtra wird gestartet
    private void startIntent(String seriesName) {
        Intent startSeriesSeasonActivity = new Intent(this, SeriesSeasonActivity.class);
        startSeriesSeasonActivity.putExtra("SeriesName", seriesName);
        startActivity(startSeriesSeasonActivity);
    }

    /*

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

        //Navigation zwischen den verschiedenen Activities
        if (id == R.id.series_Search) {
            Intent startSearchActivity = new Intent(this, SearchActivity.class);
            startActivity(startSearchActivity);
//            ListActivity.this.finish();
            return true;
        }else if (id == R.id.series_Top_30) {
            Intent startTop30Activity = new Intent(this, Top30Activity.class);
            startActivity(startTop30Activity);
//            ListActivity.this.finish();
            return true;
        }else if (id == R.id.series_Friend) {
            Intent startFriendsActivity = new Intent(this, FriendsActivity.class);
            startActivity(startFriendsActivity);
//            ListActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
