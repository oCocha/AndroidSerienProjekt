package com.serien.android.androidserienprojekt.persistence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.serien.android.androidserienprojekt.domain.SeriesItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by oCocha on 29.07.2015.
 */

//Loads an image from an URL and returns it as a bitmap
public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    private String imageUrl;
    OnImageProvidedListener onImageProvidedListener;
    Integer topListNumber;

    public ImageDownloader(OnImageProvidedListener onImageProvidedListener, Integer topListNumber) {
        this.onImageProvidedListener = onImageProvidedListener;
        this.topListNumber = topListNumber;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        imageUrl = params[0];
        return LoadImage(imageUrl);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        onImageProvidedListener.onImageReceived(bitmap, topListNumber);
    }

    private Bitmap LoadImage(String url) {
        Bitmap bm = null;
        InputStream in;
    try {
        in = OpenHttpConnection(url);
        bm = BitmapFactory.decodeStream(in);
    } catch (IOException e1) {
    }
    return bm;
    }

    private InputStream OpenHttpConnection(String strURL) throws IOException {
            InputStream inputStream = null;
            URL url = new URL(strURL);
            URLConnection conn = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        } catch (Exception ex) {
        }
        return inputStream;
    }

    //Callback for when the image is received
    public interface OnImageProvidedListener {
        void onImageReceived(Bitmap Image, Integer topListNumber);
    }
}

