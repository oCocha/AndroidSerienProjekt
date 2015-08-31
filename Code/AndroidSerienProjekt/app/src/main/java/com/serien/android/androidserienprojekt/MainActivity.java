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
import com.serien.android.androidserienprojekt.activities.Top30Activity;
import com.serien.android.androidserienprojekt.adapter.startActivityImageAdapter;

//Here ist whre the start icons are created
public class MainActivity extends Activity {


     int[] imageIds = {
            R.mipmap.search, R.mipmap.eigeneliste,
            R.mipmap.top, R.mipmap.friend
    };

     String[] textForIcons = {
            "Suche Serien", "Liste",
            "Top 30 Serien", "Freunde"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.startGridView);
        gridview.setAdapter(new startActivityImageAdapter(MainActivity.this, textForIcons, imageIds));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position){
                    case 0:
                        Intent intentSearch = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(intentSearch);
                        break;
                    case 1:
                        Intent intentList = new Intent(getApplicationContext(), ListActivity.class);
                        startActivity(intentList);
                        break;
                    case 2:
                        Intent intentTop = new Intent(getApplicationContext(), Top30Activity.class);
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
