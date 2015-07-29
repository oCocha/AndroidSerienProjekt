package com.serien.android.androidserienprojekt.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.activities.SearchActivity;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.ImageDownloader;

import java.util.ArrayList;

/**
 * Created by oCocha on 29.07.2015.
 */

//Adapter um ein Gridview mit Seriesitems aus einer Arraylist zu befüllen
public class CustomSeriesItemAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<SeriesItem> gridItems;

    public CustomSeriesItemAdapter(Context c,ArrayList<SeriesItem> griditems) {
        mContext = c;
        this.gridItems = griditems;
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

    // setzt für jedes Gridelement die dazugehörigen Unterelemente
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.activity_test, null);
            TextView nameTextView = (TextView) grid.findViewById(R.id.nameTextView);
            TextView yearTextView = (TextView) grid.findViewById(R.id.yearTextView);
            TextView actorsTextView = (TextView) grid.findViewById(R.id.actorsTextView);
            TextView ratingTextView = (TextView) grid.findViewById(R.id.ratingTextView);
            TextView plotTextView = (TextView) grid.findViewById(R.id.plotTextView);
            ImageView seriesImageView = (ImageView) grid.findViewById(R.id.seriesImageView);

            nameTextView.setText(gridItems.get(position).getName());
            yearTextView.setText(gridItems.get(position).getYear());
            actorsTextView.setText(gridItems.get(position).getActors());
            ratingTextView.setText(gridItems.get(position).getRating());
            plotTextView.setText(gridItems.get(position).getPlot());
            new ImageDownloader(seriesImageView).execute(gridItems.get(position).getImgPath());
        } else {
            grid = (View) convertView;
        }

        return grid;
    }

}
