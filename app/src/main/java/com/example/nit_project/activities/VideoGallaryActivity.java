package com.example.nit_project.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nit_project.Config;
import com.example.nit_project.R;
import com.example.nit_project.adapters.VideoViewAdapter;
import com.example.nit_project.camera.VideoActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class VideoGallaryActivity extends AppCompatActivity implements VideoViewAdapter.Callback {
    RecyclerView myrv;
    VideoViewAdapter myAdapter;
    private ProgressDialog dialog = null;

    private String filepath, newFile;
    ArrayList<String> encodedImageList,encodedImageList2;
    private JSONObject jsonObject;
    int count = 0;

    private List<VideoInfo> videoInfos;
    public ImageView imageView2;
    public static int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallary);
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
        //  video path that was recored in previous activity
        filepath = i.getStringExtra("video_path");
        if (filepath == null) {
            Toast.makeText(VideoGallaryActivity.this, "Some files are missing or have been deleted ", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG,"video file path is : "+filepath);
        videoInfos = new ArrayList<>();
        count++;
        videoInfos.add(new VideoInfo( "Vid Title", filepath));


        myrv = (RecyclerView) findViewById(R.id.gallary_view_id);
        myrv.setLayoutManager(new GridLayoutManager(this, 2));
        myrv.setHasFixedSize(true);
        myrv.setItemViewCacheSize(20);
        myrv.setDrawingCacheEnabled(true);
        myrv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        myAdapter = new VideoViewAdapter(VideoGallaryActivity.this,videoInfos );
        myAdapter.setListener(VideoGallaryActivity.this);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.video_gallary_fragment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            Intent intent2=new Intent(this, VideoActivity.class);
            intent2.putExtra("extra",true);
            startActivityForResult(intent2,Config.CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
            return true;
        } else if (id == R.id.action_picture) {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

            intent.setType("video/*");
            startActivityForResult(Intent.createChooser(intent, "Choose Video"), Config.FILE_VIDEO_REQUEST_CODE);
            return true;
        } else if (id == R.id.upload_pictures) {
            upload();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG,"value of request code  : "+requestCode);
        Log.d(TAG,"value of result code  : "+resultCode);
        if(requestCode==Config.CAMERA_CAPTURE_VIDEO_REQUEST_CODE && resultCode==1){
            filepath=data.getStringExtra("filepath");
            videoInfos.add(new VideoInfo( "Vid Title", filepath));
            count++;
            myAdapter.notifyItemInserted(count);

        }
        else if(requestCode==Config.FILE_VIDEO_REQUEST_CODE && resultCode==RESULT_OK){
            if (data.getData() != null) {
                Uri mVideoUri = data.getData();
                // Get the cursor
                String[] filePathColumn = {MediaStore.Video.Media.DATA};
                Cursor cursor = getContentResolver().query(mVideoUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filepath = cursor.getString(columnIndex);
                cursor.close();
                String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HHmmss",
                        Locale.getDefault()).format(new Date());
                int random = new Random().nextInt(88888888) + 10000000;
                timeStamp=timeStamp+"_"+random+".jpg";
                videoInfos.add(new VideoInfo("title",filepath));
                count++;
                myAdapter.notifyItemInserted(count);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAddClick() {
        imageView2.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCancelClick() {
        imageView2.setVisibility(View.GONE);
    }

    public void onDelete(View view) {
        ArrayList<Integer> deletable = myAdapter.selected;
        Collections.sort(deletable);
        Log.d("Size ", Integer.toString(deletable.size()));
        for (int i = deletable.size() - 1; i >= 0; i--) {
            videoInfos.remove(deletable.get(i).intValue());
            Log.d("deletable index", Integer.toString(deletable.get(i)));
            Log.d("Size remained ", Integer.toString(videoInfos.size()));

        }
        Integer min = Collections.min(deletable);
        Integer max = Collections.max(deletable);
        myAdapter.notifyItemRangeRemoved(min, max - min + 1);
        myAdapter.notifyItemRangeChanged(min, videoInfos.size() - min);
        deletable.clear();
        myAdapter.selected.clear();
        myAdapter.mode = "view";
        imageView2.setVisibility(View.GONE);
    }
    void upload(){
        ProgressDialog progress;
        progress = new ProgressDialog(VideoGallaryActivity.this);
        progress.setTitle("Uploading");
        progress.setMessage("Please wait...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                File f  = new File(filepath);
                String content_type  = getMimeType(f.getPath());
                String file_path = f.getAbsolutePath();
                OkHttpClient client = new OkHttpClient();
                RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                RequestBody request_body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type",content_type)
                        .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                        .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(Config.VIDEO_UPLOAD_URL)
                        .post(request_body)
                        .build();

                try {
                    okhttp3.Response response=client.newCall(request).execute();
                    //Response response = client.newCall(request).execute();
                    if(!response.isSuccessful()){

                        Log.d(TAG," error :"+response);
                        throw new IOException("Error : "+response);


                    }
                    progress.dismiss();
                    //Toast.makeText(getBaseContext(),"error :"+response,Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }
    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

}
