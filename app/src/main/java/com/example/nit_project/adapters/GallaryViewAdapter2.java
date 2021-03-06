package com.example.nit_project.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nit_project.ClickableActivity.SecondActivity;
import com.example.nit_project.ClickableActivity.ThirdActivity;
import com.example.nit_project.Config;
import com.example.nit_project.R;
import com.example.nit_project.activities.ImageInfo;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class GallaryViewAdapter2 extends RecyclerView.Adapter<GallaryViewAdapter2.MyViewHolder> {

    private String filepath;
    private Context mContext;
    private List<ImageInfo> mData;
    public ArrayList<Integer> selected;
    public String mode = "view";
    public int item_selected = 0;
    private Callback listener;
    public JSONArray jsonArray=null;


    public GallaryViewAdapter2(Context mContext, List<ImageInfo> mData,JSONArray k) {
        this.mContext = mContext;
        this.mData = mData;
        this.jsonArray=k;
    }

    public void setListener(Callback listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.gallary_grid_view, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        filepath = mData.get(position).getFilepath();
        Log.d(TAG,"filepath in adapter is "+filepath);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((ViewGroup.LayoutParams.MATCH_PARENT), (ViewGroup.LayoutParams.MATCH_PARENT) - 550);
        holder.cardView_id.setLayoutParams(params);

        if (mData.get(position).getIsImage()) {

            holder.img_title.setText(mData.get(position).getTitle());

            int imageWidth = holder.img_id.getMaxWidth();
            int imageHeight = holder.img_id.getMaxHeight();


            RequestOptions requestOptions= new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
            //setting image
            String image_url= Config.DB_URL+"AndroidFileUpload/uploads/"+mData.get(position).getFilepath();

            Glide.with(mContext).load(image_url).apply(requestOptions).into(holder.img_id);
           // holder.img_id.setImageBitmap(bitmap);
            holder.checked_button.setVisibility(View.GONE);
        }

        holder.cardView_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("view")) {
                    Intent intent = new Intent(mContext, ThirdActivity.class);


                    intent.putExtra("json_array",jsonArray.toString());
                    intent.putExtra("image_url",mData.get(position).getFilepath());
                    // start the activity
                    mContext.startActivity(intent);
                } else {
                    if (holder.checked_button.getVisibility() == View.VISIBLE) {
                        item_selected--;
                        selected.remove(new Integer(position));
                        holder.checked_button.setVisibility(View.GONE);
                    } else {
                        item_selected++;
                        selected.add(position);
                        holder.checked_button.setVisibility(View.VISIBLE);
                    }
                    if (item_selected == 0) {
                        mode = "view";
                        selected.clear();
                        listener.onCancelClick();
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView checked_button;
        TextView img_title;
        TextView vid_title;
        ImageView img_id;
        CardView cardView_id;
        VideoView vid_id;

        public MyViewHolder(View itemView) {
            super(itemView);

            img_title = (TextView) itemView.findViewById(R.id.img_title_id);
            img_id = (ImageView) itemView.findViewById(R.id.img_id);
            cardView_id = (CardView) itemView.findViewById(R.id.cardview_id);
            checked_button = itemView.findViewById(R.id.checked_button);
        }
    }

    public interface Callback {
        public void onAddClick();

        public void onCancelClick();
    }


}