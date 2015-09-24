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
import com.serien.android.androidserienprojekt.activities.FriendsActivity;

import java.util.ArrayList;

/**
 * Created by Igor on 16.09.2015.
 */
public class CustomUserAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<String> users;
    private ArrayList<Integer> numbOfSeries;

    public CustomUserAdapter(FriendsActivity friendsActivity, ArrayList<String> userNames, ArrayList<Integer> numbOfSeries) {
        activity = friendsActivity;
        users = userNames;
        this.numbOfSeries = numbOfSeries;
    }


    static class ViewHolder{
        public ImageView userPic;
        public TextView userName;
        public TextView usersSeries;
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.user_layout, null);

            viewHolder = new ViewHolder();
            viewHolder.userPic = (ImageView) convertView.findViewById(R.id.user_series_pic);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.user_series_name);
            viewHolder.usersSeries = (TextView) convertView.findViewById(R.id.number_of_series);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.userPic.setImageResource(R.mipmap.unknown_user);
        viewHolder.userName.setText(users.get(position));
        viewHolder.usersSeries.setText("Anzahl der Serien: " + numbOfSeries.get(position));

        return convertView;
    }
}
