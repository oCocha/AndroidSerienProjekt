package com.serien.android.androidserienprojekt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.serien.android.androidserienprojekt.activities.FriendsActivity;
import com.serien.android.androidserienprojekt.activities.ListActivity;
import com.serien.android.androidserienprojekt.activities.SearchActivity;
import com.serien.android.androidserienprojekt.activities.Top10Activity;
import com.serien.android.androidserienprojekt.activities.UserActivity;
import com.serien.android.androidserienprojekt.adapter.startActivityImageAdapter;


public class MainActivity extends Activity{

    private GridView gridview;
    private TextView userName;

    private boolean doubleBackClickedOnce = false;


    int[] imageIds = {
            R.mipmap.search_icon, R.mipmap.list_icon,
            R.mipmap.top_10_icon, R.mipmap.friends_icon
    };


     String[] textForIcons = {
            "Seriensuche", "Serienliste",
            "Top 10 Serien", "Serien anderer User"
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
        userName = (TextView) findViewById(R.id.display_user_name);
        gridview = (GridView) findViewById(R.id.start_gridView);
        userName.append("'" + UserActivity.getUserName() + "'");
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


    //Checks if the user pressed the hardware back-button twice
    @Override
    public void onBackPressed(){
        if(doubleBackClickedOnce){
            super.onBackPressed();
            return;
        }

        this.doubleBackClickedOnce = true;
        Toast.makeText(this, "Klicken sie noch einmal zurück um die App zu schließen", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackClickedOnce = false;
            }
        }, 2000);
    }

}
