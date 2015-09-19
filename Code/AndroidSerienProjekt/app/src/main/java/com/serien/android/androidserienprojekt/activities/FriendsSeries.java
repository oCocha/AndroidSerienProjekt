package com.serien.android.androidserienprojekt.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.serien.android.androidserienprojekt.MainActivity;
import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.adapter.CustomListAdapter;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.ImageDownloader;
import com.serien.android.androidserienprojekt.persistence.SeriesDataProvider;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Igor on 16.09.2015.
 */
public class FriendsSeries extends AppCompatActivity implements SeriesDataProvider.OnSeriesDataProvidedListener,ImageDownloader.OnImageProvidedListener{
    SeriesDataProvider sdp;
    ListView list;
    private String selectedFriend = "";
    private ArrayList<String> seriesNames = new ArrayList<>();
    private String username = "Jürgen";
    private String parseClassName = "serienApp";


    ArrayList<String> friendSeriesItems = new ArrayList<>();
    ArrayList<SeriesItem> series = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        initUI();
        getData();
        //parse();
        setupViewAndGetData();
        System.out.println("USERRRRRRRRRRRRRRRRRRR:" + UserActivity.getUserName());
    }

    private void parse() {
        ParseQuery<ParseObject> query = new ParseQuery<>(parseClassName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(FriendsSeries.this, "Error " + e, Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        //friendSeriesItems.add(list.get(i).getString("seriesName"));

                        //  Eintrag löschen
                    /*    if(list.get(i).getString("seriesName").equals("Dexter")) {
                            try {
                                list.get(i).delete();
                            }catch(Exception f ){
                            }
                    */

                        //    list.add(tempParseObject);
                        //    System.out.println("ParseOBJEKTTTTTTTTTTTTTTTTTTT:"+tempParseObject);
                    }

                    //Eintrag updaten
                    //    list.get(i).put("userName", "Hans");
                    //    list.get(i).addAllUnique("series", Arrays.asList("flying", "kungfu"));
                    //    list.get(i).saveInBackground();
                    //    System.out.println("ParseObjektTTTTTTTTTtTTTTTT "+i+":"+list.get(i));
                }

                //    Eintrag einfügen
                //    ParseObject tempParseObject = new ParseObject("SerienApp");
                //    tempParseObject.put("userName", "Bob");
                //    tempParseObject.addAllUnique("series", Arrays.asList("Dexter", "The Mentalist"));
                //    tempParseObject.saveInBackground();
            }

            //    setupViewAndGetData(friendSeriesItems);
        });
    }

    private void initUI() {
        list = (ListView) findViewById(R.id.friend_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TextView tempTextView = (TextView) view.findViewById(R.id.series_title_row);
                //String seriesName = tempTextView.getText().toString();
                //SeriesItem tempItem = db.getSeriesItem(seriesName);
                startSeriesDetailIntent(position);
            }
        });
    }

    private void startSeriesDetailIntent(Integer position) {
        Intent seriesDetailIntent = new Intent(this, SeriesDetailActivity.class);
        System.out.println("Als nächstes: SeriesDetailActivity aufrufen mit:"+series.get(position));
    }

    private void setupViewAndGetData() {
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
        selectedFriend = intent.getStringExtra("username");
        friendSeriesItems = intent.getStringArrayListExtra("seriesList");
        seriesNames = intent.getStringArrayListExtra("series");
    }
}
