package com.serien.android.androidserienprojekt.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.adapter.CustomListAdapter;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.ImageDownloader;
import com.serien.android.androidserienprojekt.persistence.SeriesDataProvider;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor on 16.09.2015.
 */
public class FriendsSeries extends AppCompatActivity implements SeriesDataProvider.OnSeriesDataProvidedListener,ImageDownloader.OnImageProvidedListener{
    SeriesDataProvider sdp;
    ListView list;
    private String selectedFriend = "";
    private ArrayList<String> seriesNames = new ArrayList<>();


    ArrayList<String> friendSeriesItems = new ArrayList<>();
    ArrayList<SeriesItem> series = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        initUI();
        getData();


        ParseQuery<ParseObject> query = new ParseQuery<>(selectedFriend);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(FriendsSeries.this, "Error " + e, Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        friendSeriesItems.add(list.get(i).getString("seriesName"));
                    }
                }

                setupViewAndGetData(friendSeriesItems);

            }
        });


    }

    private void initUI() {
        list = (ListView) findViewById(R.id.friend_list);
    }

    private void setupViewAndGetData(ArrayList<String> friendSeriesItems) {
        if(series.isEmpty()){
            for(int i = 0; i < friendSeriesItems.size(); i++){
                sdp = new SeriesDataProvider();
                sdp.startSeriesFetching(this, friendSeriesItems.get(i), i);
            }
        }else{
            initAdapter();
        }
    }

    public void onSeriesDataReceived(SeriesItem seriesItem, Integer topListNumber) {
        series.add(seriesItem);
        initAdapter();
        new ImageDownloader(this, topListNumber).execute(series.get(topListNumber).getImgPath());
    }

    public void onSeriesNotFound(String searchQuery) {
        String toastMessage = "Error" + " '" + searchQuery + "'";
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onImageReceived(Bitmap Image, Integer topListNumber) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.out.println("IMAGEEEEEEEEEEEEEEE:"+Image+" TOPLISTNUMBERRRRRRRRRRR:"+topListNumber);
        Image.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        String encoded = Base64.encodeToString(bArray, Base64.DEFAULT);
        SeriesItem tempSeriesItem = series.get(topListNumber);
        tempSeriesItem.updateimgString(encoded);
        series.set(topListNumber, tempSeriesItem);
        initAdapter();
    }

    private void initAdapter() {
        CustomListAdapter customListAdapter = new CustomListAdapter(this, series, seriesNames);
        list.setAdapter(customListAdapter);
    }



    public void getData() {
        Intent intent = getIntent();
        selectedFriend = intent.getStringExtra("friend");
        seriesNames = intent.getStringArrayListExtra("series");
    }
}
