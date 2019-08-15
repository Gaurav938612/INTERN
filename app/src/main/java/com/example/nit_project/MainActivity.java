package com.example.nit_project;





import android.app.Activity;

import android.app.AlertDialog;

import android.app.ProgressDialog;

import android.app.Service;

import android.content.Context;

import android.content.Intent;

import android.net.ConnectivityManager;

import android.net.NetworkInfo;

import android.os.Bundle;

import android.util.Log;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.Toast;



import com.example.nit_project.activities.AdminActivity;



public class MainActivity extends Activity {

    EditText UsernameEt,PasswordEt;

    Button btn1,btn2;

    AlertDialog alert;

    SharedpreferenceConfig shared;

    String user_name;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        if(getIntent().getBooleanExtra("exit",false))

        {

            finish();

        }

        else {

            shared = new SharedpreferenceConfig(getApplicationContext());

            if (shared.readloginstatus()) {

                user_name = shared.readusername();

                Intent intent = new Intent(this, fragment.class);

                intent.putExtra("userkey", user_name);

                intent.putExtra("parent", "mainactivity");

                startActivity(intent);



            }

        }

        UsernameEt= findViewById((R.id.etName));

        PasswordEt= findViewById(R.id.etPassword);

        btn1= findViewById(R.id.btnLogin);

        btn2= findViewById(R.id.btnReset);

        btn1.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                if(checkconnection().equals("connected")) {

                    Log.d("here","connected");

                    String username = UsernameEt.getText().toString();

                    String password = PasswordEt.getText().toString();

                    String type = "login";

                    BackgroundWorker backgroundWorker = new BackgroundWorker(view.getContext());

                    backgroundWorker.execute(type, username, password);

                    UsernameEt.setText("");

                    PasswordEt.setText("");

                }

                else {Log.d("here","not connected");

                    Toast.makeText(view.getContext(),"Internet not connected!!",Toast.LENGTH_SHORT).show();

                }

            }

        });

    /*    ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null&&networkInfo.isConnected())

        {

        }

        else

        {

        btn1.setEnabled(false);

        //btn2.setEnabled(false);

        }*/



    }

    private String checkconnection(){

        String s="";

        ConnectivityManager connectivityManager=(ConnectivityManager)

                getSystemService(Service.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null&&networkInfo.isConnected())

        {

            return "connected";

        }

        else return "not connected";

    }



    public void Onreset(View view){

        UsernameEt.setText("");

        PasswordEt.setText("");

    }

    public void OnAdminLogin(View view) {



        if (checkconnection().equals("connected")) {

            String username = UsernameEt.getText().toString();

            String password = PasswordEt.getText().toString();



            if (username.equals("Admin") && password.equals("1234")) {

                startActivity(new Intent(this, AdminActivity.class));

            } else {

                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();

            }

            UsernameEt.setText("");

            PasswordEt.setText("");

        }

        else {Log.d("here","not connected");

            Toast.makeText(this,"Internet not connected!!",Toast.LENGTH_SHORT).show();

        }

    }



    public void Onregister(View view) {

        startActivity(new Intent(this,RegisterActivity.class));

    }

}