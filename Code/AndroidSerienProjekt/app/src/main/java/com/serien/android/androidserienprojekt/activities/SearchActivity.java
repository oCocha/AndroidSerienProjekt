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
    private ArrayList<String> seriesInsideDB = new ArrayList<>();
    ArrayList<SeriesItem> foundItem = new ArrayList<>();
    public static EditText seriesEditText;
    public static Button searchButton;
    public static TextView searchSuccessText;
    public static final String NO_SERIES_DATA = "Gesuchte Serie leider nicht gefunden.";
    SeriesDataProvider sdp = new SeriesDataProvider();

    ListView list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initDB();
        getDBData();
        initUI();
        initListener();
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



    //Setzt einen Onclicklistener auf den SuchButton, welcher die Suche der Serie startet
    private void initListener() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tempString = seriesEditText.getText().toString();
                startDataFetching(tempString);
                hideKeayboard();
            }
        });
    }


    private void hideKeayboard() {
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void startDataFetching(String tempString) {
        sdp.startSeriesFetching(this, tempString, 0);
    }


    //Initialisiert die UI-Elemente
    private void initUI() {
        seriesEditText = (EditText) findViewById(R.id.search_series_searchterm);
        searchButton = (Button) findViewById(R.id.search_searchButton);
        searchSuccessText = (TextView) findViewById(R.id.search_successed_text);

        list = (ListView) findViewById(R.id.search_found_series);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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


    private void startIntentSeriesInDB(SeriesItem tempItem) {
        Intent startSeriesOverviewActivity = new Intent(this, SeriesOverviewActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("seriesItem", tempItem);
        startSeriesOverviewActivity.putExtras(mBundle);
        startActivity(startSeriesOverviewActivity);
    }


    private void startIntentSeriesDetailView(SeriesItem ser) {
        Intent startSeriesDetailActivity = new Intent(this, SeriesDetailActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("seriesItem", ser);
        startSeriesDetailActivity.putExtras(mBundle);
        startActivity(startSeriesDetailActivity);
    }


    public void onSeriesNotFound(String searchQuery) {
        String toastMessage = NO_SERIES_DATA + " '" + searchQuery + "'";
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        if(!foundItem.isEmpty()){
            searchSuccessText.setVisibility(View.GONE);
            foundItem.clear();
        }
    }


    //zeigt die Serieninformationen an, sobald ein Serienitem erhalten wurde
    public void onSeriesDataReceived(SeriesItem series, Integer topListInt) {
        seriesEditText.setText("");
        searchSuccessText.setVisibility(View.VISIBLE);
        foundItem.clear();
        foundItem.add(series);
        new ImageDownloader(this, topListInt).execute(foundItem.get(topListInt).getImgPath());
    }

    private void initAdapter() {
        CustomListAdapter cLA = new CustomListAdapter(this, foundItem, seriesInsideDB);
        list.setAdapter((cLA));
    }


    public void getDBData() {
        seriesInsideDB = db.getAllSeriesNames();

    }

    @Override
    public void onImageReceived(Bitmap Image, Integer topListNumber) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.out.println("IMAGEEEEEEEEEEEEEEE:" + Image + " TOPLISTNUMBERRRRRRRRRRR:" + topListNumber);
        if(Image != null){
            Image.compress(Bitmap.CompressFormat.PNG, 100, bos);
        }
        byte[] bArray = bos.toByteArray();
        String encoded = Base64.encodeToString(bArray, Base64.DEFAULT);
        SeriesItem tempSeriesItem = foundItem.get(topListNumber);
        tempSeriesItem.updateimgString(encoded);
        foundItem.set(topListNumber, tempSeriesItem);
        initAdapter();
    }


    @Override
    protected void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}