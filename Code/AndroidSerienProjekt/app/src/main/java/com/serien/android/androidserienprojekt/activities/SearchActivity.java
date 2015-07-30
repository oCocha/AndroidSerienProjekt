package com.serien.android.androidserienprojekt.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.serien.android.androidserienprojekt.MainActivity;
import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.ImageDownloader;
import com.serien.android.androidserienprojekt.persistence.SeriesDataProvider;

import org.json.JSONObject;

//Dies ist die SearchActivity, in welcher Serien per AsyncTask gesucht und falls vorhanden angezeigt werden
public class SearchActivity extends ActionBarActivity implements SeriesDataProvider.OnSeriesDataProvidedListener{

    public static ImageView seriesImageView;
    public static TextView nameTextView;
    public static TextView yearTextView;
    public static TextView actorsTextView;
    public static TextView ratingTextView;
    public static TextView plotTextView;
    public static EditText seriesEditText;
    public static Button searchButton;
    SeriesDataProvider sdp = new SeriesDataProvider();
    public static JSONObject testObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initUI();
        initListener();
    }

    //Setzt einen Onclicklistener auf den SuchButton, welcher die Suche der Serie startet
    private void initListener() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tempString = seriesEditText.getText().toString();
                startDataFetching(tempString);
            }
        });
    }

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
    }

    public void onSeriesDataReceived(SeriesItem seriesItem) {
        System.out.println("CALLLLLLLLLLLLLBACKKKKKKKKKKK");
        nameTextView.setText(seriesItem.getName());
        actorsTextView.setText(seriesItem.getActors());
        ratingTextView.setText(seriesItem.getRating());
        yearTextView.setText(seriesItem.getYear());
        plotTextView.setText(seriesItem.getPlot());
        new ImageDownloader(SearchActivity.seriesImageView).execute(seriesItem.getImgPath());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Navigation zwischen den Activities
        if (id == R.id.series_Main) {
            Intent startMainActivity = new Intent(this, MainActivity.class);
            startActivity(startMainActivity);
//            SearchActivity.this.finish();
            return true;
        }else if (id == R.id.series_Top_30) {
            Intent startTop30Activity = new Intent(this, Top30Activity.class);
            startActivity(startTop30Activity);
//            SearchActivity.this.finish();
            return true;
        }else if (id == R.id.series_Friend) {
            Intent startFriendsActivity = new Intent(this, FriendsActivity.class);
            startActivity(startFriendsActivity);
//            SearchActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
