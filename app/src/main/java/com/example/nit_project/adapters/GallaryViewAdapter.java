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

import com.example.nit_project.ClickableActivity.SecondActivity;
import com.example.nit_project.R;
import com.example.nit_project.activities.ImageInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class GallaryViewAdapter extends RecyclerView.Adapter<GallaryViewAdapter.MyViewHolder> {

    private String filepath;
    private Context mContext;
    private List<ImageInfo> mData;
    public ArrayList<Integer> selected;
    public String mode = "view";
    public int item_selected = 0;
    private Callback listener;


    public GallaryViewAdapter(Context mContext, List<ImageInfo> mData) {
        this.mContext = mContext;
        this.mData = mData;
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

            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;

            Bitmap bitmap=decodeFile(filepath);
            bitmap=getResizedBitmap(bitmap,imageWidth,imageHeight);
            holder.img_id.setImageBitmap(bitmap);
            holder.checked_button.setVisibility(View.GONE);
        }

        holder.cardView_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("view")) {
                    Intent intent = new Intent(mContext, SecondActivity.class);


                    intent.putExtra("filepath", mData.get(position).getFilepath());
                    intent.putExtra("image_url",mData.get(position).getDescription());
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
        holder.cardView_id.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mode.equals("view")) {
                    item_selected = 0;
                    mode = "delete";
                    selected = new ArrayList<>();
                    selected.add(position);
                    item_selected++;
                    holder.checked_button.setVisibility(View.VISIBLE);
                    listener.onAddClick();
                } else {
                    holder.checked_button.setVisibility(View.VISIBLE);
                    item_selected++;
                    selected.add(position);
                }
                return true;
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

    private static Bitmap getResizedBitmap(Bitmap bitmap, float maxWidth, float maxHeight) {

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
        Log.e(TAG, "width : " + width + "height : " + height);
        return Bitmap.createScaledBitmap(bitmap, (int) width, (int) height, true);

    }

    public Bitmap decodeFile(String pathName) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        for (options.inSampleSize =1; options.inSampleSize <= 32; options.inSampleSize++) {
            try {
                bitmap = BitmapFactory.decodeFile(pathName, options);
                Log.d(TAG, "Decoded successfully for sampleSize " + options.inSampleSize);
                break;
            } catch (OutOfMemoryError outOfMemoryError) {
                Log.e(TAG, "outOfMemoryError while reading file for sampleSize " + options.inSampleSize
                        + " retrying with higher value");
            }
        }
        return bitmap;
    }





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
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = mContext.getContentResolver().query(contentUri, null, null, null, null);
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
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
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
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
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