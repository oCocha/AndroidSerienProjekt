package com.serien.android.androidserienprojekt.persistence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.serien.android.androidserienprojekt.activities.SearchActivity;
import com.serien.android.androidserienprojekt.activities.TestActivity;
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
public class SeriesDataProvider {

    public final String urlBegin = "http://www.omdbapi.com/?t=";
    public final String urlEnd = "&y=&plot=short&r=json";
    public final String URL_KEY_NAME = "Title";
    public final String URL_KEY_YEAR = "Year";
    public final String URL_KEY_ACTORS = "Actors";
    public final String URL_KEY_RATING = "imdbRating";
    public final String URL_KEY_PLOT = "Plot";
    public final String URL_KEY_IMAGE = "Poster";
    SeriesItem tempSeriesItem;
    String searchURL = "";
    String tempImageURL;
    String tempName;
    String tempYear;
    String tempActors;
    String tempRating;
    String tempPlot;


    public void startSeriesFetching(String searchURL) {
        this.searchURL = searchURL.replace(" ", "+");;
        getSeriesData(searchURL);
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

        //gets the mensaDishes from the URL in the background
        protected JSONObject doInBackground(URL... urls) {
            HttpURLConnection urlConnection = null;
            JSONObject searchResultItems = null;
            String jSONResponse = processHttpRequest(urlBegin+searchURL+urlEnd);
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
                Log.e("My App", "Could not parse malformed JSON: \"" + searchResultItems + "\"");
            }
            return searchResultItems;
        }

        //after background computation has finished onMensaDishdataProviderListener is called
        protected void onPostExecute(JSONObject seriesSearchResult) {
            super.onPostExecute(seriesSearchResult);
            try {
                tempName = seriesSearchResult.getString(URL_KEY_NAME);
                tempYear = seriesSearchResult.getString(URL_KEY_YEAR);
                tempRating = seriesSearchResult.getString(URL_KEY_RATING);
                tempActors = seriesSearchResult.getString(URL_KEY_ACTORS);
                tempPlot = seriesSearchResult.getString(URL_KEY_PLOT);
                tempImageURL = seriesSearchResult.getString(URL_KEY_IMAGE);
                tempSeriesItem = new SeriesItem(tempName, tempYear, tempActors, tempRating, tempPlot, tempImageURL);
            } catch (Throwable t) {
                System.out.println("Spackt");
            }
            SearchActivity.nameTextView.setText(tempSeriesItem.getName());
            SearchActivity.actorsTextView.setText(tempSeriesItem.getActors());
            SearchActivity.ratingTextView.setText(tempSeriesItem.getRating());
            SearchActivity.yearTextView.setText(tempSeriesItem.getYear());
            SearchActivity.plotTextView.setText(tempSeriesItem.getPlot());
            new ImageDownloader(SearchActivity.seriesImageView).execute(tempSeriesItem.getImgPath());
            }
        }

    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
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
            new FetchSeries().execute(new URL(urlBegin + seriesName + urlEnd));
        } catch (MalformedURLException e) {
            System.out.println("Unable to create URL: " + e.toString());
        }
    }

}
