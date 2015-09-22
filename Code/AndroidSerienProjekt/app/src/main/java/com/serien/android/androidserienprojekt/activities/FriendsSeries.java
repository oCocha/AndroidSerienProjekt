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

/**
 * Created by Igor on 16.09.2015.
 */
public class FriendsSeries extends AppCompatActivity implements SeriesDataProvider.OnSeriesDataProvidedListener,ImageDownloader.OnImageProvidedListener{

    SeriesDataProvider sdp;
    private SeriesRepository db;
    ListView list;
    private ArrayList<String> seriesNamesInLocalDB = new ArrayList<>();


    ArrayList<String> friendSeriesItems = new ArrayList<>();
    ArrayList<SeriesItem> series = new ArrayList<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        initDB();
        getData();
        initUI();
        initListener();
        initSeriesViewAndAdapter();
    }


    //Initializes the SQLite database
    private void initDB() {
        db = new SeriesRepository(this);
        db.open();
    }


    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }


    //Gets the data which is provided by the FriendsActivity
    public void getData() {
        Intent intent = getIntent();
        friendSeriesItems = intent.getStringArrayListExtra("allSeriesList");
        seriesNamesInLocalDB = db.getAllSeriesNames();
    }


    //Initializes the UI
    private void initUI() {
        list = (ListView) findViewById(R.id.friend_list);

    }


    //Initializes the Click listener for each series element depending of the status
    private void initListener() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tempText = (TextView) view.findViewById(R.id.series_title_row);
                String tempSeriesName = tempText.getText().toString();
                if(seriesNamesInLocalDB.contains(tempSeriesName)){
                    SeriesItem tempSeriesItem = db.getSeriesItem(tempSeriesName);
                    startIntentSeriesInDB(tempSeriesItem);
                }else{
                    SeriesItem tempSeriesItem = series.get(position);
                    startIntentSeriesNotInDB(tempSeriesItem);
                }
            }
        });
    }


    //If the selected Item is in the local database, this Application starts the SeriesOverviewActivity
    private void startIntentSeriesInDB(SeriesItem tempSeriesItem) {
        Intent startSeriesOverviewActivity = new Intent(this, SeriesOverviewActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("seriesItem", tempSeriesItem);
        startSeriesOverviewActivity.putExtras(mBundle);
        startActivity(startSeriesOverviewActivity);
    }


    //If the selected Item is not in the local database, this Application starts the SeriesDetailActivity
    private void startIntentSeriesNotInDB(SeriesItem tempSeriesItem) {
        Intent startSeriesDetailActivity = new Intent(this, SeriesDetailActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("seriesItem", tempSeriesItem);
        startSeriesDetailActivity.putExtras(mBundle);
        startActivity(startSeriesDetailActivity);
    }


    //Gets data for each series item and initializes the adapter
    private void initSeriesViewAndAdapter() {
        if(series.isEmpty()){
            for(int i = 0; i < friendSeriesItems.size(); i++){
                sdp = new SeriesDataProvider();
                sdp.startSeriesFetching(this, friendSeriesItems.get(i), i);
            }
        }else{
            initAdapter();
        }
    }


    //coming soon...
    public void onSeriesDataReceived(SeriesItem seriesItem, Integer topListNumber) {
        series.add(seriesItem);
        initAdapter();
        new ImageDownloader(this, topListNumber).execute(series.get(topListNumber).getImgPath());
    }


    //coming soon...
    public void onSeriesNotFound(String searchQuery) {
        String toastMessage = "Error" + " '" + searchQuery + "'";
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }


    //coming soon...
    @Override
    public void onImageReceived(Bitmap Image, Integer topListNumber) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        String encoded = Base64.encodeToString(bArray, Base64.DEFAULT);
        SeriesItem tempSeriesItem = series.get(topListNumber);
        tempSeriesItem.updateimgString(encoded);
        series.set(topListNumber, tempSeriesItem);
        initAdapter();
    }


    //Initializes the adapter to show the series items in a list
    private void initAdapter() {
        CustomListAdapter customListAdapter = new CustomListAdapter(this, series, seriesNamesInLocalDB);
        list.setAdapter(customListAdapter);
    }


    //If the User goes back to this Activity, the Activity restarts itself
    @Override
    protected void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}
