package com.serien.android.androidserienprojekt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.serien.android.androidserienprojekt.R;

/**
 * Created by Igor on 20.08.2015.
 */
public class startActivityImageAdapter extends BaseAdapter{
    private Context nContext;
    private final String[] imageText;
    private final int[] imageId;

    public startActivityImageAdapter(Context c, String[] imageText, int[] imageId){
        nContext = c;
        this.imageText = imageText;
        this.imageId = imageId;
    }


    @Override
    public int getCount() {
        return imageText.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) nContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){

            grid = inflater.inflate(R.layout.start_activity_single_item, null);
            TextView textView = (TextView) grid.findViewById(R.id.start_icon_name);
            ImageView imageView = (ImageView) grid.findViewById(R.id.start_icon);
            textView.setText(imageText[position]);
            imageView.setImageResource(imageId[position]);
        }else{
            grid = convertView;
        }
        return grid;
    }

}
