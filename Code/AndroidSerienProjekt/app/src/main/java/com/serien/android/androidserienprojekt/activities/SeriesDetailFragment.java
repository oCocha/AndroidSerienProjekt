package com.serien.android.androidserienprojekt.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.ImageDownloader;

public class SeriesDetailFragment extends Fragment {


    public static Button deleteButton;
    SeriesItem serItem;
    ImageDownloader.OnImageProvidedListener onImageProvidedListener;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        Intent i = getActivity().getIntent();

        serItem = (SeriesItem) i.getSerializableExtra("itemForFrag");

        return inflater.inflate(R.layout.activity_series_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initUI();
        initListener();
    }


    private void initListener() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SeriesOverviewActivity seriesOverviewActivity = (SeriesOverviewActivity) getActivity();
                seriesOverviewActivity.deleteSeries(serItem.getName());
                seriesOverviewActivity.onBackPressed();
            }
        });
    }


    static class ViewHolder{
        public ImageView serImage;
        public TextView serName;
        public TextView serRating;
        public TextView serYear;
        public TextView serActor;
        public TextView serPlot;
    }


    private void initUI() {

        //Change name of the Activity
        SeriesOverviewActivity ser = (SeriesOverviewActivity) getActivity();
        ser.setTitle("Serie - " + serItem.getName());


        //initialise the ViewHolder for the given Data
        ViewHolder viewHolder;
        viewHolder = new ViewHolder();

        viewHolder.serName = (TextView) getView().findViewById(R.id.fragment_series_name);
        viewHolder.serImage = (ImageView) getView().findViewById(R.id.fragment_series_image);
        viewHolder.serRating = (TextView) getView().findViewById(R.id.fragment_series_rating);
        viewHolder.serYear = (TextView) getView().findViewById(R.id.fragment_series_year);
        viewHolder.serActor = (TextView) getView().findViewById(R.id.fragment_series_actors);
        viewHolder.serPlot = (TextView) getView().findViewById(R.id.fragment_series_description);
        deleteButton = (Button) getView().findViewById(R.id.fragment_delete_button);


        viewHolder.serName.setText(serItem.getName());
        viewHolder.serRating.setText(serItem.getActors());
        viewHolder.serYear.setText(serItem.getYear());
        viewHolder.serActor.setText(serItem.getRating());
        viewHolder.serPlot.setText(serItem.getPlot());
        String testBitString = serItem.getImgString();
        byte[] decodedByte = Base64.decode(testBitString, 0);
        Bitmap seriesBitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        viewHolder.serImage.setImageBitmap(seriesBitmap);
    }
}
