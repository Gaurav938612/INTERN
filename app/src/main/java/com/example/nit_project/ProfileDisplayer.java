package com.example.nit_project;

import android.app.AlertDialog;
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

public class ProfileDisplayer extends AsyncTask<String,Void,String> {
    String json;
    String result;
    Context context;
    ProfileDisplayer(Context context){
        this.context=context;
    }
    @Override
    protected String doInBackground(String... params) {
        String type =params[0];
        String user_name=params[1];
        String post_data;
        String display_url="http://noobieuser.000webhostapp.com/displayprofile.php";
        if(type.equals("displayProfile")){
            try {

                URL url=new URL(display_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,
                        "UTF-8"));
                if(user_name.length()!=0) {
                    post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((json = bufferedReader.readLine()) != null) {
                        stringBuilder.append(json + "\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim();
                }
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
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        json=result;
        Intent intent=new Intent(context,frag_2.class);
        intent.putExtra("json_data",json);
        context.startActivity(intent);

    }
}

