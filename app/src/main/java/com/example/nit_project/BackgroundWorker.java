package com.example.nit_project;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundWorker extends AsyncTask<String,Void,String> {
    ProgressDialog progress;
    private String r;
    String user_name;
    Context context;
    AlertDialog alertDialog;
    BackgroundWorker (Context ctx){
        context=ctx;
    }
    @Override
    protected String doInBackground(String ... params) {
        String type =params[0];
        String login_url="http://noobieuser.000webhostapp.com//login.php";
        if(type.equals("login")){
            try {
                user_name=params[1];
                String password=params[2];
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,
                        "UTF-8"));
                String post_data= URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line=bufferedReader.readLine())!=null){
                    result +=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        alertDialog=new AlertDialog.Builder(context).create();
        alertDialog.setTitle("login status");
        progress = new ProgressDialog(context);
        progress.setTitle("Logging in");
        progress.setMessage("Please wait...");
        progress.show();

    }

    @Override
    protected void onPostExecute(String result) {
        progress.dismiss();
        if(result.equals("user varified")){
        Intent intent=new Intent(context,fragment.class);
            intent.putExtra("userkey",user_name);
            intent.putExtra("parent","backgroundworker");
        context.startActivity(intent);
        }
        else {
            alertDialog.setMessage(result);
            alertDialog.show();
        }


    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
