package com.serien.android.androidserienprojekt.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.adapter.CustomListAdapter;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.ImageDownloader;
import com.serien.android.androidserienprojekt.persistence.SeriesDataProvider;
import com.serien.android.androidserienprojekt.persistence.SeriesRepository;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

//Dies ist die Top30Activity, in welcher beliebte/gut bewertete Serien per AsynTask geladen und angezeigt werden
public class Top30Activity extends ActionBarActivity implements SeriesDataProvider.OnSeriesDataProvidedListener,ImageDownloader.OnImageProvidedListener{
    public static final String NO_SERIES_DATA = "Gesuchte Serie wurde leider nicht gefunden";
    private SeriesRepository db;
    ArrayList<SeriesItem> top30SeriesItemList = new ArrayList<>();
    ArrayList<String> top30SeriesStringList = new ArrayList<>();
    ArrayList<String> seriesNames = new ArrayList<>();
    SeriesDataProvider sdp;
    ListView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top30);
        initDB();
        getDBData();
        initUI();
        setupTop30List();
    }

    private void initDB() {
        db = new SeriesRepository(this);
        db.open();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }



    //Sobald ein SeriesDataProvider SeriesItems liefert werden sie in eine ArraList gespeichert und per Adapter angezeigt
    public void onSeriesDataReceived(SeriesItem seriesItem, Integer topListNumber) {
        top30SeriesItemList.add(seriesItem);
        initAdapter();
        new ImageDownloader(this, topListNumber).execute(top30SeriesItemList.get(topListNumber).getImgPath());
    }


    public void onSeriesNotFound(String searchQuery) {
        String toastMessage = NO_SERIES_DATA + " '" + searchQuery + "'";
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }


    private void setupTop30List() {
        initTop30List();
        fetchTop30ListData();
    }

    //Hier wird die Top30 liste per AsyncTask aus der OMDBAPI geladen
    private void fetchTop30ListData() {
//        top30SeriesItemList.clear();
        if(top30SeriesItemList.size() < 1){
            for(int i = 0; i < top30SeriesStringList.size(); i++) {
                sdp = new SeriesDataProvider();
                sdp.startSeriesFetching(this, top30SeriesStringList.get(i), i);
            }
        }else{
            initAdapter();
        }
    }

    //Hier wird die Top30 liste probeweise manuell erstellt
    private void initTop30List() {
        top30SeriesStringList.add("Gotham");
        top30SeriesStringList.add("American Horror Story");
        top30SeriesStringList.add("Game of Thrones");
        top30SeriesStringList.add("Sons of Anarchy");
        top30SeriesStringList.add("The Walking Dead");
        top30SeriesStringList.add("Once Upon a Time");
        top30SeriesStringList.add("Outlander");
        top30SeriesStringList.add("The Blacklist");
        top30SeriesStringList.add("The Big Bang Theory");
        top30SeriesStringList.add("Scorpion");
    }

    //Hier wird ein Gridviewadapter erstellt und mit dem Gridview verkn�pft
    private void initAdapter() {

        CustomListAdapter customListAdapter = new CustomListAdapter(this, top30SeriesItemList, seriesNames);
        gridView.setAdapter(customListAdapter);
    }

    //Hier werden die UI Elemente erstellt
    private void initUI() {
        gridView = (ListView) findViewById(R.id.series_top);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tempText = (TextView) view.findViewById(R.id.series_title_row);
                String seriesName = tempText.getText().toString();
                if (seriesNames.contains(seriesName)) {
                    SeriesItem tempItem = db.getSeriesItem(seriesName);
                    startIntentSeriesInDB(tempItem);
                } else {
                    SeriesItem tempItem = top30SeriesItemList.get(position);
                    startIntentSeriesNotInDB(tempItem);
                }
            }
        });
    }


    private void startIntentSeriesInDB(SeriesItem tempItem) {
        Intent startSeriesOverviewActivity = new Intent(this, SeriesOverviewActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("seriesItem", tempItem);
        startSeriesOverviewActivity.putExtras(mBundle);
        startActivity(startSeriesOverviewActivity);
    }


    private void startIntentSeriesNotInDB(SeriesItem tempItem) {
        Intent startSeriesDetailActivity = new Intent(this, SeriesDetailActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("seriesItem", tempItem);
        startSeriesDetailActivity.putExtras(mBundle);
        startActivity(startSeriesDetailActivity);

    }


    @Override
    public void onImageReceived(Bitmap Image, Integer topListNumber) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.out.println("IMAGEEEEEEEEEEEEEEE:"+Image+" TOPLISTNUMBERRRRRRRRRRR:"+topListNumber);
        Image.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        String encoded = Base64.encodeToString(bArray, Base64.DEFAULT);
        SeriesItem tempSeriesItem = top30SeriesItemList.get(topListNumber);
        tempSeriesItem.updateimgString(encoded);
        top30SeriesItemList.set(topListNumber, tempSeriesItem);
        initAdapter();
    }

    public void getDBData() {
        seriesNames = db.getAllSeriesNames();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
