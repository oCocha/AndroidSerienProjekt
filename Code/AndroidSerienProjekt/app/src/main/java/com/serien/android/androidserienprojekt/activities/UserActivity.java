package com.serien.android.androidserienprojekt.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.serien.android.androidserienprojekt.MainActivity;
import com.serien.android.androidserienprojekt.R;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    public static EditText seriesEditText;
    public static Button searchButton;
    final Context context = this;
    private ArrayList<String> allUser = new ArrayList<>();
    static String username;
    private Dialog dialog;
    private EditText name;
    private EditText nameConfirm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initDialog();
        initUI();
        setupParse();
    }

    private void initDialog() {
        username = "";

    }

    private void initListener(final ArrayList<String> allUsers) {




        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username = seriesEditText.getText().toString();
                if(allUsers.contains(username)){
                    Toast.makeText(UserActivity.this, "Hallo '" + username + "'!", Toast.LENGTH_SHORT).show();
                    Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentMain);
                }else{

                    dialog = new Dialog(context);
                    dialog.setContentView(R.layout.login_fragment);
                    dialog.setTitle("Anmelden");

                    name = (EditText) dialog.findViewById(R.id.name_user);
                    name.setText(username);
                    nameConfirm = (EditText) dialog.findViewById(R.id.name_user_confirm);


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
                                username = nameOneEntered;
                                dialog.dismiss();
                                Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intentMain);
                            }else{
                                nameConfirm.setText("");
                                Toast.makeText(UserActivity.this, "Der Bestätigungsname " + "'" + nameTwoEntered + "'" + " stimmt nicht mit dem Eingegebenen Namen '" + nameOneEntered + "' Überein!", Toast.LENGTH_LONG).show();

                            }
                        }
                    });




                    dialog.show();


                }


            }
        });





    }

    public static String getUserName(){
        return username;
    }

    private void initUI() {
        seriesEditText = (EditText) findViewById(R.id.user_editText);
        searchButton = (Button) findViewById(R.id.accept_Button);
    }


    private void setupParse() {
        String parseClassName = "SerienApp";
        ParseQuery<ParseObject> query = new ParseQuery<>(parseClassName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(UserActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        allUser.add(list.get(i).getString("userName"));
                        }
                    }
                initListener(allUser);
                }
        });
    }

}
