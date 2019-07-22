package com.example.nit_project;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nit_project.activities.AdminActivity;

public class MainActivity extends Activity {
    EditText UsernameEt,PasswordEt;
    TextView textView;
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
        textView= findViewById(R.id.netId);
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected())
        {
            textView.setVisibility(View.INVISIBLE);
        }
        else
        {
        btn1.setEnabled(false);
        //btn2.setEnabled(false);
        }

    }
    public void Onlogin(View view){
        String username=UsernameEt.getText().toString();
        String password=PasswordEt.getText().toString();
        String type ="login";
        BackgroundWorker backgroundWorker=new BackgroundWorker(this);
        backgroundWorker.execute(type,username,password);
        UsernameEt.setText("");
        PasswordEt.setText("");
    }
    public void Onreset(View view){
        UsernameEt.setText("");
        PasswordEt.setText("");
    }
    public void OnAdminLogin(View view){

        String username=UsernameEt.getText().toString();
        String password=PasswordEt.getText().toString();

        if(username.equals("Admin")&&password.equals("1234")) {
            startActivity(new Intent(this, AdminActivity.class));
        }
        else {
            Toast.makeText(this,"Invalid username or password",Toast.LENGTH_SHORT).show();
        }
        UsernameEt.setText("");
        PasswordEt.setText("");
    }

    public void Onregister(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }
}
