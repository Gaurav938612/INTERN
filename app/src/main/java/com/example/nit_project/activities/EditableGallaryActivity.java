package com.example.nit_project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.nit_project.ClickableActivity.ClickableAreasImage;
import com.example.nit_project.R;
import com.example.nit_project.adapters.GallaryViewAdapter;
import com.example.nit_project.adapters.GallaryViewAdapter2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditableGallaryActivity extends AppCompatActivity {

    GridLayout gridLayout;
    RecyclerView myrv;
    JSONObject jsonObject=null;
    GallaryViewAdapter2 myAdapter;
    JSONArray jsonArray1=null;
    JSONArray jsonArray2=null;
    private List<ImageInfo> lstImageinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String extra=i.getStringExtra("json_details");
        try {
            jsonObject=new JSONObject(extra);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jsonArray1=jsonObject.getJSONArray("image_url");
            jsonArray2=jsonObject.getJSONArray("image_coordinates");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lstImageinfo = new ArrayList<>();
        for(int j=0;j<jsonArray1.length();j++){
            JSONObject jsonObject1=null;
            try {
                jsonObject1 = jsonArray1.getJSONObject(j) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String s="";
            try {
                 s=jsonObject1.getString("image_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            lstImageinfo.add(new ImageInfo("case_number", "Categorie Gallary","timeStamp",s, true));
        }



        setContentView(R.layout.activity_editable_gallary);
        myrv = (RecyclerView) findViewById(R.id.gallary_view_id);
        myrv.setLayoutManager(new GridLayoutManager(this, 2));
        myrv.setHasFixedSize(true);
        myrv.setItemViewCacheSize(20);
        myrv.setDrawingCacheEnabled(true);
        myrv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        myAdapter = new GallaryViewAdapter2(EditableGallaryActivity.this, lstImageinfo,jsonArray2);
        myrv.setAdapter(myAdapter);

    }

    @Override
    public void onBackPressed() {
        ClickableAreasImage.clickableAreas.clear();
        finish();
    }
}
