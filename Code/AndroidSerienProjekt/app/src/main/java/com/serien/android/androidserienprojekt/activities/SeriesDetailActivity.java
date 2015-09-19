package com.serien.android.androidserienprojekt.activities;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.serien.android.androidserienprojekt.MainActivity;
import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.ImageDownloader;
import com.serien.android.androidserienprojekt.persistence.SeriesRepository;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by King Igor on 17.09.2015.
 */
public class SeriesDetailActivity extends AppCompatActivity implements ImageDownloader.OnImageProvidedListener {

    public static ImageView seriesImageView;
    private SeriesRepository db;
    Intent intent;
    SeriesItem specificSeries;
    private Bitmap seriesImage;
    private static Button addButton;
    private String parseClassName = "SerienApp";
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


    private void initDB() {
        db = new SeriesRepository(this);
        db.open();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public void getData() {
        intent = getIntent();
        specificSeries = (SeriesItem) intent.getSerializableExtra("seriesItem");
    }

    static class ViewHolder{
        public ImageView serImage;
        public TextView serName;
        public TextView serRating;
        public TextView serYear;
        public TextView serActor;
        public TextView serPlot;
    }


    private void initUI() {
        SeriesDetailActivity.super.setTitle("Series - " + specificSeries.getName());


        //initialise the ViewHolder for the given Data
        ViewHolder viewHolder;
        viewHolder = new ViewHolder();

        viewHolder.serName = (TextView) findViewById(R.id.detail_series_name);
        seriesImageView = (ImageView) findViewById(R.id.detail_series_image);
        viewHolder.serRating = (TextView) findViewById(R.id.detail_series_rating);
        viewHolder.serYear = (TextView) findViewById(R.id.detail_series_year);
        viewHolder.serActor = (TextView) findViewById(R.id.detail_series_actors);
        viewHolder.serPlot = (TextView) findViewById(R.id.detail_series_description);
        addButton = (Button) findViewById(R.id.detail_add_button);

        viewHolder.serName.setText(specificSeries.getName());
        viewHolder.serRating.setText(specificSeries.getActors());
        viewHolder.serYear.setText(specificSeries.getYear());
        viewHolder.serActor.setText(specificSeries.getRating());
        viewHolder.serPlot.setText(specificSeries.getPlot());
        new ImageDownloader(this, null).execute(specificSeries.getImgPath(), specificSeries.getName());
    }


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
                showText();
            }
        });
    }

    private void updateParseData() {
        ParseQuery<ParseObject> query = new ParseQuery<>(parseClassName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(SeriesDetailActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getString("userName").equals(USERNAME))
                            list.get(i).addAllUnique("series", Arrays.asList(specificSeries.getName()));
                            list.get(i).saveInBackground();
                    }
                }
            }
        });
    }

    private void showText() {
        String toastMessage = "'" + specificSeries.getName() + "' wurde der Liste hinzugefügt!";
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onImageReceived(Bitmap image, Integer integer) {
        seriesImageView.setImageBitmap(image);
        seriesImage = image;
    }

}
