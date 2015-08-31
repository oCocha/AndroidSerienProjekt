package com.serien.android.androidserienprojekt.adapter;

import android.app.Activity;
import android.content.Context;
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
 * Created by Igor on 31.08.2015.
 */
public class CustomListAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<SeriesItem> seriesItems;

    public CustomListAdapter(Activity activity, ArrayList<SeriesItem> series){
        this.activity = activity;
        seriesItems = series;
    }

    @Override
    public int getCount() {
        return seriesItems.size();
    }

    @Override
    public Object getItem(int position) {
        return seriesItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = inflater.inflate(R.layout.activity_list_row, null);

            ImageView seriesImage = (ImageView) convertView.findViewById(R.id.series_thumbnail);
            TextView seriesName = (TextView) convertView.findViewById(R.id.series_title);
            TextView seriesActors = (TextView) convertView.findViewById(R.id.series_actors);
            TextView seriesRating = (TextView) convertView.findViewById(R.id.series_rating);
            TextView seriesYear = (TextView) convertView.findViewById(R.id.series_releaseYear);

            SeriesItem item = seriesItems.get(position);

            new ImageDownloader(seriesImage).execute(item.getImgPath());
            seriesName.setText(item.getName());
            //falsches Fetching der beiden unteres Sachen
            seriesActors.setText(item.getRating());
            seriesRating.setText("Rating: " + item.getActors());

            seriesYear.setText(item.getYear());
        }
        return convertView;
    }
}
