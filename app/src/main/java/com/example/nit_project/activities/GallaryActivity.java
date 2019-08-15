package com.example.nit_project.activities;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nit_project.ClickableActivity.ClickableArea;
import com.example.nit_project.ClickableActivity.ClickableAreasImage;
import com.example.nit_project.Config;
import com.example.nit_project.R;
import com.example.nit_project.adapters.GallaryViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class GallaryActivity extends AppCompatActivity implements GallaryViewAdapter.Callback {

    RecyclerView myrv;
    GallaryViewAdapter myAdapter;
    private ProgressDialog dialog = null;

    private String filepath, newFile;
    Uri fileuri;
    ArrayList<String> encodedImageList,encodedImageList2;
    private JSONObject jsonObject;
    int count = 0;

    private List<ImageInfo> lstImageinfo;
    private String case_number = null;
    private boolean isimage;
    public ImageView imageView2;
    public static int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imageView2 = findViewById(R.id.delete_button);
        imageView2.setVisibility(View.GONE);
        Intent i = getIntent();
        // image or video path that is captured in previous activity
        filepath = i.getStringExtra("filepath");
        case_number = i.getStringExtra("case_number");
        isimage = i.getBooleanExtra("isImage", true);
        if (filepath == null) {
            Toast.makeText(GallaryActivity.this, "Some files are missing or have been deleted ", Toast.LENGTH_SHORT).show();
        }
        newFile = compressImage(filepath);
        lstImageinfo = new ArrayList<>();
        count++;
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HHmmss",
                Locale.getDefault()).format(new Date());
        int random = new Random().nextInt(88888888) + 10000000;
        timeStamp=timeStamp+"_"+random+".jpg";
        lstImageinfo.add(new ImageInfo(case_number, "Categorie Gallary", timeStamp, newFile, isimage));


        myrv = (RecyclerView) findViewById(R.id.gallary_view_id);
        myrv.setLayoutManager(new GridLayoutManager(this, 2));
        myrv.setHasFixedSize(true);
        myrv.setItemViewCacheSize(20);
        myrv.setDrawingCacheEnabled(true);
        myrv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        myAdapter = new GallaryViewAdapter(GallaryActivity.this, lstImageinfo);
        myAdapter.setListener(GallaryActivity.this);
        myrv.setAdapter(myAdapter);

        //progress dialog box
        dialog = new ProgressDialog(this);
        dialog.setMessage("Preparing for Upload...");
        dialog.setCancelable(false);

        jsonObject = new JSONObject();
        encodedImageList = new ArrayList<>();
        encodedImageList2=new ArrayList<>();
        //first image being added from camera
    }

    private void AddImageToList(Uri fileuri,String id) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileuri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        encodedImageList.add(encodedImage);
        encodedImageList2.add(id);
        Log.d(TAG, "encoded imaged added");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gallary_fragment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            captureImage();
            return true;
        } else if (id == R.id.action_picture) {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Choose picture"), Config.FILE_IMAGE_REQUEST_CODE);
            return true;
        } else if (id == R.id.upload_pictures) {
            dialog.show();
            Uri uri;
            for (ImageInfo i : lstImageinfo) {
                uri = Uri.fromFile(new File(i.getFilepath()));
                AddImageToList(uri,i.getDescription());
            }
            if (encodedImageList.isEmpty()) {
                Toast.makeText(this, "Please select some images first.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return false;
            }
            dialog.setMessage("Uploading Picutres...");
            JSONArray jsonArray = new JSONArray();
            for (String encoded : encodedImageList) {

                jsonArray.put(encoded);
            }
            JSONArray jsonArray1=new JSONArray();
            for (String encoded:encodedImageList2){
                jsonArray1.put(encoded);
            }
            JSONArray jsonArray2 = new JSONArray();
            for (ClickableArea c : ClickableAreasImage.clickableAreas) {
                JSONObject j=new JSONObject();
                try {
                    j.put("id",c.getIndex());
                    j.put("x1",c.getX());
                    j.put("y1",c.getY());
                    j.put("x2",c.getW());
                    j.put("y2",c.getH());
                    j.put("item",c.getItem());

                    jsonArray2.put(j);
                    Log.d(TAG,jsonArray2.toString());
                } catch (JSONException e) {
                    Log.e(TAG,"ClickableArea object can't be put"+ e.toString());
                }
            }
            try {
                //jsonObject.put(Config.imageName, "test_name");
                jsonObject.put(Config.imageList, jsonArray);
                jsonObject.put("image_id",jsonArray1);
                Log.d(TAG,"json array1  is :"+jsonArray1);
                jsonObject.put("description",jsonArray2);
                Log.d(TAG,"case number"+case_number);
                jsonObject.put("case_no",case_number);
                Log.d(TAG,"Json object before sending :"+jsonObject.toString());
            } catch (JSONException e) {
                Log.e("JSONObject Here", e.toString());
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.urlUpload, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d(TAG,"Message from server"+jsonObject.toString());
                            dialog.dismiss();
                            Toast.makeText(getApplication(), "Images Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            encodedImageList.clear();
                            encodedImageList2.clear();
                            ClickableAreasImage.clickableAreas.clear();
                            lstImageinfo.clear();
                            myAdapter = new GallaryViewAdapter(GallaryActivity.this, lstImageinfo);
                            myAdapter.setListener(GallaryActivity.this);
                            myrv.setAdapter(myAdapter);
                            count=0;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("Message from server", volleyError.toString());
                    Toast.makeText(getApplication(), "Error Occurred", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(200 * 30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(jsonObjectRequest);
        }
        return super.onOptionsItemSelected(item);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileuri = getOutputMediaFileUri(Config.MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
        startActivityForResult(intent, Config.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    public static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == Config.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileuri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        fileuri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == Config.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // launching upload activity
                displayActivity(true);
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(), " Cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == Config.FILE_IMAGE_REQUEST_CODE) {
            int n = 1;
            if (resultCode == RESULT_OK) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                if (data.getData() != null) {
                    Uri mImageUri = data.getData();
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    filepath = cursor.getString(columnIndex);
                    cursor.close();
                    newFile = compressImage(filepath);
                    String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HHmmss",
                            Locale.getDefault()).format(new Date());
                    int random = new Random().nextInt(88888888) + 10000000;
                    timeStamp=timeStamp+"_"+random+".jpg";
                    lstImageinfo.add(new ImageInfo(case_number, "Categorie Gallary", timeStamp
                            , newFile, isimage));
                } else if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    n = mClipData.getItemCount();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri mImageUri = item.getUri();
                        Cursor cursor = getContentResolver().query(mImageUri,
                                filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        filepath = cursor.getString(columnIndex);
                        cursor.close();
                        newFile = compressImage(filepath);
                        String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HHmmss",
                                Locale.getDefault()).format(new Date());
                        int random = new Random().nextInt(88888888) + 10000000;
                        timeStamp=timeStamp+"_"+random+".jpg";
                        lstImageinfo.add(new ImageInfo(case_number, "Categorie Gallary",timeStamp
                                , newFile, isimage));
                    }
                }
                //myAdapter.notifyDataSetChanged();
                myAdapter.notifyItemRangeInserted(count + 1, n);
                count = count + n;
            } else if (resultCode == RESULT_CANCELED) {
                //do nothing
            } else {
                Toast.makeText(getApplicationContext(), "Failed to select image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayActivity(boolean isimage) {
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HHmmss",
                Locale.getDefault()).format(new Date());
        int random = new Random().nextInt(88888888) + 10000000;
        timeStamp=timeStamp+"_"+random+".jpg";
        lstImageinfo.add(new ImageInfo(case_number, "Categorie Gallary", timeStamp,
                compressImage(fileuri.getPath()), isimage));
        //myAdapter.notifyDataSetChanged();
        count++;
        myAdapter.notifyItemInserted(count);
    }


    @Override
    public void onAddClick() {
        imageView2.setVisibility(View.VISIBLE);

    }

    @Override
    public void onCancelClick() {
        imageView2.setVisibility(View.GONE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        myAdapter = new GallaryViewAdapter(GallaryActivity.this, lstImageinfo);
        myAdapter.setListener(GallaryActivity.this);
        myrv.setAdapter(myAdapter);
    }


    public void onDelete(View view) {
        ArrayList<Integer> deletable = myAdapter.selected;
        Collections.sort(deletable);
        Log.d("Size ", Integer.toString(deletable.size()));
        for (int i = deletable.size() - 1; i >= 0; i--) {
            String item_name=lstImageinfo.get(deletable.get(i).intValue()).getDescription();
            for(ClickableArea ca : ClickableAreasImage.clickableAreas)
            {
                if(ca.getIndex().equals(item_name)){
                    ClickableAreasImage.clickableAreas.remove(ca);
                }
            }
            lstImageinfo.remove(deletable.get(i).intValue());
            Log.d("deletable index", Integer.toString(deletable.get(i)));
            Log.d("Size remained ", Integer.toString(lstImageinfo.size()));

        }
        Integer min = Collections.min(deletable);
        Integer max = Collections.max(deletable);
        myAdapter.notifyItemRangeRemoved(min, max - min + 1);
        //myAdapter.notifyItemRemoved(min);
        myAdapter.notifyItemRangeChanged(min, lstImageinfo.size() - min);

        deletable.clear();
        myAdapter.selected.clear();
        myAdapter.mode = "view";
        imageView2.setVisibility(View.GONE);
    }


    //whatever are below this line are being used for image compression
    //and image  width and height reduction

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    //give file new file path
    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "SSFR_Image/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        i++;
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HHmmss",
                Locale.getDefault()).format(new Date());
        String uriSting = (file.getAbsolutePath() + "/"+ timeStamp+ "_"+i+ ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

}

