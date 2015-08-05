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

import java.util.List;
import java.util.Map;

/**
 * Created by oCocha on 03.08.2015.
 */


public class CustomSeriesExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> seriesCollections;
    private List<String> seasons;

    public CustomSeriesExpandableListAdapter(Activity context, List<String> seasons,
                                 Map<String, List<String>> seriesCollections) {
        this.context = context;
        this.seriesCollections = seriesCollections;
        this.seasons = seasons;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return seriesCollections.get(seasons.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.episode_item, null);
        }

        TextView episodeTextView = (TextView) convertView.findViewById(R.id.episodeTextView);

        CheckBox episodeCheckBox = (CheckBox) convertView.findViewById(R.id.episodeCheckBox);
        /*
        delete.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<String> child =
                                        laptopCollections.get(laptops.get(groupPosition));
                                child.remove(childPosition);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        */

        episodeTextView.setText(laptop);
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
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.season_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.seasonTextView);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
