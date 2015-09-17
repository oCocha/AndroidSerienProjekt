package com.serien.android.androidserienprojekt.adapter;

import android.app.Activity;
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
 * Created by Igor on 31.08.2015.
 */
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<SeriesItem> seriesItems;
    private ArrayList<String> serNames;
    ImageDownloader.OnImageProvidedListener onImageProvidedListener;

    public CustomListAdapter(Activity activity, ArrayList<SeriesItem> series, ArrayList<String> seriesNames) {
        this.activity = activity;
        seriesItems = series;
        serNames = seriesNames;
    }



    static class ViewHolder {
        public ImageView seriesImage;
        public TextView seriesName;
        public TextView seriesActors;
        public TextView seriesRating;
        public TextView seriesYear;
        public ImageView inList;
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
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_list_row, null);

            viewHolder = new ViewHolder();
            viewHolder.seriesImage = (ImageView) convertView.findViewById(R.id.series_thumbnail_row);
            viewHolder.seriesName = (TextView) convertView.findViewById(R.id.series_title_row);
            viewHolder.seriesActors = (TextView) convertView.findViewById(R.id.series_actors_row);
            viewHolder.seriesRating = (TextView) convertView.findViewById(R.id.series_rating_row);
            viewHolder.seriesYear = (TextView) convertView.findViewById(R.id.series_releaseYear_row);
            viewHolder.inList = (ImageView) convertView.findViewById(R.id.series_in_list);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SeriesItem item = seriesItems.get(position);

        viewHolder.seriesName.setText(item.getName());
        //Actors und Ratings sind vertauscht, fehler beim fetching
        viewHolder.seriesActors.setText(item.getRating());
        viewHolder.seriesRating.setText(item.getActors());
        viewHolder.seriesYear.setText(item.getYear());

        if(serNames.contains(item.getName())){
            viewHolder.inList.setImageResource(R.mipmap.liste);
            viewHolder.inList.setVisibility(View.VISIBLE);
        }else{
            viewHolder.inList.setVisibility(View.GONE);
        }

        if(viewHolder.seriesImage != null){
//            new ImageDownloader(viewHolder.seriesImage, onImageProvidedListener).execute(item.getImgPath());
            if(item.getImgString() != null) {
                String testBitString = item.getImgString();
                byte[] decodedByte = Base64.decode(testBitString, 0);
                Bitmap testBitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                viewHolder.seriesImage.setImageBitmap(testBitmap);
            }
        }
        return convertView;
    }
}
