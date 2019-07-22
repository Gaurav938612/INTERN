package com.example.nit_project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
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

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

    }

    public void onApprove(View view) {
        Intent intent=new Intent(this,viewDetails.class);
        intent.putExtra("status","approved");
        startActivity(intent);
    }

    public void onRequest(View view) {
        Intent intent=new Intent(this,viewDetails.class);
        intent.putExtra("status","requested");
        startActivity(intent);
    }

    public void onBlock(View view) {
        Intent intent=new Intent(this,viewDetails.class);
        intent.putExtra("status","blocked");
        startActivity(intent);
    }

    public void onBlocksomeone(View view) {
        Intent intent=new Intent(this,viewDetails.class);
        intent.putExtra("status","blocksomeone");
        startActivity(intent);
    }
}