package com.serien.android.androidserienprojekt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.adapter.CustomUserAdapter;
import com.serien.android.androidserienprojekt.persistence.SeriesRepository;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    ListView listV;
    private SeriesRepository db;
    ArrayList<String> userName = new ArrayList<>();
    ArrayList<String> seriesNames = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        initDB();
        getNames();
        initUI();
        initAdapter();

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
        String friend = "";
            switch(position){
                case 0:
                    friend = "friendOne";
                    break;
                case 1:
                    friend = "friendTwo";
                    break;
                case 2:
                    friend = "friendThree";
                    break;
        }

        Intent intentFriend = new Intent(getApplicationContext(), FriendsSeries.class);
        intentFriend.putExtra("friend", friend);
        intentFriend.putStringArrayListExtra("series", seriesNames);
        startActivity(intentFriend);

    }

    private void initAdapter() {
        CustomUserAdapter customUserAdapter = new CustomUserAdapter(this, userName);
        listV.setAdapter(customUserAdapter);
    }


}
