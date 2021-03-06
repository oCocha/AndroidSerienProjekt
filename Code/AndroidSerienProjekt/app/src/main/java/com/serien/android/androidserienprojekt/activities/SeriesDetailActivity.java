package com.serien.android.androidserienprojekt.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.ImageDownloader;
import com.serien.android.androidserienprojekt.persistence.SeriesRepository;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by Igor on 17.09.2015.
 */
public class SeriesDetailActivity extends AppCompatActivity implements ImageDownloader.OnImageProvidedListener {

    public static ImageView seriesImageView;
    private SeriesRepository db;

    private SeriesItem specificSeries;
    private Bitmap seriesImage;

    private static Button addButton;

    private final static String USERNAME = UserActivity.getUserName();


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_detail);

        initDB();
        getData();
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


    //Get the data from the previous Activity
    public void getData() {
        Intent intent = getIntent();
        specificSeries = (SeriesItem) intent.getSerializableExtra("seriesItem");
    }


    //Initializes the UI of this activity
    private void initUI() {
        SeriesDetailActivity.super.setTitle("Series - " + specificSeries.getName());

        TextView serName = (TextView) findViewById(R.id.detail_series_name);
        seriesImageView = (ImageView) findViewById(R.id.detail_series_image);
        TextView serRating = (TextView) findViewById(R.id.detail_series_rating);
        TextView serYear = (TextView) findViewById(R.id.detail_series_year);
        TextView serActor = (TextView) findViewById(R.id.detail_series_actors);
        TextView serPlot = (TextView) findViewById(R.id.detail_series_description);
        addButton = (Button) findViewById(R.id.detail_add_button);

        serName.setText(specificSeries.getName());
        serRating.setText(specificSeries.getActors());
        serYear.setText(specificSeries.getYear());
        serActor.setText(specificSeries.getRating());
        serPlot.setText(specificSeries.getPlot());
        new ImageDownloader(this, null).execute(specificSeries.getImgPath(), specificSeries.getName());
    }


    //Initializes the listener for the add button. If the add button is clicked the specific series will be add to Parse.com and the local database
    private void initListener() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addSeriesItem(specificSeries);
                updateParseData();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                seriesImage.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bArray = bos.toByteArray();
                String encoded = Base64.encodeToString(bArray, Base64.DEFAULT);
                db.updateImage(specificSeries.getName(), encoded);

                SeriesDetailActivity.super.onBackPressed();
                showSuccessMessage();
            }
        });
    }


    //Add the specific seriesname to the Parse.com database
    private void updateParseData() {
        String parseClassName = "SerienApp";
        ParseQuery<ParseObject> query = new ParseQuery<>(parseClassName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(SeriesDetailActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getString("userName").equals(USERNAME))
                            list.get(i).addAllUnique("series", Collections.singletonList(specificSeries.getName()));
                            list.get(i).saveInBackground();
                    }
                }
            }
        });
    }


    //If the series is added to the database the user gets a message that adding of the series was a success
    private void showSuccessMessage() {
        String toastMessage = "'" + specificSeries.getName() + "' wurde der Liste hinzugefügt!";
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }


    //If the image data is received it gets updated in the view
    @Override
    public void onImageReceived(Bitmap image, Integer integer) {
        if(image == null){
            image = BitmapFactory.decodeResource(getResources(), R.mipmap.placeholder_image);
            seriesImage = image;
        }else{
            seriesImage = image;

        }
        seriesImageView.setImageBitmap(seriesImage);
    }

}
