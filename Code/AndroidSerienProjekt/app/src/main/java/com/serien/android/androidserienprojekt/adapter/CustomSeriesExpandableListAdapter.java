package com.serien.android.androidserienprojekt.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.activities.SeriesSeasonActivity;
import com.serien.android.androidserienprojekt.domain.SeriesItem;
import com.serien.android.androidserienprojekt.persistence.SeriesRepository;
import com.thoughtworks.xstream.XStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by oCocha on 03.08.2015.
 */


public class CustomSeriesExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> seriesCollections;
    private ArrayList<ArrayList<Integer>> seasonsWatchedTemp;
    private List<String> seasons;
    private SeriesItem seriesItem;
    private XStream xStream;
    private String xmlSeasonsWatched;
    OnWatchedEpisodesChangedListener onWatchedEpisodesChangedListener;


    public CustomSeriesExpandableListAdapter(Activity context, List<String> seasons,
                                 Map<String, List<String>> seriesCollections, ArrayList<ArrayList<Integer>> seasonsWatchedTemp, SeriesItem seriesItem, OnWatchedEpisodesChangedListener onWatchedEpisodesChangedListener) {
        this.context = context;
        this.seriesCollections = seriesCollections;
        this.seasons = seasons;
        this.seasonsWatchedTemp = seasonsWatchedTemp;
        this.seriesItem = seriesItem;
        this.onWatchedEpisodesChangedListener = onWatchedEpisodesChangedListener;
        xStream = new XStream();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return seriesCollections.get(seasons.get(groupPosition)).get(childPosition);
    }

    public Object getChildWatched(int groupPosition, int childPosition) {
        return seasonsWatchedTemp.get(groupPosition).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String episodeName = (String) getChild(groupPosition, childPosition);
        final Integer episodesWatched = (Integer) getChildWatched(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.episode_item, null);
        }

        TextView episodeTextView = (TextView) convertView.findViewById(R.id.episode_series_name);
        CheckBox episodeCheckBox = (CheckBox) convertView.findViewById(R.id.episode_series_checkBox);

        episodeCheckBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ArrayList<Integer> tempArrayList = seasonsWatchedTemp.get(groupPosition);
                if(tempArrayList.get(childPosition) == 1){
                    tempArrayList.set(childPosition, 0);
                }else{
                    tempArrayList.set(childPosition, 1);
                }
                seasonsWatchedTemp.set(groupPosition, tempArrayList);
                xmlSeasonsWatched = xStream.toXML(seasonsWatchedTemp);
                onWatchedEpisodesChangedListener.onWatchedEpisodesChanged(seriesItem.getName(), xmlSeasonsWatched);
            }
        });

        if(episodesWatched == 1) {
            episodeCheckBox.setChecked(true);
        }else{
            episodeCheckBox.setChecked(false);
        }
        episodeTextView.setText(episodeName);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return seriesCollections.get(seasons.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return seasons.get(groupPosition);
    }

    public int getGroupCount() {
        return seasons.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String seasonName = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.season_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.season_series_season_name);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(seasonName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface OnWatchedEpisodesChangedListener {
        void onWatchedEpisodesChanged(String seriesName, String seasonsWatched);
    }

}
