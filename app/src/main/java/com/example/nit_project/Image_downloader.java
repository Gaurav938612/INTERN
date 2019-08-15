package com.example.nit_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Image_downloader extends AsyncTask<String, Void, Bitmap> {
    Bitmap bitmap=null;
    View view=null;
    Callback callback;
    public Image_downloader (View view, Context context){
        this.view=view;
        callback=(Callback)context;
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
       bitmap=result;
        ImageView imageView=view.findViewById(R.id.imageView);
        imageView.setImageBitmap(result);
        callback.notify_on_execute(result);
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
    public interface Callback{
        public void notify_on_execute(Bitmap bitmap);
    }
}
