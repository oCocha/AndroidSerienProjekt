package com.serien.android.androidserienprojekt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.serien.android.androidserienprojekt.activities.FriendsActivity;
import com.serien.android.androidserienprojekt.activities.ListActivity;
import com.serien.android.androidserienprojekt.activities.SearchActivity;
import com.serien.android.androidserienprojekt.activities.Top10Activity;
import com.serien.android.androidserienprojekt.adapter.startActivityImageAdapter;

//Here ist whre the start icons are created
public class MainActivity extends Activity{

    private GridView gridview;


    int[] imageIds = {
            R.mipmap.search, R.mipmap.eigeneliste,
            R.mipmap.top, R.mipmap.friend
    };


     String[] textForIcons = {
            "Seriensuche", "Serienliste",
            "Top 10 Serien", "Freunde"
    };


    //Initializes the MainActivity.class
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
        setupAdapter();
        setupListener();

    }


    //Initializes the GridView of this Activity
    private void setupUI() {
        gridview = (GridView) findViewById(R.id.start_gridView);
    }


    //Sets a adapter on the GridView to show the Icons
    private void setupAdapter() {
        gridview.setAdapter(new startActivityImageAdapter(MainActivity.this, textForIcons, imageIds));
    }


    //Initializes the listener in this Activity
    private void setupListener() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intentSearch = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(intentSearch);
                        break;
                    case 1:
                        Intent intentList = new Intent(getApplicationContext(), ListActivity.class);
                        startActivity(intentList);
                        break;
                    case 2:
                        Intent intentTop = new Intent(getApplicationContext(), Top10Activity.class);
                        startActivity(intentTop);
                        break;
                    case 3:
                        Intent intentFriend = new Intent(getApplicationContext(), FriendsActivity.class);
                        startActivity(intentFriend);
                        break;
                }
            }
        });
    }

}
