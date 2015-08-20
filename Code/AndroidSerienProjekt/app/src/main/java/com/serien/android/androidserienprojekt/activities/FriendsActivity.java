package com.serien.android.androidserienprojekt.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.serien.android.androidserienprojekt.R;

public class FriendsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
    }

    /*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.series_Search) {
            Intent startSearchActivity = new Intent(this, SearchActivity.class);
            startActivity(startSearchActivity);
//            FriendsActivity.this.finish();
            return true;
        }else if (id == R.id.series_Top_30) {
            Intent startTop30Activity = new Intent(this, Top30Activity.class);
            startActivity(startTop30Activity);
//            FriendsActivity.this.finish();
            return true;
        }else if (id == R.id.series_Main) {
            Intent startMainActivity = new Intent(this, ListActivity.class);
            startActivity(startMainActivity);
//            FriendsActivity.this.finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    */

}
