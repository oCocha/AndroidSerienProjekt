package com.serien.android.androidserienprojekt.persistence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by oCocha on 29.07.2015.
 */

//L�dt ein Bild mithilfe einer �bergebenen URL und setzt es in ein �bergebenes Imageview
public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private String imageUrl;

    public ImageDownloader(ImageView imageView) {
        imageViewReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        imageUrl = params[0];
        return LoadImage(imageUrl);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null){
            final ImageView imageView = imageViewReference.get();
            if(imageView != null){
                imageView.setImageBitmap(bitmap);
            }
        }
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
}

