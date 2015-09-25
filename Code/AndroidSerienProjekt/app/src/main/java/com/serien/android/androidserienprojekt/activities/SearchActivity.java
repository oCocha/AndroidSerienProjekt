package com.serien.android.androidserienprojekt.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

//Dies ist die SearchActivity, in welcher Serien per AsyncTask gesucht und falls vorhanden angezeigt werden
public class SearchActivity extends ActionBarActivity implements SeriesDataProvider.OnSeriesDataProvidedListener, ImageDownloader.OnImageProvidedListener {


    private String tempString;
    private SeriesRepository db;
    SeriesDataProvider sdp = new SeriesDataProvider();

    private ArrayList<String> seriesInsideDB = new ArrayList<>();
    private ArrayList<SeriesItem> foundItem = new ArrayList<>();

    public static EditText seriesEditText;
    public static Button searchButton;
    public static TextView searchSuccessText;
    public static final String NO_SERIES_DATA = "Gesuchte Serie leider nicht gefunden.";

    private ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initDB();
        getDBData();
        initUI();
        initListener();
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


    //Gets series names of the series inside the local database
    public void getDBData() {
        seriesInsideDB = db.getAllSeriesNames();
    }


    //Initializes the UI and the buttons of this activity
    private void initUI() {
        seriesEditText = (EditText) findViewById(R.id.search_series_searchterm);
        searchButton = (Button) findViewById(R.id.search_searchButton);
        searchSuccessText = (TextView) findViewById(R.id.search_successed_text);

        listView = (ListView) findViewById(R.id.search_found_series);
    }


    //Sets a listener on the search button and on the view (specific series)
    private void initListener() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tempString = seriesEditText.getText().toString();
                startDataFetching(tempString);
                hideKeayboard();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tempText = (TextView) view.findViewById(R.id.series_title_row);
                String seriesName = tempText.getText().toString();
                if (seriesInsideDB.contains(seriesName)) {
                    SeriesItem tempItem = db.getSeriesItem(seriesName);
                    startIntentSeriesInDB(tempItem);
                } else {
                    SeriesItem ser = foundItem.get(position);
                    startIntentSeriesDetailView(ser);
                }
            }
        });
    }


    //Start searching for a series with the Input of the user
    private void startDataFetching(String tempString) {
        sdp.startSeriesFetching(this, tempString, 0);
    }


    //If the search button is clicked, the Keyboard gets hidden
    private void hideKeayboard() {
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    //if the found series is inside the local database, this intent starts the SeriesOverviewActivity
    private void startIntentSeriesInDB(SeriesItem tempItem) {
        Intent startSeriesOverviewActivity = new Intent(this, SeriesOverviewActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("seriesItem", tempItem);
        startSeriesOverviewActivity.putExtras(mBundle);
        startActivity(startSeriesOverviewActivity);
    }


    //If the found series is not inside the local database, this activity starts the detailView of the specific series
    private void startIntentSeriesDetailView(SeriesItem ser) {
        Intent startSeriesDetailActivity = new Intent(this, SeriesDetailActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("seriesItem", ser);
        startSeriesDetailActivity.putExtras(mBundle);
        startActivity(startSeriesDetailActivity);
    }


    //If the input of the user doesn't matches a series name then the user gets notified about it
    public void onSeriesNotFound(String searchQuery) {
        String toastMessage = NO_SERIES_DATA + " '" + searchQuery + "'";
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        if(!foundItem.isEmpty()){
            searchSuccessText.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            foundItem.clear();
        }
    }


    //If the input of the user matches a seriesname the series will be shown in the View
    public void onSeriesDataReceived(SeriesItem series, Integer topListInt) {
        seriesEditText.setText("");
        searchSuccessText.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
        foundItem.clear();
        foundItem.add(series);
        new ImageDownloader(this, topListInt).execute(foundItem.get(topListInt).getImgPath());
    }


    //Initializes the adapter for the View
    private void initAdapter() {
        CustomListAdapter cLA = new CustomListAdapter(this, foundItem, seriesInsideDB);
        listView.setAdapter((cLA));
    }


    //Updates the Imagepath of the found series
    @Override
    public void onImageReceived(Bitmap Image, Integer topListNumber) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        String encoded = Base64.encodeToString(bArray, Base64.DEFAULT);
        SeriesItem tempSeriesItem = foundItem.get(topListNumber);
        tempSeriesItem.updateimgString(encoded);
        foundItem.set(topListNumber, tempSeriesItem);
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