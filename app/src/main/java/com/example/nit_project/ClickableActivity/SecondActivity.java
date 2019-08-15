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

import com.example.nit_project.R;

import java.io.File;
import java.io.FileNotFoundException;

import uk.co.senab.photoview.PhotoViewAttacher;

public class SecondActivity extends AppCompatActivity
        implements OnClickableAreaClickedListener,ClickableAreasImage.Callback {

    private final String TAG = getClass().getSimpleName();
    public ClickableAreasImage clickableAreasImage;
    String  filepath,image_url;
    public  ImageView image;
    Button btnmark,btnedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        btnedit=findViewById(R.id.edit);
        btnmark=findViewById(R.id.Mark);
        btnmark.setVisibility(View.GONE);
        ClickableAreasImage.decider=false;
        //get filepath from previous activity
        Intent intent = getIntent();
        filepath = intent.getStringExtra("filepath");
        image_url=intent.getStringExtra("image_url");
        if(clickableAreasImage==null){
            Log.d(TAG,"clickableareaimage is null");
        }
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
        BitmapFactory.Options options = new BitmapFactory.Options();
        Uri temp = Uri.fromFile(new File(filepath));
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(this.getContentResolver().openInputStream(temp), null, options);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "uri is null");
        }
        int imageWidth = image.getMaxWidth();
        int imageHeight = image.getMaxHeight();
        Bitmap bitmap = decodeFile(filepath);
        bitmap = getResizedBitmap(bitmap, imageWidth, imageHeight);
        image.setImageBitmap(bitmap);
        clickableAreasImage=new ClickableAreasImage(SecondActivity.this);
        clickableAreasImage.setParameters(new PhotoViewAttacher(image), this,
                getWindow().getDecorView(),image_url);
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

