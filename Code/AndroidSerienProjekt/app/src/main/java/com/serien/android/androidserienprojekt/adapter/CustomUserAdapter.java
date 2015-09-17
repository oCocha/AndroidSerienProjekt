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
        View userView;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            userView = inflater.inflate(R.layout.user_and_search_series_layout, null);

            ImageView userPic = (ImageView) userView.findViewById(R.id.user_series_pic);
            TextView userName = (TextView) userView.findViewById(R.id.user_series_name);

            userPic.setImageResource(R.mipmap.unknown_user);
            userName.setText(users.get(position));
        }else{
            userView = convertView;
        }
        return userView;
    }
}
