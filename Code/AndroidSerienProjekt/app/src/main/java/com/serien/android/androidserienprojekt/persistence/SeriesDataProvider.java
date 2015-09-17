package com.serien.android.androidserienprojekt.persistence;

import android.os.AsyncTask;
import android.util.Log;

import com.serien.android.androidserienprojekt.domain.SeriesItem;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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

/**
 * Created by oCocha on 27.07.2015.
 */

//L�dt die Daten zu einer gesuchten Serie und zeigt sie in der Searchactivity an(SCHLECHT -> CALLBACK einbauen)
public class SeriesDataProvider {

    public final String OMDB_API_URL_BEGIN = "http://www.omdbapi.com/?t=";
    public final String OMDB_API_URL_END = "&y=&plot=short&r=json";
    public final String OMDB_URL_KEY_NAME = "Title";
    public final String OMDB_URL_KEY_YEAR = "Year";
    public final String OMDB_URL_KEY_ACTORS = "Actors";
    public final String OMDB_URL_KEY_RATING = "imdbRating";
    public final String OMDB_URL_KEY_PLOT = "Plot";
    public final String OMDB_URL_KEY_IMAGE = "Poster";
    public final String OMDB_URL_KEY_RESPONSE = "Response";
    public final String OMDB_URL_KEY_IMDBID = "imdbID";
    private OnSeriesDataProvidedListener onSeriesDataProvidedListener;
    SeriesItem seriesData;
    String searchQuery;
    String searchURL = "";
    String tempImageURL;
    String tempName;
    String tempYear;
    String tempActors;
    String tempRating;
    String tempPlot;
    String tempImdbID;
    String responseCheck;
    String tempWatched = null;
    Integer topListNumber;

    //wandelt den �bergebenen String in eine brauchbare SearchQuery um und �bergibt sie weiter
    public void startSeriesFetching(OnSeriesDataProvidedListener onSeriesDataProvidedListener, String searchQuery, Integer topListNumber) {
        this.onSeriesDataProvidedListener = onSeriesDataProvidedListener;
        this.searchQuery = searchQuery;
        this.topListNumber = topListNumber;
        searchURL = searchQuery.replace(" ", "+");;
        getSeriesData(searchURL);
    }

    //Klasse um ein JSONObject von einer �bergebenen URL zu laden
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

        //l�dt im Hintergrund die Daten
        protected JSONObject doInBackground(URL... urls) {
            HttpURLConnection urlConnection = null;
            JSONObject searchResultItems = null;
            String jSONResponse = processHttpRequest(OMDB_API_URL_BEGIN+searchURL+OMDB_API_URL_END);
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

        //nach dem Laden des JSONObjects werden die Daten weiterverarbeitet
        protected void onPostExecute(JSONObject seriesSearchResult) {
            super.onPostExecute(seriesSearchResult);
            try {
                tempName = seriesSearchResult.getString(OMDB_URL_KEY_NAME);
                tempYear = seriesSearchResult.getString(OMDB_URL_KEY_YEAR);
                tempRating = seriesSearchResult.getString(OMDB_URL_KEY_RATING);
                tempActors = seriesSearchResult.getString(OMDB_URL_KEY_ACTORS);
                tempPlot = seriesSearchResult.getString(OMDB_URL_KEY_PLOT);
                tempImageURL = seriesSearchResult.getString(OMDB_URL_KEY_IMAGE);
                tempImdbID = seriesSearchResult.getString(OMDB_URL_KEY_IMDBID);
                responseCheck = seriesSearchResult.getString(OMDB_URL_KEY_RESPONSE);
            } catch (Throwable t) {
                System.out.println("Spackt"+t);
            }
            try {
                responseCheck = seriesSearchResult.getString(OMDB_URL_KEY_RESPONSE);
            } catch (Throwable t) {
                System.out.println("Spackt 2");
            }
            if(responseCheck.equals("False")) {
                onSeriesDataProvidedListener.onSeriesNotFound(searchQuery);
            }else {
                seriesData = new SeriesItem(tempName, tempYear, tempRating, tempActors, tempPlot, tempImageURL, tempImdbID, tempWatched, null);
                onSeriesDataProvidedListener.onSeriesDataReceived(seriesData, topListNumber);
            }
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
            new FetchSeries().execute(new URL(OMDB_API_URL_BEGIN + seriesName + OMDB_API_URL_END));
        } catch (MalformedURLException e) {
            System.out.println("Unable to create URL: " + e.toString());
        }
    }

    //Interface f�r das Bereitstellen der Seriesitems
    public interface OnSeriesDataProvidedListener {
        void onSeriesDataReceived(SeriesItem seriesData, Integer topListNumber);
        void onSeriesNotFound(String searchQuery);
    }

}
