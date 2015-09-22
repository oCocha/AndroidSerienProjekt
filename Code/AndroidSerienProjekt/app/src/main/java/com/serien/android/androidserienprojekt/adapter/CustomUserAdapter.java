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

    public CustomUserAdapter(FriendsActivity friendsActivity, ArrayList<String> userNames) {
        activity = friendsActivity;
        users = userNames;

    }


    static class ViewHolder{
        public ImageView userPic;
        public TextView userName;
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
            convertView = inflater.inflate(R.layout.user_and_search_series_layout, null);

            viewHolder = new ViewHolder();
            viewHolder.userPic = (ImageView) convertView.findViewById(R.id.user_series_pic);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.user_series_name);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.userPic.setImageResource(R.mipmap.unknown_user);
        viewHolder.userName.setText(users.get(position));

        return convertView;
    }
}
