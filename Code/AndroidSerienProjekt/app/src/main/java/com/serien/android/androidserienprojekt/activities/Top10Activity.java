package com.serien.android.androidserienprojekt.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


//Dies ist die Top10Activity, in welcher beliebte/gut bewertete Serien per AsynTask geladen und angezeigt werden
public class Top10Activity extends AppCompatActivity implements SeriesDataProvider.OnSeriesDataProvidedListener,ImageDownloader.OnImageProvidedListener{

    private SeriesRepository db;

    private ListView gridView;

    private ArrayList<SeriesItem> top30SeriesItemList = new ArrayList<>();
    private ArrayList<String> top30SeriesStringList = new ArrayList<>();
    private ArrayList<String> seriesNames = new ArrayList<>();

    public static final String NO_SERIES_DATA = "Gesuchte Serie wurde leider nicht gefunden";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top30);

        initDB();
        getDBData();
        initUI();
        initListener();
        setupTop10List();
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


    //Gets all series names that are inside the local database
    public void getDBData() {
        seriesNames = db.getAllSeriesNames();
    }


    //Initializes the UI
    private void initUI() {
        gridView = (ListView) findViewById(R.id.series_top);
    }


    //Sets a click listener on each series item
    private void initListener() {
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


    //If the specific tv serial is inside the local database, this intent starts the SeriesOverviewActivity
    private void startIntentSeriesInDB(SeriesItem tempItem) {
        Intent startSeriesOverviewActivity = new Intent(this, SeriesOverviewActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("seriesItem", tempItem);
        startSeriesOverviewActivity.putExtras(mBundle);
        startActivity(startSeriesOverviewActivity);
    }


    //If the specific tv serial is not inside the local database, this activity starts the detailView of the specific series
    private void startIntentSeriesNotInDB(SeriesItem tempItem) {
        Intent startSeriesDetailActivity = new Intent(this, SeriesDetailActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("seriesItem", tempItem);
        startSeriesDetailActivity.putExtras(mBundle);
        startActivity(startSeriesDetailActivity);
    }


    //Initializes the top 10 series list
    private void setupTop10List() {
        initTop10List();
        fetchTop10ListData();
    }


    //Inserting manually 10 series names inside an arraylist
    private void initTop10List() {
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


    //Sets a adapter on the view so that the user can click on each series item that is shown
    private void initAdapter() {
        CustomListAdapter customListAdapter = new CustomListAdapter(this, top30SeriesItemList, seriesNames);
        gridView.setAdapter(customListAdapter);
    }


    //Gets the series data with the names out of the previous created arraylist
    private void fetchTop10ListData() {
        for(int i = 0; i < top30SeriesStringList.size(); i++) {
            SeriesDataProvider sdp = new SeriesDataProvider();
            sdp.startSeriesFetching(this, top30SeriesStringList.get(i), i);
        }
    }


    //If the input of the user matches a seriesname the tv serial will be shown in the View
    public void onSeriesDataReceived(SeriesItem seriesItem, Integer topListNumber) {
        top30SeriesItemList.add(seriesItem);
        new ImageDownloader(this, topListNumber).execute(top30SeriesItemList.get(topListNumber).getImgPath());
    }


    //If the input of the user doesn't matches a series name then the user gets notified about it
    public void onSeriesNotFound(String searchQuery) {
        String toastMessage = NO_SERIES_DATA + " '" + searchQuery + "'";
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }


    //Updates the Imagepath of the found series
    @Override
    public void onImageReceived(Bitmap Image, Integer topListNumber) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        String encoded = Base64.encodeToString(bArray, Base64.DEFAULT);
        SeriesItem tempSeriesItem = top30SeriesItemList.get(topListNumber);
        tempSeriesItem.updateimgString(encoded);
        top30SeriesItemList.set(topListNumber, tempSeriesItem);
        initAdapter();
    }


    //Restarts the activity
    @Override
    protected void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}
