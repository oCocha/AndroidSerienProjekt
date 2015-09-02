package com.serien.android.androidserienprojekt.activities;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.adapter.CustomSeriesExpandableListAdapter;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//Dies ist die SeriesSeasonActivity, in welcher Serien mitsamt aller Staffeln und Episoden angezeigt werden und mithilfe einer Checkbox markiert werden können
public class SeriesSeasonActivity extends AppCompatActivity {
    List<String> seasonList;
    List<String> episodeList;
    Map<String, List<String>> seriesCollection;
    ExpandableListView expListView;
    boolean findId=true;
    TextView seriesNameTextView;
    Intent intent;
    String seriesName;
    int seasonCounter=0;
    int seasonId=1;
    String endURL="/0/100/all/all";
    String testId="8523";
    String guideBoxBase ="http://api-public.guidebox.com/v1.43/US/tpYo46LVz6OxRTeMTyJXnMIOivu3QU/show/";
    boolean episodes = false;
    String guideBoxEpisodes = "/episodes/";
    int testSession = 1;
    String guideBoxEnd ="/0/1/all/all";
    String totalResults="";
    public final String API_TOTAL_RESULTS ="total_results";
    public final String API_ID="id";
    public final String API_FIRST_AIRED="first_aired";
    public final String API_TITLE="title";
    public final String API_ALTERNATE_TITLE="alternate_title";
    public final String API_DURATION="duration";
    public final String API_RESULTS ="results";
    ArrayList<Integer> totalResultsInt = new ArrayList<Integer>();
    ArrayList<ArrayList<String>> titleList = new ArrayList<ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_season);
        intent = getIntent();
        seriesName = intent.getStringExtra("SeriesName");
        convertId(seriesName);
        setupTestData();
        initUI();
    }

    private void convertId(String imdb) {
        try {
            new FetchSeries().execute(new URL("https://api-public.guidebox.com/v1.43/US/tpYo46LVz6OxRTeMTyJXnMIOivu3QU/search/id/imdb/"+imdb));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //erstellt einen ExpandableListViewAdapter und verknüpft diesen mit der erstellten ExpandableList
    private void initAdapter() {
        final CustomSeriesExpandableListAdapter expListAdapter = new CustomSeriesExpandableListAdapter(this, seasonList, seriesCollection);
        expListView.setAdapter(expListAdapter);
    }
    //erstellt die UI Elemente
    private void initUI() {
        expListView = (ExpandableListView) findViewById(R.id.series_expandableList);
        seriesNameTextView = (TextView) findViewById(R.id.nameTextView);
        seriesNameTextView.setText(seriesName);
    }
    //erstellt einen Testseriendatensatz
    private void setupTestData() {
        seriesName = intent.getStringExtra("SeriesName");
        setupSeasonList();
    }
    //sets up the list with all sessions and episodes of a series
    private void setupCollection(ArrayList <ArrayList<String>> title, ArrayList<Integer> totalResultsInt) {
        ArrayList<ArrayList<String>> seasons = new ArrayList<ArrayList<String>>();
        ArrayList<String> season = new ArrayList<String>();
        for(int j=0;j<seasonCounter;j++) {
            for (int i = 0; i < totalResultsInt.get(j); i++) {
                season.add("Episode " +(i+1)+ "  " + title.get(j).get(i));
            }
            seasons.add(j, season);
            season= new ArrayList<String>();
        }
        seriesCollection = new LinkedHashMap<String, List<String>>();
        int c=0;
        for (String compareString : seasonList) {
            c++;
            if (compareString.equals("Staffel "+ c)) {
                loadChild(seasons.get(c - 1));
            }
            seriesCollection.put(compareString, episodeList);
            initAdapter();
        }
    }
    //loads all episodes of a season in the list
    private void loadChild(ArrayList<String> s) {
        episodeList = new ArrayList<String>();
        for (String model : s)
            episodeList.add(model);
    }
    private void setupSeasonList() {
        seasonList = new ArrayList<String>();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_series_season, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class FetchSeries extends AsyncTask<URL, Integer, JSONObject> {
        public String processHttpRequest(String url) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(url));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else{
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (Exception e) {
            }
            return responseString;
        }
        //ldt im Hintergrund die Daten
        protected JSONObject doInBackground(URL... urls) {
            HttpURLConnection urlConnection = null;
            JSONObject searchResultItems = null;
            String jSONResponse = processHttpRequest(guideBoxBase + testId + guideBoxEpisodes + testSession + guideBoxEnd);
            if (jSONResponse != null) {
                try {
                    urlConnection = (HttpURLConnection) urls[0].openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    jSONResponse = readSearchResultInputStream(in);
                } catch (IOException e) {
                    System.out.println("IO exception when trying to open URL connection:" + e.toString());
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            try {
                searchResultItems = new JSONObject(jSONResponse);
                Log.d("My App", searchResultItems.toString());
            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON/No JSON Response" + t);
            }
            return searchResultItems;
        }
        //after having loaded the JSONObject it is used to get the info in it
        protected void onPostExecute(JSONObject seriesSearchResult) {
            super.onPostExecute(seriesSearchResult);
            //gets called once at the beginning to get the guideboxId from the imdbId
            if(findId==true) {
                if (seriesSearchResult.has("id")) {
                    try {
                        String guideboxId = seriesSearchResult.getString("id");
                        String guideboxName = seriesSearchResult.getString("title");
                        seriesNameTextView = (TextView) findViewById(R.id.nameTextView);
                        seriesNameTextView.setText(guideboxName);
                        findId = false;
                        testId = guideboxId;
                        getSeriesData(guideboxId);
                    } catch (JSONException e) {
                        System.out.println("Spackt " + e);
                        e.printStackTrace();
                    }
                }

            }else {
                //gets called once to get the number of seasons of a series
                if(episodes==false) {
                    try {
                        JSONArray result = seriesSearchResult.getJSONArray(API_RESULTS);
                        seasonCounter=result.length();
                        setupSeasons(result.length());
                    } catch (JSONException e) {
                        System.out.println("Spackt " + e);
                    }
                } else {
                    //gets called once for each season in a series
                    try {
                        if (seriesSearchResult.has(API_TOTAL_RESULTS)) {
                            totalResults = seriesSearchResult.getString(API_TOTAL_RESULTS);
                        }
                        totalResultsInt.add(Integer.parseInt(totalResults));
                        JSONArray result = seriesSearchResult.getJSONArray(API_RESULTS);
                        ArrayList<String> titles = new ArrayList<String>();
                        ArrayList<String> epis = new ArrayList<String>();
                        for(int i=0;i<totalResultsInt.get(seasonId-1);i++) {
                            Object title = result.get(totalResultsInt.get(seasonId-1)-1-i);
                            JSONObject o = new JSONObject();
                            o.put("a", title);
                            JSONObject o2 = o.getJSONObject("a");
                            titles.add(o2.getString(API_TITLE));
                            epis.add(o2.getString("episode_number"));
                        }
                        titleList.add(seasonId-1,titles);
                        seasonId++;
                        if(seasonId==seasonCounter+1) {
                            setupCollection(titleList, totalResultsInt);
                        }
                    } catch (JSONException e) {
                        System.out.println("Spackt " + e);
                    }
                }
            }
        }
    }

    private void setupSeasons(int length) {
        for(int i=1;i<=length;i++){
            seasonList.add("Staffel " + i);
        }
        episodes =true;
        try {
            for(int i=1;i<seasonCounter+1;i++) {
                new FetchSeries().execute(new URL("https://api-public.guidebox.com/v1.43/US/tpYo46LVz6OxRTeMTyJXnMIOivu3QU/show/"+ testId+"/episodes/" + i + endURL));
            }
        } catch (MalformedURLException e) {
            System.out.println("Unable to create URL: " + e.toString());
        }
    }

    //returns the response
    private String readSearchResultInputStream(InputStream in) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String response = "";
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }
        } catch (IOException e) {
            System.out.println("Exception reading response: " + e.toString());
        }
        return response;
    }
    //starts the FetchSeries class with the given name of the series
    public void getSeriesData(String seriesName) {
        try {
            new FetchSeries().execute(new URL("https://api-public.guidebox.com/v1.43/US/tpYo46LVz6OxRTeMTyJXnMIOivu3QU/show/"+ testId+"/seasons"));
        } catch (MalformedURLException e) {
            System.out.println("Unable to create URL: " + e.toString());
        }
    }
}
