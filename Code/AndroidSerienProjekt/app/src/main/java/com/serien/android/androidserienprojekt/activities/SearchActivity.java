package com.serien.android.androidserienprojekt.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.ImageDownloader;
import com.serien.android.androidserienprojekt.persistence.SeriesDataProvider;
import com.serien.android.androidserienprojekt.persistence.SeriesRepository;

//Dies ist die SearchActivity, in welcher Serien per AsyncTask gesucht und falls vorhanden angezeigt werden
public class SearchActivity extends ActionBarActivity implements SeriesDataProvider.OnSeriesDataProvidedListener{

    public static final String SERIES_NAME = "Serienname";
    public static final String SERIES_ACTORS = "Serienschauspieler";
    public static final String SERIES_YEAR = "Serienjahr";
    public static final String SERIES_RATING = "Serienwertung";
    public static final String SERIES_PLOT = "Plot der Serie";

    private String tempString;
    private SeriesRepository db;
    private SeriesItem foundItem;
    public static ImageView seriesImageView;
    public static TextView nameTextView;
    public static TextView yearTextView;
    public static TextView actorsTextView;
    public static TextView ratingTextView;
    public static TextView plotTextView;
    public static EditText seriesEditText;
    public static Button searchButton;
    public static Button addButton;
    public static Button deleteButton;
    public static final String NO_SERIES_DATA = "Gesuchte Serie leider nicht gefunden.";
    SeriesDataProvider sdp = new SeriesDataProvider();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initDB();
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



        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.deleteSeries(foundItem.getName());
                String deleteMessage = "'" + foundItem.getName() + "'" + " wurde aus der Liste entfernt!";
                Toast.makeText(getApplicationContext(), deleteMessage, Toast.LENGTH_SHORT).show();
                deleteButton.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.addSeriesItem(foundItem);
                String addMessage = "'" + foundItem.getName() + "'" + " wurde der Liste hinzugefügt!";
                Toast.makeText(getApplicationContext(), addMessage, Toast.LENGTH_SHORT).show();
                deleteButton.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);
            }
        });
    }

    private void hideKeayboard() {
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /*
    private void startIntent() {
        Intent startSeriesSeasonActivity = new Intent(this, SeriesSeasonActivity.class);
        startActivity(startSeriesSeasonActivity);
    }
    */

    private void startDataFetching(String tempString) {
        sdp.startSeriesFetching(this, tempString);
    }

    //Initialisiert die UI-Elemente
    private void initUI() {
        seriesImageView = (ImageView) findViewById(R.id.seriesImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        yearTextView = (TextView) findViewById(R.id.yearTextView);
        actorsTextView = (TextView) findViewById(R.id.actorsTextView);
        plotTextView = (TextView) findViewById(R.id.plotTextView);
        ratingTextView = (TextView) findViewById(R.id.ratingTextView);
        seriesEditText = (EditText) findViewById(R.id.seriesEditText);
        searchButton = (Button) findViewById(R.id.searchButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        addButton = (Button) findViewById(R.id.addButton);
    }


    public void onSeriesNotFound(String searchQuery) {
        String toastMessage = NO_SERIES_DATA + " '" + searchQuery + "'";
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        nameTextView.setText(SERIES_NAME);
        actorsTextView.setText(SERIES_ACTORS);
        ratingTextView.setText(SERIES_RATING);
        yearTextView.setText(SERIES_YEAR);
        plotTextView.setText(SERIES_PLOT);
        System.out.println(SERIES_NAME+SERIES_ACTORS);
    }


    //zeigt die Serieninformationen an, sobald ein Serienitem erhalten wurde
    public void onSeriesDataReceived(SeriesItem series) {
        System.out.println("CALLLLLLLLLLLLLBACKKKKKKKKKKK");
        nameTextView.setText(series.getName());
        actorsTextView.setText(series.getActors());
        ratingTextView.setText(series.getRating());
        yearTextView.setText(series.getYear());
        plotTextView.setText(series.getPlot());
        new ImageDownloader(SearchActivity.seriesImageView).execute(series.getImgPath());

        foundItem = series;


        if (db.getSeriesItems(series.getName()) != null) {
            deleteButton.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.GONE);
        }else{
            addButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
        }

    }
}
