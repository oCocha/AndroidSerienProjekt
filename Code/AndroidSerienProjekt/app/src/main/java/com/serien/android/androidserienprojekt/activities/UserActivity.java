package com.serien.android.androidserienprojekt.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.serien.android.androidserienprojekt.MainActivity;
import com.serien.android.androidserienprojekt.R;
import com.serien.android.androidserienprojekt.persistence.SeriesRepository;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends Activity {

    private EditText seriesEditText;
    private Button searchForUserButton;
    private Button registerNewUser;
    static String username;
    private Dialog dialog;
    private EditText name;
    private EditText nameConfirm;

    private String parseClassName = "SerienApp";

    final Context context = this;

    private ArrayList<String> allUser = new ArrayList<>();
    private ArrayList<ArrayList<String>> allSeriesList = new ArrayList<>();


    private SeriesRepository db;

    private boolean rightUser = false;
    private boolean rightUserSeries = true;
    private boolean newUser = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initUsername();
        initDB();
        getParseData();
        initUI();
        initListener();
    }


    private void initUsername() {
        username = "";
    }


    //Initializes the SQLite database
    private void initDB() {
        db = new SeriesRepository(this);
        db.open();
    }


    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }


    //Gets all Usernames and the Serieslists of each User in the Parse.com Database
    private void getParseData() {
        ParseQuery<ParseObject> query = new ParseQuery<>(parseClassName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(UserActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        allUser.add(list.get(i).getString("userName"));
                        allSeriesList.add((ArrayList) list.get(i).get("series"));
                    }
                }
            }
        });
    }


    //Initializes the UI
    private void initUI() {
        seriesEditText = (EditText) findViewById(R.id.user_editText);
        searchForUserButton = (Button) findViewById(R.id.accept_Button);
        registerNewUser = (Button) findViewById(R.id.register_button);
    }


    //Initializes the listener in this activity
    private void initListener() {

        //Sets a onClickListener on the search button for user in the Parse.com database
        searchForUserButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username = seriesEditText.getText().toString();

                if (allUser.contains(username)) {
                    rightUser = true;
                    checkInsertsOfUser();


                    if (rightUser && rightUserSeries) {
                        showSuccessMessage();
                        startApplication();

                    }else if (rightUser && !rightUserSeries) {
                        showWrongUserMessage();
                    }
                } else if(username.isEmpty()){
                    Toast.makeText(UserActivity.this, "Sie haben keinen validen Benutzernamen eingegeben!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserActivity.this, "Benutzername nicht gefunden. Bitte geben Sie einen validen Usernamen ein oder registrieren Sie sich ", Toast.LENGTH_LONG).show();
                }
            }
        });

        registerNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegistrationScreen();
            }
        });
    }


    //Checks if the name that the User inserted into the EditText field is inside the Parse.com database
    private void checkInsertsOfUser() {
        rightUserSeries = true;
        ArrayList<String> tempSeriesList = db.getAllSeriesNames();
        ArrayList<String> parseSeriesList = allSeriesList.get(allUser.indexOf(username));

        if (parseSeriesList != null) {
            if(parseSeriesList.size() != tempSeriesList.size() || parseSeriesList.isEmpty() && tempSeriesList.isEmpty()){
                rightUserSeries = false;
            }
        }


        for(int i = 0; i < tempSeriesList.size(); i++){
            if (parseSeriesList != null) {
                if(!parseSeriesList.contains(tempSeriesList.get(i))){
                    rightUserSeries = false;
                }
            }
        }
    }


    //Shows a success Message to the User
    private void showSuccessMessage() {
        Toast.makeText(UserActivity.this, "Willkommen zurück '" + username + "'!", Toast.LENGTH_SHORT).show();
    }


    //If the Username and the number of Series is correct, the Activity MainActivity will start
    private void startApplication() {
        Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentMain);
        finish();
    }



    //If only the Username is correct, the User gets a message about it
    private void showWrongUserMessage() {
        Toast.makeText(UserActivity.this, "'" + username + "' ist nicht ihr richtiger Benutzername!", Toast.LENGTH_LONG).show();
    }



    //If the Username is new, a registration screen will show up
    private void showRegistrationScreen() {
        initScreen();
        initListenerInLogInScreen();
        showScreen();
    }


    //Initializes the registration Screen
    private void initScreen() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.login_fragment);
        dialog.setTitle("Anmelden");

        name = (EditText) dialog.findViewById(R.id.name_user);
        nameConfirm = (EditText) dialog.findViewById(R.id.name_user_confirm);
        name.setText(username);
    }


    //Initializes the Buttons in the registration screen
    private void initListenerInLogInScreen() {

        Button cancel = (Button) dialog.findViewById(R.id.decline_button);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        Button okButton = (Button) dialog.findViewById(R.id.login_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameOneEntered = name.getText().toString();
                String nameTwoEntered = nameConfirm.getText().toString();

                if (nameOneEntered.equals(nameTwoEntered)) {
                    newUserIntent(nameOneEntered);
                } else {
                    showMessageWrongUsername(nameOneEntered, nameTwoEntered);
                }
            }
        });
    }


    //Shows the registration screen where the User can create a new Account
    private void showScreen() {
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    //Initializes new SQLite Database and starts a Intent
    private void newUserIntent(String nameOneEntered) {
        username = nameOneEntered;
        dialog.dismiss();
        db.initDBNew();
        updateParseDatabase();
        Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentMain);
        Toast.makeText(UserActivity.this, "Sie haben sich mit dem Namen '" + username + "' Angemeldet" , Toast.LENGTH_SHORT).show();
        finish();
    }


    //Delets the entered confirm Name of the User and shows a Message to the User
    private void showMessageWrongUsername(String nameOneEntered, String nameTwoEntered) {
        nameConfirm.setText("");
        Toast.makeText(UserActivity.this, "Der Bestätigungsname " + "'" + nameTwoEntered + "'" + " stimmt nicht mit dem Eingegebenen Namen '" + nameOneEntered + "' Überein!", Toast.LENGTH_LONG).show();
    }


    //If a new User is created this new User will be saved in the Parse.com Database
    private void updateParseDatabase() {
        ParseQuery<ParseObject> query = new ParseQuery<>(parseClassName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(UserActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getString("userName").equals(username)) {
                            newUser = false;
                        }
                    }
                    if (newUser) {
                        ParseObject tempParseObject = new ParseObject("SerienApp");
                        tempParseObject.put("userName", username);
                        tempParseObject.saveInBackground();
                    }
                }
            }
        });
    }


    //Returns the current Username
    public static String getUserName(){
        return username;
    }

}
