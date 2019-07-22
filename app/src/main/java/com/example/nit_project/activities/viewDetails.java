package com.example.nit_project.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.nit_project.R;
import com.example.nit_project.adapters.RecyclerViewAdapter;
import com.example.nit_project.model.Anime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class  viewDetails extends AppCompatActivity {

    private String JSON_URL ;
    private JsonArrayRequest request ;
    private RequestQueue requestQueue ;
    String stats;
    private List<Anime> lstAnime ;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        stats=getIntent().getStringExtra("status");

        if(stats.equals("requested"))
        {
            JSON_URL="https://noobieuser.000webhostapp.com/AdminPage/PendingDetails.php";
        }
        else if(stats.equals("approved")||stats.equals("blocksomeone"))
        {
            JSON_URL = "https://noobieuser.000webhostapp.com/AdminPage/ApprovedDetails.php";
        }
        else if(stats.equals("blocked"))
        {
            JSON_URL = "https://noobieuser.000webhostapp.com/AdminPage/BlockedDetails.php";
        }

        lstAnime = new ArrayList<>() ;
        recyclerView = findViewById(R.id.recyclerviewid);
        jsonrequest();



    }

    private void jsonrequest() {

        request = new JsonArrayRequest(JSON_URL,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject  = null;

                for (int i = 0 ; i < response.length(); i++ ) {


                    try {
                        jsonObject = response.getJSONObject(i) ;
                        Anime anime = new Anime() ;
                        anime.setFirst_name(jsonObject.getString("firstname"));
                        anime.setUser_id(jsonObject.getString("username"));
                        anime.setMiddle_name(jsonObject.getString("middlename"));
                        anime.setLast_name(jsonObject.getString("lastname"));
                        anime.setAddress(jsonObject.getString("address"));
                        anime.setBatch_number(jsonObject.getString("batch"));
                        anime.setEmail(jsonObject.getString("email"));
                        anime.setCode(jsonObject.getString("code"));
                        anime.setContact_number(jsonObject.getString("contact"));
                        anime.setStatus(jsonObject.getString("status"));
                        anime.setImage_url(jsonObject.getString("image_url"));
                        lstAnime.add(anime);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                setuprecyclerview(lstAnime);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue = Volley.newRequestQueue(viewDetails.this);
        requestQueue.add(request) ;


    }

    private void setuprecyclerview(List<Anime> lstAnime) {

        RecyclerViewAdapter myadapter = new RecyclerViewAdapter(this,lstAnime,stats) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);
        //recyclerView.showContextMenu();

    }
}