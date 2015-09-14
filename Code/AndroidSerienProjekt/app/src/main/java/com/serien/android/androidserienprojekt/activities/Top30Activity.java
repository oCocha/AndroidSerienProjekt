package com.serien.android.androidserienprojekt.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.widget.ListView;
import android.widget.Toast;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.adapter.CustomListAdapter;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.ImageDownloader;
import com.serien.android.androidserienprojekt.persistence.SeriesDataProvider;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

//Dies ist die Top30Activity, in welcher beliebte/gut bewertete Serien per AsynTask geladen und angezeigt werden
public class Top30Activity extends ActionBarActivity implements SeriesDataProvider.OnSeriesDataProvidedListener,ImageDownloader.OnImageProvidedListener{
    public static final String NO_SERIES_DATA = "Gesuchte Serie wurde leider nicht gefunden";
    ArrayList<SeriesItem> top30SeriesItemList = new ArrayList<SeriesItem>();
    ArrayList<String> top30SeriesStringList = new ArrayList<String>();
    SeriesDataProvider sdp;
    ListView gridView;
    Toast seriesNotFoundToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top30);
        initUI();
        setupTop30List();
    }

    //Sobald ein SeriesDataProvider SeriesItems liefert werden sie in eine ArraList gespeichert und per Adapter angezeigt
    public void onSeriesDataReceived(SeriesItem seriesItem, Integer topListNumber) {
        top30SeriesItemList.add(seriesItem);
        initAdapter();
        new ImageDownloader(this, topListNumber).execute(top30SeriesItemList.get(topListNumber).getImgPath());
    }

    public void onSeriesNotFound(String searchQuery) {
        String toastMessage = NO_SERIES_DATA + " '" + searchQuery + "'";
        seriesNotFoundToast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    private void setupTop30List() {
        initTop30List();
        fetchTop30ListData();
    }

    //Hier wird die Top30 Liste per AsyncTask aus der OMDBAPI geladen
    private void fetchTop30ListData() {
//        top30SeriesItemList.clear();
        if(top30SeriesItemList.size() < 1){
            for(int i = 0; i < top30SeriesStringList.size(); i++) {
                sdp = new SeriesDataProvider();
                sdp.startSeriesFetching(this, top30SeriesStringList.get(i), i);
            }
        }else{
            initAdapter();
        }
    }

    //Hier wird die Top30 Liste probeweise manuell erstellt
    private void initTop30List() {
        top30SeriesStringList.add("Gotham");
        top30SeriesStringList.add("American Horror Story");
        top30SeriesStringList.add("Game of Thrones");
        top30SeriesStringList.add("Sons of Anarchy");
        top30SeriesStringList.add("The Walking Dead");
        top30SeriesStringList.add("Once Upon a Time");
        top30SeriesStringList.add("Outlander");
        top30SeriesStringList.add("The Blacklist");
        top30SeriesStringList.add("The Big Bang Theory");
        top30SeriesStringList.add("Scorpion");
    }

    //Hier wird ein Gridviewadapter erstellt und mit dem Gridview verkn�pft
    private void initAdapter() {
        CustomListAdapter customListAdapter = new CustomListAdapter(this, top30SeriesItemList);
        gridView.setAdapter(customListAdapter);
    }

    //Hier werden die UI Elemente erstellt
    private void initUI() {
        gridView = (ListView) findViewById(R.id.series_top);
    }

    @Override
    public void onImageReceived(Bitmap Image, Integer topListNumber) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.out.println("IMAGEEEEEEEEEEEEEEE:"+Image+" TOPLISTNUMBERRRRRRRRRRR:"+topListNumber);
        Image.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        String encoded = Base64.encodeToString(bArray, Base64.DEFAULT);
        SeriesItem tempSeriesItem = top30SeriesItemList.get(topListNumber);
        tempSeriesItem.updateimgString(encoded);
        top30SeriesItemList.set(topListNumber, tempSeriesItem);
        initAdapter();
    }



    /*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top30, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.series_Search) {
            Intent startSearchActivity = new Intent(this, SearchActivity.class);
            startActivity(startSearchActivity);
//            Top30Activity.this.finish();
            return true;
        }else if (id == R.id.series_Main) {
            Intent startMainActivity = new Intent(this, ListActivity.class);
            startActivity(startMainActivity);
//            Top30Activity.this.finish();
            return true;
        }else if (id == R.id.series_Friend) {
            Intent startFriendsActivity = new Intent(this, FriendsActivity.class);
            startActivity(startFriendsActivity);
//            Top30Activity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    */

}
