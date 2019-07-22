package com.example.nit_project.ClickableActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Constraints;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nit_project.Config;
import com.example.nit_project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import uk.co.senab.photoview.PhotoViewAttacher;


public class ThirdActivity extends AppCompatActivity
        implements OnClickableAreaClickedListener,ClickableAreasImage.Callback {

    private final String TAG = getClass().getSimpleName();
    public ClickableAreasImage clickableAreasImage;
    String  filepath,image_url;
    public  ImageView image;
    Button btnmark,btnedit;
    JSONArray jsonArray1=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        btnedit=findViewById(R.id.edit);
        btnmark=findViewById(R.id.Mark);
        btnmark.setVisibility(View.GONE);
        //get filepath from previous activity
        Intent intent = getIntent();
        image_url=intent.getStringExtra("image_url");
        String extra=intent.getStringExtra("json_array");


        //add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Add image
        image = findViewById(R.id.imageView);

        RequestOptions requestOptions= new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
        //setting image
        String image_url= Config.DB_URL+"AndroidFileUpload/uploads/"+this.image_url;

        Glide.with(this).load(image_url).apply(requestOptions).into(image);
        ClickableAreasImage.decider=true;
        clickableAreasImage=new ClickableAreasImage(ThirdActivity.this);
        clickableAreasImage.setParameters(new PhotoViewAttacher(image), this,
                getWindow().getDecorView(),image_url);

        try {
            jsonArray1=new JSONArray(extra);
            Log.d(TAG,"Json coordinate parsed are : "+jsonArray1.toString());
            if(ClickableAreasImage.clickableAreas.size()==0){
                for(int m=0;m<jsonArray1.length();m++){
                    JSONObject j2=null;
                    j2=jsonArray1.getJSONObject(m);
                    int X1=j2.getInt("x1");
                    int Y1=j2.getInt("y1");
                    int X2=j2.getInt("x2");
                    int Y2=j2.getInt("y2");
                    String s=j2.getString("description");
                    String st=j2.getString("image_url");
                    Log.d(TAG, "adding in third activity x" + X1 + "adding y" + Y1 + "adding x" + X2 + "adding y" + Y2);
                    ClickableAreasImage.clickableAreas.add(new ClickableArea(X1, Y1, X2, Y2,s,st));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG,"this is not parsing in third ativity");
        }



        // clickableAreasImage.setListener(SecondActivity.this);


        clickableAreasImage.ACTION = "ZOOM";
        clickableAreasImage.MODE = "VIEW";
        //creating spinnner drop down

//        ListAdapter2 =new ArrayAdapter<String>(SecondActivity.this,
//                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
    }

    // Listen for touches on your images:
    @Override
    public void onClickableAreaTouched(Object item, ClickableArea ca) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private static Bitmap getResizedBitmap(Bitmap bitmap, float maxWidth, float maxHeight) {
        Log.d(Constraints.TAG, "max width" + maxWidth + "max height" + maxHeight);
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        if (width > maxWidth) {
            height = (maxWidth / width) * height;
            width = maxWidth;
        }
        if (height > maxHeight) {
            width = (maxHeight / height) * width;
            height = maxHeight;
        }
        return Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);

    }

    public Bitmap decodeFile(String pathName) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
            try {
                bitmap = BitmapFactory.decodeFile(pathName, options);
                Log.d(TAG, "Decoded successfully for sampleSize " + options.inSampleSize);
                break;
            } catch (OutOfMemoryError outOfMemoryError) {
                // If an OutOfMemoryError occurred, we continue with for loop and next inSampleSize value
                Log.e(TAG, "outOfMemoryError while reading file for sampleSize " + options.inSampleSize
                        + " retrying with higher value");
            }
        }
        return bitmap;
    }

    public void onEdit(View view) {
        Log.d(TAG,"edit pressed");
        if (clickableAreasImage.MODE.equals("EDIT")) {
            clickableAreasImage.MODE = "VIEW";
            clickableAreasImage.rem=true;
            btnedit.setText("edit");
            btnmark.setVisibility(View.GONE);
        } else {
            clickableAreasImage.MODE = "EDIT";
            btnedit.setText("cancel");
            clickableAreasImage.rem=true;
            btnmark.setVisibility(View.VISIBLE);
        }
    }

    public void onBoxer(View view) {
        Log.d(TAG,"Mark pressed");
        clickableAreasImage.ACTION = "MARK";
        clickableAreasImage.createdialog();
    }
    @Override
    public String GiveMeImageUrl() {
        return  image_url;
    }
}

