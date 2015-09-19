package com.serien.android.androidserienprojekt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.serien.android.androidserienprojekt.activities.FriendsActivity;
import com.serien.android.androidserienprojekt.activities.ListActivity;
import com.serien.android.androidserienprojekt.activities.SearchActivity;
import com.serien.android.androidserienprojekt.activities.Top30Activity;
import com.serien.android.androidserienprojekt.activities.UserActivity;
import com.serien.android.androidserienprojekt.adapter.startActivityImageAdapter;
import com.serien.android.androidserienprojekt.domain.SeriesItem;

import java.util.Arrays;
import java.util.List;

//Here ist whre the start icons are created
public class MainActivity extends Activity {
    private String parseClassName = "SerienApp";
    private boolean isNewUser = true;

    int[] imageIds = {
            R.mipmap.search, R.mipmap.eigeneliste,
            R.mipmap.top, R.mipmap.friend
    };

     String[] textForIcons = {
            "Suche Serien", "liste",
            "Top 30 Serien", "Freunde"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupParse();
        GridView gridview = (GridView) findViewById(R.id.start_gridView);
        gridview.setAdapter(new startActivityImageAdapter(MainActivity.this, textForIcons, imageIds));
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

    private void setupParse() {
        ParseQuery<ParseObject> query = new ParseQuery<>(parseClassName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getString("userName").equals(UserActivity.getUserName())){
                            isNewUser = false;
                        }
                    }
                    if(isNewUser == true) {
                        System.out.println("NEUERUSERRRRRRRRRRRRRRRRRR."+UserActivity.getUserName()+list);
                        //    Eintrag einfügen
                        ParseObject tempParseObject = new ParseObject("SerienApp");
                        tempParseObject.put("userName", UserActivity.getUserName());
                        //    tempParseObject.addAllUnique("series", Arrays.asList("Dexter", "The Mentalist"));
                        tempParseObject.saveInBackground();
                    }
                }
            }
        });
    }
}
