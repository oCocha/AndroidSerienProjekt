package com.serien.android.androidserienprojekt.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.persistence.SeriesDataProvider;

import org.json.JSONObject;

public class SearchActivity extends ActionBarActivity {

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
        setContentView(R.layout.activity_test);
        initUI();
        initListener();
    }

    private void initListener() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tempString = seriesEditText.getText().toString();
                sdp.startSeriesFetching(tempString);
            }
        });    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
