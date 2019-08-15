package com.example.nit_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nit_project.ClickableActivity.ClickableAreasImage;
import com.example.nit_project.activities.EditableGallaryActivity;
import com.example.nit_project.activities.GallaryActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class frag_4 extends AppCompatActivity {

    String case_number;
    ProgressDialog progress;
    AlertDialog alertDialog;
    JSONObject jsonobject;
    TextInputEditText case_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_4);
         case_id=findViewById(R.id.caseId);

    }

    public void DownloadFiles(View view) {
        case_number=case_id.getText().toString().trim();
        if(case_number.length()==0)
        {
            Toast.makeText(this,"Case_number can't be empty",Toast.LENGTH_SHORT);
            return;
        }

        final JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("case_number",case_number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progress = new ProgressDialog(this);
        progress.setTitle("Downloading");
        progress.setMessage("Please wait...");
        progress.show();
        progress.setCancelable(false);
        Log.d(TAG," Case number : "+jsonObject.toString());
        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.POST, Config.Edit_url,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject1) {
                        Log.d(TAG,"onResponse");
                        jsonobject=jsonObject1;
                        progress.dismiss();
                        Log.d(TAG,"Json object after respond is : "+jsonobject.toString());
                        try {
                            JSONArray j=jsonobject.getJSONArray("image_url");
                            if(j.length()==0){
                                Toast.makeText(getApplicationContext(),"Case number not Found.",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intent=new Intent(getApplicationContext(), EditableGallaryActivity.class);
                                intent.putExtra("json_details",jsonobject.toString());
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Log.d(TAG,"error hai");
                alertDialog.setTitle("Error !");
                if(error instanceof TimeoutError){
                    //Toast.makeText(getApplicationContext(),"Timeout Error", Toast.LENGTH_SHORT).show();
                    alertDialog.setMessage("Timeout Error");
                }
                else if(error instanceof NoConnectionError){
                    // Toast.makeText(getApplicationContext(),"No Connection  Error", Toast.LENGTH_SHORT).show();
                    alertDialog.setMessage("No Connection ");
                }
                else if(error instanceof AuthFailureError){
                    //Toast.makeText(getApplicationContext(),"Authentication Error", Toast.LENGTH_SHORT).show();
                    alertDialog.setMessage("Authentication Error");
                }
                else if(error instanceof NetworkError){
                    // Toast.makeText(getApplicationContext(),"Network Error", Toast.LENGTH_SHORT).show();
                    alertDialog.setMessage("Network Problem");
                }
                else if(error instanceof ServerError){
                    // Toast.makeText(getApplicationContext(),"Server Error", Toast.LENGTH_SHORT).show();
                    alertDialog.setMessage("Server Problem !\nTry after some time");
                }
                else if(error instanceof ParseError){
                    // Toast.makeText(getApplicationContext(),"Json Error", Toast.LENGTH_SHORT).show();
                    alertDialog.setMessage("Something went wrong ,please try again");
                }
                else
                    alertDialog.setMessage("please try again");
                alertDialog.show();
            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
