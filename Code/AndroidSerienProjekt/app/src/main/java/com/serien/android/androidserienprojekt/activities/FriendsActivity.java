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

    private ListView listV;
    private SeriesRepository db;
    private Handler handler = new Handler();

    private ArrayList<String> userName = new ArrayList<>();
    private ArrayList<Integer> numbOfSeries = new ArrayList<>();
    ArrayList<ArrayList<String>> seriesList = new ArrayList<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        initDB();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initUI();
                initListener();
                initAdapter();
            }
        }, parseData());
    }


    //Initializes the SQLite database
    private void initDB() {
        db = new SeriesRepository(this);
        db.open();
    }


    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }


    //Gets the Users which are saved in the Parse.com Database
    private long parseData() {
        String parseClassName = "SerienApp";
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
                    for(int i = 0; i <seriesList.size() ; i++){
                        if(seriesList.get(i) != null) {
                            int serNumb = seriesList.get(i).size();
                            numbOfSeries.add(serNumb);
                        }else{
                            numbOfSeries.add(0);
                        }
                    }
                }
            }
        });
        return 1500;
    }


    //Initializes the UI where all users are shown
    private void initUI() {
        listV = (ListView) findViewById(R.id.friend_list);
    }


    //Initializes the listener for each user in the listView
    private void initListener() {
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startIntent(position);
            }
        });
    }


    //If a user is Clicked, his Serieslist will be shown, else the User gets a message that this User has no Series in his list
    private void startIntent(int position) {
        if((seriesList.get(position) != null) && (seriesList.get(position).size() != 0)) {
            showSelectedUsersList(position);
        }else{
            showErrorMessage(position);
        }
    }


    //Shows the list of the selected User and get all Names from the local database
    private void showSelectedUsersList(int position) {
        Intent intentFriend = new Intent(getApplicationContext(), FriendsSeries.class);
        intentFriend.putStringArrayListExtra("allSeriesList", seriesList.get(position));
        startActivity(intentFriend);
    }


    //Shows a message that the particular User has no Series in his list
    private void showErrorMessage(int position) {
        Toast.makeText(FriendsActivity.this, "Der Benutzer '" + userName.get(position) + "' hat keine Serien in seiner Liste!", Toast.LENGTH_SHORT).show();
    }


    //Initializes the adapter to show the User picture and his name
    private void initAdapter() {
        CustomUserAdapter customUserAdapter = new CustomUserAdapter(this, userName, numbOfSeries);
        listV.setAdapter(customUserAdapter);
    }

}
