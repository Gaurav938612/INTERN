package com.example.nit_project.ClickableActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Constraints;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nit_project.Config;
import com.example.nit_project.Image_downloader;
import com.example.nit_project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.lang.reflect.Array;

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.example.nit_project.ClickableActivity.ClickableAreasImage.clickableAreas;


public class ThirdActivity extends AppCompatActivity
        implements OnClickableAreaClickedListener,ClickableAreasImage.Callback,Image_downloader.Callback {

    private final String TAG = getClass().getSimpleName();
    public ClickableAreasImage clickableAreasImage;
    public Image_downloader image_downloader;
    String  filepath,image_url;
    public  ImageView image;
    Button btnmark,btnedit;
    JSONArray jsonArray1=null;
    Spinner spinner;

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
        spinner=findViewById(R.id.view_spinner);
        //add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Add image
        image = findViewById(R.id.imageView);

        RequestOptions requestOptions= new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
        //setting image
        String image_url= Config.DB_URL+"AndroidFileUpload/uploads/"+this.image_url;

        InputStream input=null;
        Bitmap myBitmap=null;

        //Glide.with(this).load(image_url).apply(requestOptions).into(image);
        ClickableAreasImage.decider=true;
        image_downloader=new Image_downloader(getWindow().getDecorView(),this);
        image_downloader.execute(image_url);

        clickableAreasImage=new ClickableAreasImage(ThirdActivity.this);
        try {
            jsonArray1=new JSONArray(extra);
          //  Log.d(TAG,"Json coordinate parsed are : "+jsonArray1.toString());
            if(clickableAreas.size()==0){
                for(int m=0;m<jsonArray1.length();m++){
                    JSONObject j2=null;
                    j2=jsonArray1.getJSONObject(m);
                    int X1=j2.getInt("x1");
                    int Y1=j2.getInt("y1");
                    int X2=j2.getInt("x2");
                    int Y2=j2.getInt("y2");
                    String s=j2.getString("description");
                    String st=j2.getString("image_url");
                    Log.d(TAG,"image_url is :"+st);
                    Log.d(TAG,"image_description is :"+s);
                    Log.d(TAG, "adding in third activity x" + X1 + "adding y" + Y1 + "adding x" + X2 + "adding y" + Y2);
                    clickableAreas.add(new ClickableArea(X1, Y1, X2, Y2,s,st));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG,"this is not parsing in third ativity");
        }

        clickableAreasImage.ACTION = "ZOOM";
        clickableAreasImage.MODE = "VIEW";

    }

    // Listen for touches on your images:
    @Override
    public void onClickableAreaTouched(Object item, ClickableArea ca) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.third_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            int count=0;
            for (ClickableArea ca : clickableAreas) {
                if(ca.getIndex().equals(image_url)) {
                    count++;
                }
            }
            String string[]=new String[count+1];
            string[0]="select item";
            int i=1;
            for (ClickableArea ca : clickableAreas) {
                if(ca.getIndex().equals(image_url)) {
                    string[i]=ca.getItem();
                    i+=1;
                }
            }

            ArrayAdapter<String> marked_items=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,string);
            spinner.setAdapter(marked_items);
            spinner.setVisibility(View.VISIBLE);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                   // Toast.makeText(getApplicationContext(),Integer.toString(i),Toast.LENGTH_SHORT).show();
                    int j=1;
                    for (ClickableArea ca : clickableAreas) {
                        if(ca.getIndex().equals(image_url)) {
                            if(j==i)
                            {
                                //draw rectangle
                                Toast.makeText(getApplicationContext(),ca.getItem()+" "+ca.getX(),Toast.LENGTH_SHORT).show();
                                break;
                            }
                            j+=1;
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            return true;
        }
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

    @Override
    public void notify_on_execute(Bitmap bitmap) {
        if(bitmap!=null){
        clickableAreasImage.setParameters(new PhotoViewAttacher(image), this,
                    getWindow().getDecorView(),image_url);
        }
        else {
            Log.d(TAG,"bitmap is null");
        }
    }

    public void Goback(View view) {
        finish();
    }
}

