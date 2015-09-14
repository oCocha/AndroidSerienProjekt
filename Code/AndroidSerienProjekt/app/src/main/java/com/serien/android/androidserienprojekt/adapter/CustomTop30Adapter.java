package com.serien.android.androidserienprojekt.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.ImageDownloader;

import java.util.ArrayList;

/**
 * Created by oCocha on 29.07.2015.
 */

//Adapter um ein Gridview mit Seriesitems aus einer Arraylist zu bef�llen
public class CustomTop30Adapter extends BaseAdapter implements ImageDownloader.OnImageProvidedListener{

    private Context mContext;
    private ArrayList<SeriesItem> gridItems;
    TextView nameTextView;
    TextView plotTextView;
    ImageView seriesImageView;

    public CustomTop30Adapter(Context c, ArrayList<SeriesItem> gridItems) {
        mContext = c;
        this.gridItems = gridItems;
    }

    public int getCount() {
        return gridItems.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // setzt f�r jedes Gridelement die dazugeh�rigen Unterelemente
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.activity_series_overview, null);
            nameTextView = (TextView) grid.findViewById(R.id.season_series_name);
//            TextView yearTextView = (TextView) grid.findViewById(R.id.yearTextView);
//            TextView actorsTextView = (TextView) grid.findViewById(R.id.actorsTextView);
//            TextView ratingTextView = (TextView) grid.findViewById(R.id.ratingTextView);
            plotTextView = (TextView) grid.findViewById(R.id.search_series_plot);
            seriesImageView = (ImageView) grid.findViewById(R.id.search_series_image);

            nameTextView.setText(gridItems.get(position).getName());
//            yearTextView.setText(gridItems.get(position).getYear());
//            actorsTextView.setText(gridItems.get(position).getActors());
//            ratingTextView.setText(gridItems.get(position).getRating());
            plotTextView.setText(gridItems.get(position).getPlot());

            if(gridItems.get(position).getImgString() != null) {
                String testBitString = gridItems.get(position).getImgString();
                System.out.println("BITTTTTTTTTTTTTTTTTTTTTTTTTTBILDDDDDDDDDDDDDDDDDDDDDDDD");
                byte[] decodedByte = Base64.decode(testBitString, 0);
                Bitmap testBitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                seriesImageView.setImageBitmap(testBitmap);
            }
            new ImageDownloader(/*seriesImageView, */this, null
            ).execute(gridItems.get(position).getImgPath());
        } else {
            grid = convertView;
        }

        return grid;
    }

    @Override
    public void onImageReceived(Bitmap Image, Integer integer) {
//        seriesImageView.setImageBitmap(Image);
    }
}
