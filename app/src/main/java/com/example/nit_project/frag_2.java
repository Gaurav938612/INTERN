package com.example.nit_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nit_project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class frag_2 extends AppCompatActivity {
    TextView userId,fullnameId,rankId,emailId;
    String userid,fullname,rank,email,image_url;
    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_2);
         userId=(TextView)findViewById(R.id.userId);
        fullnameId=(TextView)findViewById(R.id.fullnameId);
        rankId=(TextView)findViewById(R.id.rankId);
        emailId=findViewById(R.id.emailId);

        json_string=getIntent().getExtras().getString("json_data");
        try {
            jsonObject = new JSONObject(json_string);
            jsonArray=jsonObject.getJSONArray("server_response");
           // int count=0;
         //   while(count<jsonArray.length())
         //   {
                JSONObject jo=jsonArray.getJSONObject(0);
                userid=jo.getString("user_id");
                String firstname,middlename,lastname;
                firstname=jo.getString("first_name");
                middlename=jo.getString("middle_name");
                lastname=jo.getString("last_name");
                fullname=firstname+middlename+lastname;
                fullnameId.setText(fullname);
                rank=jo.getString("rank");
                rankId.setText(rank);
                email=jo.getString("email");
                emailId.setText(email);
                image_url=jo.getString("image_url");
                ImageView iv=findViewById(R.id.imageId);
                RequestOptions requestOptions= new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

            Glide.with(this).load(image_url).apply(requestOptions).into(iv);


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
