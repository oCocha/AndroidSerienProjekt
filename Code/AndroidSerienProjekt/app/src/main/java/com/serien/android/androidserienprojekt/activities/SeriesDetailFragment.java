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
import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.domain.SeriesItem;

import java.util.Arrays;
import java.util.List;

public class SeriesDetailFragment extends Fragment {


    private static Button deleteButton;
    private SeriesItem serItem;
    private static String USERNAME = UserActivity.getUserName();
    private String tempString;


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


    //Initialises a listener. Sets a listener on the delete button inside the view
    private void initListener() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateParseData();
                returnToPreviousActivity();
                showDeleteMessage();
            }
        });
    }


    //Update the Parse.com database. It deletes the name of a specific series inside the Parse.com database
    private void updateParseData() {
        String parseClassName = "SerienApp";
        ParseQuery<ParseObject> query = new ParseQuery<>(parseClassName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getString("userName").equals(USERNAME)) {
                            for (int j = 0; j < list.get(i).getJSONArray("series").length(); j++) {
                                try {
                                    tempString = list.get(i).getJSONArray("series").getString(j);
                                } catch (Exception g) {
                                }
                                if (tempString.equals(serItem.getName())) {
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


    //After the back button is pressed, the user returns to the previous activity
    private void returnToPreviousActivity() {
        SeriesOverviewActivity seriesOverviewActivity = (SeriesOverviewActivity) getActivity();
        seriesOverviewActivity.deleteSeries(serItem.getName());
        seriesOverviewActivity.onBackPressed();
    }


    //Shows a message that the specific series is removed from database
    private void showDeleteMessage() {
        String toastMessage = "'" + serItem.getName() + "' wurde aus der Liste entfernt!";
        Toast.makeText(getActivity(), toastMessage,Toast.LENGTH_SHORT).show();
    }


    //Initializes the UI
    private void initUI() {

        //Changes name of the Activity
        SeriesOverviewActivity ser = (SeriesOverviewActivity) getActivity();
        ser.setTitle("Serie - " + serItem.getName());


        TextView serName = (TextView) getView().findViewById(R.id.fragment_series_name);
        ImageView serImage = (ImageView) getView().findViewById(R.id.fragment_series_image);
        TextView serRating = (TextView) getView().findViewById(R.id.fragment_series_rating);
        TextView serYear = (TextView) getView().findViewById(R.id.fragment_series_year);
        TextView serActor = (TextView) getView().findViewById(R.id.fragment_series_actors);
        TextView serPlot = (TextView) getView().findViewById(R.id.fragment_series_description);
        deleteButton = (Button) getView().findViewById(R.id.fragment_delete_button);


        serName.setText(serItem.getName());
        serRating.setText(serItem.getActors());
        serYear.setText(serItem.getYear());
        serActor.setText(serItem.getRating());
        serPlot.setText(serItem.getPlot());
        String testBitString = serItem.getImgString();
        byte[] decodedByte = Base64.decode(testBitString, 0);
        Bitmap seriesBitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        serImage.setImageBitmap(seriesBitmap);
    }

}
