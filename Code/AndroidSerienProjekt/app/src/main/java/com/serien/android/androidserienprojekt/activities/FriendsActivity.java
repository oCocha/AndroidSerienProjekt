package com.serien.android.androidserienprojekt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.adapter.CustomUserAdapter;
import com.serien.android.androidserienprojekt.persistence.SeriesRepository;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    ListView listV;
    private SeriesRepository db;
    ArrayList<String> userName = new ArrayList<>();
    ArrayList<String> seriesNames = new ArrayList<>();
    ArrayList<ArrayList<String>> seriesList = new ArrayList<>();
    private String parseClassName = "SerienApp";
    private Handler handler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        initDB();
        //getNames();
        parseData();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initUI();
                initAdapter();
            }
        }, 1500);
    }

    private void parseData() {
        ParseQuery<ParseObject> query = new ParseQuery<>(parseClassName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(FriendsActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if (!(list.get(i).getString("userName").equals(UserActivity.getUserName()))) {
                            userName.add(list.get(i).getString("userName"));
                            seriesList.add((ArrayList) list.get(i).get("series"));
                        }
                    }
                }
            }
        });
    }

    public void getNames() {
        userName.add("Dante");
        userName.add("Tidus");
        userName.add("Cloud");
        seriesNames = db.getAllSeriesNames();
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


    private void initUI() {
        listV = (ListView) findViewById(R.id.friend_list);
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startIntent(position);
            }
        });
    }

    private void startIntent(int position) {
        if((seriesList.get(position) != null) && (seriesList.get(position).size() != 0)) {
            System.out.println("LISTENLÄNGEEEEEEEEEEEEEE:"+seriesList.get(position).size());
            Intent intentFriend = new Intent(getApplicationContext(), FriendsSeries.class);
            intentFriend.putExtra("username", userName.get(position));
            intentFriend.putStringArrayListExtra("allSeriesList", seriesList.get(position));
            intentFriend.putStringArrayListExtra("series", seriesNames);
            startActivity(intentFriend);
        }
    }

    private void initAdapter() {
        CustomUserAdapter customUserAdapter = new CustomUserAdapter(this, userName);
        listV.setAdapter(customUserAdapter);
    }


}
