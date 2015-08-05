package com.serien.android.androidserienprojekt.persistence;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by oCocha on 29.07.2015.
 */

//L�dt ein Bild mithilfe einer �bergebenen URL und setzt es in ein �bergebenes Imageview
public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
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
            System.out.println("Imagedownloader fehler");
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
