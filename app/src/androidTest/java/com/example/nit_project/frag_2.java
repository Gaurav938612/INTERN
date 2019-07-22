package com.example.nit_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class frag_2 extends AppCompatActivity {
    TextView usernameId,fullnameId,postId;
    String username,fullname,post;
    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_2);
        fullnameId=(TextView)findViewById(R.id.userId);
        usernameId=(TextView)findViewById(R.id.fullnameId);
        postId=(TextView)findViewById(R.id.rankId);

        json_string=getIntent().getExtras().getString("json_data");
        try {
            jsonObject = new JSONObject(json_string);
            jsonArray=jsonObject.getJSONArray("server_response");
           // int count=0;
         //   while(count<jsonArray.length())
         //   {
                JSONObject jo=jsonArray.getJSONObject(0);
                username=jo.getString("username");
                fullname=jo.getString("fullname");
                post=jo.getString("post");
                fullnameId.setText(fullname);
                usernameId.setText(username);
                postId.setText(post);
         //       break;
         //   }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void Goback(View view) {
        finish();
    }

}
