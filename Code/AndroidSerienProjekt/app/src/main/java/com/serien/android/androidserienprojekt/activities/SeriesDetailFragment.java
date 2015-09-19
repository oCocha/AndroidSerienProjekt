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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.serien.android.androidserienprojekt.MainActivity;
import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.ImageDownloader;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SeriesDetailFragment extends Fragment {


    private static Button deleteButton;
    SeriesItem serItem;
    ImageDownloader.OnImageProvidedListener onImageProvidedListener;
    private String parseClassName = "SerienApp";
    private static String USERNAME = UserActivity.getUserName();
    String tempString;


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
                updateParseData();
                SeriesOverviewActivity seriesOverviewActivity = (SeriesOverviewActivity) getActivity();
                seriesOverviewActivity.deleteSeries(serItem.getName());
                seriesOverviewActivity.onBackPressed();
                showText();
            }
        });
    }

    private void updateParseData() {
        ParseQuery<ParseObject> query = new ParseQuery<>(parseClassName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                //    Toast.makeText(SeriesDetailFragment.this, "Error " + e, Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getString("userName").equals(USERNAME)) {
                            System.out.println("ARAYYYYYYYYYYYYYYYYYYYYYY:"+list.get(i).getJSONArray("series"));
                            for(int j = 0; j < list.get(i).getJSONArray("series").length(); j++) {
                                try {
                                    tempString = list.get(i).getJSONArray("series").getString(j);
                                }catch (Exception g){
                                }
                                if(tempString.equals(serItem.getName())) {
                                    System.out.println("ZULÖSCHENDESERIEEEEEEEEEE:"+tempString);
                                    list.get(i).removeAll("series", Arrays.asList(serItem.getName()));
                                    list.get(i).saveInBackground();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void showText() {
        String toastMessage = "'" + serItem.getName() + "' wurde aus der Liste entfernt!";
        Toast.makeText(getActivity(), toastMessage,Toast.LENGTH_SHORT).show();
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
