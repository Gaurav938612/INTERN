package com.example.nit_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nit_project.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class AnimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        getSupportActionBar().hide();

        /*private String user_id;
        private String first_name;
        private String middle_name;
        private String last_name;
        private String email;
        private String contact_number;
        private String batch_number;
        private String address;
        private String status;
        private String code;*/
        //retrieve data
        String first_name=getIntent().getExtras().getString("first_name");
        String last_name=getIntent().getExtras().getString("last_name");
        String middle_name=getIntent().getExtras().getString("middle_name");
        String user_id=getIntent().getExtras().getString("user_id");
        String address=getIntent().getExtras().getString("address");
        String contact_number=getIntent().getExtras().getString("contact");
        String batch_number=getIntent().getExtras().getString("batch");
        String status=getIntent().getExtras().getString("status");
        String code=getIntent().getExtras().getString("code");
        String email=getIntent().getExtras().getString("email");
        String image_url=getIntent().getExtras().getString("image_url");

        //initializing values

        CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        TextView First_name=findViewById(R.id.aa_full_name);
        TextView tv_studio=findViewById(R.id.aa_studio);
        TextView tv_categorie=findViewById(R.id.aa_categorie);
        TextView tv_description=findViewById(R.id.aa_description);
        TextView tv_rating=findViewById(R.id.aa_rating);
        ImageView img=findViewById(R.id.aa_thumbnail);


        //setting values

        tv_categorie.setText(status);
        First_name.setText(first_name);
        tv_description.setText(email);
        tv_rating.setText(code);
        tv_studio.setText(contact_number);

        collapsingToolbarLayout.setTitle(first_name);
        RequestOptions requestOptions= new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
        //setting image

        Glide.with(this).load(image_url).apply(requestOptions).into(img);



    }
}
