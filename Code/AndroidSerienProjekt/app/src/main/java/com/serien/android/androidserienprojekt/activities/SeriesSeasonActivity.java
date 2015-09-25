package com.serien.android.androidserienprojekt.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.adapter.CustomSeriesExpandableListAdapter;
import com.serien.android.androidserienprojekt.persistence.SeriesRepository;
import com.thoughtworks.xstream.XStream;

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
public class SeriesSeasonActivity extends Fragment implements CustomSeriesExpandableListAdapter.OnWatchedEpisodesChangedListener{
    List<String> seasonList;
    List<String> episodeList;
    List<Integer> episodeListWatched;
    ArrayList<ArrayList<Integer>> seasonsWatchedTemp;
    Map<String, List<String>> seriesCollection;
    Map<String, List<Integer>> seriesCollectionWatched;
    private SeriesRepository db;
    ExpandableListView expListView;
    boolean findId=true;
    TextView seriesNameTextView;
    String seriesID;
    int seasonCounter=0;
    int seasonId=1;
    String endURL="/0/100/all/all";
    String testId="8523";
    String guideBoxBase ="http://api-public.guidebox.com/v1.43/US/rKLFGBQmcGcVIW3lgmk2KpXr1tbEq7b7/show/";
    boolean episodes = false;
    String guideBoxEpisodes = "/episodes/";
    int testSession = 1;
    String guideBoxEnd ="/0/1/all/all";
    String totalResults="";
    String guideboxName;
    public final String API_TOTAL_RESULTS ="total_results";
    public final String API_TITLE="title";
    public final String API_RESULTS ="results";
    ArrayList<Integer> totalResultsInt = new ArrayList<>();
    ArrayList<ArrayList<String>> titleList = new ArrayList<>();
    SeriesOverviewActivity overView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancesState){
        return inflater.inflate(R.layout.activity_series_season, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupSeriesName();
        initDB();
        convertId(seriesID);
        initUI();
    }

    //Gets the ImdbID of the clicked tv serial
    private void setupSeriesName() {
        overView = (SeriesOverviewActivity) getActivity();
        seriesID = overView.getSeriesID();
        seasonList = new ArrayList<>();
    }

    //Initializes the database
    private void initDB() {
        db = new SeriesRepository(overView);
        db.open();
    }

    //Creates the URL for a specific tv serial and fetches the data
    private void convertId(String imdb) {
        try {
            new FetchSeries().execute(new URL("https://api-public.guidebox.com/v1.43/US/rKLFGBQmcGcVIW3lgmk2KpXr1tbEq7b7/search/id/imdb/"+imdb));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //Initializes the UI elements
    private void initUI() {
        expListView = (ExpandableListView) getView().findViewById(R.id.series_expandableList);
        seriesNameTextView = (TextView) getView().findViewById(R.id.season_series_name);
        seriesNameTextView.setText(seriesID);
    }

    //This class fetches all seasons and episodes of a tv serial
    private class FetchSeries extends AsyncTask<URL, Integer, JSONObject> {

        public String processHttpRequest(String url) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;

            try {
                response = httpclient.execute(new HttpGet(url));
                StatusLine statusLine = response.getStatusLine();

                //Checks whether the parent activity is still active
                if(!overView.getActivityStatus()){
                    response.getEntity().getContent().close();
                } else if(statusLine.getStatusCode() == HttpStatus.SC_OK){
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

        //Loads the tv serial data from the GuideBox API
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

            //Creates a new JSONObject from the received responsestring
            try {
                searchResultItems = new JSONObject(jSONResponse);
            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON/No JSON Response" + t);
            }
            return searchResultItems;
        }

        //Saves the data from the new JSONObject
        protected void onPostExecute(JSONObject seriesSearchResult) {
            super.onPostExecute(seriesSearchResult);
            if(seriesSearchResult != null) {
                //Gets called once at the beginning to get the guideboxId from the imdbId
                if (findId) {
                    if (seriesSearchResult.has("id")) {
                        try {
                            String guideboxId = seriesSearchResult.getString("id");
                            guideboxName = seriesSearchResult.getString("title");
                            seriesNameTextView.setText(guideboxName);
                            findId = false;
                            testId = guideboxId;
                            getSeriesData(guideboxId);
                        } catch (JSONException e) {
                            System.out.println("Error " + e);
                            e.printStackTrace();
                        }
                    }

                } else {
                    //Gets called once to get the number of seasons of a tv serial
                    if (!episodes) {
                        try {
                            JSONArray result = seriesSearchResult.getJSONArray(API_RESULTS);
                            seasonCounter = result.length();
                            setupSeasons(result.length());
                        } catch (JSONException e) {
                            System.out.println("Error " + e);
                        }
                    } else {
                        //Gets called once for each season in a tv serial
                        try {
                            if (seriesSearchResult.has(API_TOTAL_RESULTS)) {
                                totalResults = seriesSearchResult.getString(API_TOTAL_RESULTS);
                            }
                            totalResultsInt.add(Integer.parseInt(totalResults));
                            JSONArray result = seriesSearchResult.getJSONArray(API_RESULTS);
                            ArrayList<String> titles = new ArrayList<>();
                            ArrayList<String> epis = new ArrayList<>();
                            for (int i = 0; i < totalResultsInt.get(seasonId - 1); i++) {
                                Object title = result.get(totalResultsInt.get(seasonId - 1) - 1 - i);
                                JSONObject o = new JSONObject();
                                o.put("a", title);
                                JSONObject o2 = o.getJSONObject("a");
                                titles.add(o2.getString(API_TITLE));
                                epis.add(o2.getString("episode_number"));
                            }
                            titleList.add(seasonId - 1, titles);
                            seasonId++;
                            if (seasonId == seasonCounter + 1) {
                                setupCollection(titleList, totalResultsInt);
                            }
                        } catch (JSONException e) {
                            System.out.println("Error " + e);
                        }
                    }
                }
            }
        }
    }

    //Saves the seasons in a list and fetches the episodes data
    private void setupSeasons(int length) {
        for(int i=1;i<=length;i++){
            seasonList.add("Staffel " + i);
        }
        episodes =true;
        try {
            for(int i=1;i<seasonCounter+1;i++) {
                new FetchSeries().execute(new URL("https://api-public.guidebox.com/v1.43/US/rKLFGBQmcGcVIW3lgmk2KpXr1tbEq7b7/show/"+ testId+"/episodes/" + i + endURL));
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

    //Starts the FetchSeries class with the given name of the series
    public void getSeriesData(String seriesName) {
        try {
            new FetchSeries().execute(new URL("https://api-public.guidebox.com/v1.43/US/rKLFGBQmcGcVIW3lgmk2KpXr1tbEq7b7/show/"+ testId+"/seasons"));
        } catch (MalformedURLException e) {
            System.out.println("Unable to create URL: " + e.toString());
        }
    }


    //Sets up the list with all sessions and episodes of a tv seial and marks the watched episodes
    private void setupCollection(ArrayList <ArrayList<String>> title, ArrayList<Integer> totalResultsInt) {

        ArrayList<ArrayList<String>> seasons = new ArrayList<>();
        ArrayList<String> season = new ArrayList<>();
        XStream xStream = new XStream();

        //Creates a list of watched episodes if nothing is saved in the database already
        if(db.getSeriesItem(guideboxName).getWatched() == null) {

            seasonsWatchedTemp = new ArrayList<>();
            ArrayList<Integer> seasonEpisodesWatched = new ArrayList<>();
            Integer tempInt = 0;

            for(int i = 0; i < totalResultsInt.size(); i++) {
                seasonEpisodesWatched = new ArrayList<>();
                for(int j = 0; j < totalResultsInt.get(i); j++) {
                    seasonEpisodesWatched.add(tempInt);
                }
                seasonsWatchedTemp.add(seasonEpisodesWatched);
            }

        //Loads a list of watched episodes if this list is saved in the database already
        }else{
            seasonsWatchedTemp = new ArrayList<>();
            seasonsWatchedTemp = (ArrayList<ArrayList<Integer>>) xStream.fromXML(db.getSeriesItem(guideboxName).getWatched());
        }

        //Creates a list of epiosdes and a list of watched/nonwatched episodes and creates a new adapter with those lists
        for(int j=0;j<seasonCounter;j++) {
            for (int i = 0; i < totalResultsInt.get(j); i++) {
                season.add("Episode " +(i+1)+ ":  " + title.get(j).get(i));
            }
            seasons.add(j, season);
            season= new ArrayList<>();
        }
        seriesCollection = new LinkedHashMap<>();
        seriesCollectionWatched = new LinkedHashMap<>();
        int c=0;
        for (String compareString : seasonList) {
            c++;
            if (compareString.equals("Staffel "+ c)) {
                loadChild(seasons.get(c - 1));
                loadChildWatched(seasonsWatchedTemp.get(c - 1));
            }
            seriesCollection.put(compareString, episodeList);
            seriesCollectionWatched.put(compareString, episodeListWatched);
            initAdapter();
        }
    }

    //loads all episodes of a season in the list
    private void loadChild(ArrayList<String> s) {
        episodeList = new ArrayList<>();
        for (String model : s)
            episodeList.add(model);
    }

    //loads all watched episodes of a season in the list
    private void loadChildWatched(ArrayList<Integer> s) {
        episodeListWatched = new ArrayList<>();
        for (Integer model : s)
            episodeListWatched.add(model);
    }

    //Creates a new expandableListAdapter and links it with the expandableList
    private void initAdapter() {
        CustomSeriesExpandableListAdapter expListAdapter = new CustomSeriesExpandableListAdapter(getActivity(), seasonList, seriesCollection, seasonsWatchedTemp, db.getSeriesItem(guideboxName), this);
        expListView.setAdapter(expListAdapter);
    }

    //Updates the list of watched episodes in the database if the user marks an episode as watched/nonwatched
    public void onWatchedEpisodesChanged(String seriesName, String seasonsWatched) {
        db.updateWatchedEpisodes(seriesName, seasonsWatched);
    }

}
