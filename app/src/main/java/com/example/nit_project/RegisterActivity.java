package com.example.nit_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
     ProgressDialog progress;
     AlertDialog alertDialog;
     ImageView imageView;
    String encodedImage;
    EditText Userid,Firstname,Middlename,Lastname,Email,Contactnumber,Batchnumber,Address,Rank;
    String userid,firstname,middlename,lastname,email,contactnumber,batchnumber,address,rank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Userid=findViewById(R.id.reg_userId);
        Firstname=findViewById(R.id.reg_firstnameId);
        Middlename=findViewById(R.id.reg_middlenameId);
        Lastname=findViewById(R.id.reg_lastnameId);
        Email=findViewById(R.id.reg_emailId);
        Contactnumber=findViewById(R.id.reg_contactId);
        Rank=findViewById(R.id.reg_rankId);
        Batchnumber=findViewById(R.id.reg_batchId);
        Address=findViewById(R.id.reg_addressId);
        alertDialog=new AlertDialog.Builder(RegisterActivity.this).create();
        progress = new ProgressDialog(RegisterActivity.this);
        imageView=findViewById(R.id.imageViewId);



    }
    public void Onreg_reset(View view) {
        Userid.setText("");
        Firstname.setText("");
        Middlename.setText("");
        Lastname.setText("");
        Email.setText("");
        Contactnumber.setText("");
        Rank.setText("");
        Batchnumber.setText("");
        Address.setText("");
    }


    public void Goback(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void Onsubmit(View view) {
        //another_function();

        userid=Userid.getText().toString().trim();
        firstname=Firstname.getText().toString().trim();
        middlename=Middlename.getText().toString().trim();
        lastname=Lastname.getText().toString().trim();
        email=Email.getText().toString().trim();
        contactnumber=Contactnumber.getText().toString().trim();
        batchnumber=Batchnumber.getText().toString().trim();
        address=Address.getText().toString().trim();
        rank=Rank.getText().toString().trim();

        if(userid.equals("")||firstname.equals("")||lastname.equals("")||email.equals("")||contactnumber.equals("")||batchnumber.equals("")||address.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please fill in all the fields ! ",Toast.LENGTH_SHORT).show();
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()==false){
            alertDialog.setTitle("Invalid Email !");
            alertDialog.setMessage("Please Enter a valid email");
            alertDialog.show();
        }
        else if(contactnumber.length()!=10){
            alertDialog=new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Invalid Phone number !");
            alertDialog.setMessage("Please Enter a valid Phone number");
            alertDialog.show();
        }
        else{
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put(Config.KEY_USERID,userid);
                jsonObject.put(Config.KEY_FIRSTNAE,firstname);
                jsonObject.put(Config.KEY_MIDDLENAME,middlename);
                jsonObject.put(Config.KEY_LASTNAME,lastname);
                jsonObject.put(Config.KEY_EMAIL,email);
                jsonObject.put(Config.KEY_CONTACTNUMBER,contactnumber);
                jsonObject.put(Config.KEY_BATCHNUMBER,batchnumber);
                jsonObject.put(Config.KEY_ADDRESS,address);
                jsonObject.put(Config.KEY_RANK,rank);
                jsonObject.put("image",encodedImage);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progress.setTitle("Submitting");
            progress.setMessage("Please wait...");
            progress.show();
            Log.d(" json object is : ",jsonObject.toString());
            JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.POST, Config.REGISTER_URL,jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject1) {
                            progress.dismiss();
                            String response="";
                            try {
                                 response=jsonObject1.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (response.contains("Plese Check your email to confirm !")) {
                                AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage(response+"\n"+email)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                        });
                                AlertDialog alert2=builder.create();
                                alert2.show();
                            }
                            else if(response.contains("This Email is  already registred !")){
                                alertDialog.setTitle("Duplicate Email");
                                alertDialog.setMessage(response);
                                alertDialog.show();
                            }
                            else if(response.contains("This Userid is already taken !")){
                                alertDialog.setTitle("Dupicate Userid");
                                alertDialog.setMessage("This Userid is already taken !");
                                alertDialog.show();
                            }
                            else {
                                alertDialog.setTitle("Error");
                                alertDialog.setMessage(response);
                                alertDialog.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
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
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(200 * 30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(jsonObjectRequest);
        };


    }

    /*void another_function(){
        another_function();

        userid=Userid.getText().toString().trim();
        firstname=Firstname.getText().toString().trim();
        middlename=Middlename.getText().toString().trim();
        lastname=Lastname.getText().toString().trim();
        email=Email.getText().toString().trim();
        contactnumber=Contactnumber.getText().toString().trim();
        batchnumber=Batchnumber.getText().toString().trim();
        address=Address.getText().toString().trim();
        rank=Rank.getText().toString().trim();

        if(userid.equals("")||firstname.equals("")||lastname.equals("")||email.equals("")||contactnumber.equals("")||batchnumber.equals("")||address.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please fill in all the fields ! ",Toast.LENGTH_SHORT).show();
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()==false){
            alertDialog.setTitle("Invalid Email !");
            alertDialog.setMessage("Please Enter a valid email");
            alertDialog.show();
        }
        else if(contactnumber.length()!=10){
            alertDialog=new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Invalid Phone number !");
            alertDialog.setMessage("Please Enter a valid Phone number");
            alertDialog.show();
        }
        else{
            progress.setTitle("Submitting");
            progress.setMessage("Please wait...");
            progress.show();
            StringRequest stringRequest =new JsonObjectRequest(Request.Method.POST, Config.REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress.dismiss();
                            if (response.contains("Plese Check your email to confirm !")) {
                                AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage(response+"\n"+email)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                        });
                                AlertDialog alert2=builder.create();
                                alert2.show();
                            }
                            else if(response.contains("This Email is  already registred !")){
                                alertDialog.setTitle("Duplicate Email");
                                alertDialog.setMessage(response);
                                alertDialog.show();
                            }
                            else if(response.contains("This Userid is already taken !")){
                                alertDialog.setTitle("Dupicate Userid");
                                alertDialog.setMessage("This Userid is already taken !");
                                alertDialog.show();
                            }
                            else {
                                alertDialog.setTitle("Error");
                                alertDialog.setMessage(response);
                                alertDialog.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
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
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params=new HashMap<String, String>();
                    params.put(Config.KEY_USERID,userid);
                    params.put(Config.KEY_FIRSTNAE,firstname);
                    params.put(Config.KEY_MIDDLENAME,middlename);
                    params.put(Config.KEY_LASTNAME,lastname);
                    params.put(Config.KEY_EMAIL,email);
                    params.put(Config.KEY_CONTACTNUMBER,contactnumber);
                    params.put(Config.KEY_BATCHNUMBER,batchnumber);
                    params.put(Config.KEY_ADDRESS,address);
                    params.put(Config.KEY_RANK,rank);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders()  {
                    Map<String,String> headers=new HashMap<String,String>();
                    headers.put("User-Agent","noobie");
                    return headers;
                }

            });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }





    }*/

    public void uploadPhoto(View view) {
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Choose picture"), Config.FILE_IMAGE_REQUEST_CODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==Config.FILE_IMAGE_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                if(data!=null){
                    Uri mImageUri = data.getData();
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filepath = cursor.getString(columnIndex);
                    cursor.close();
                    String newFile = compressImage(filepath);

                    BitmapFactory.Options options=new BitmapFactory.Options();
                    options.inJustDecodeBounds=true;
                    Bitmap bitmap=null;
                    bitmap=BitmapFactory.decodeFile(newFile);
                    imageView.setImageBitmap(bitmap);
                    Uri newUri=Uri.fromFile(new File(newFile));
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), newUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        String timeStamp = new SimpleDateFormat("dd_MM_yyyy_HHmmss",
                Locale.getDefault()).format(new Date());
        String uriSting = (file.getAbsolutePath() + "/"+ timeStamp+ "_"+".jpg");
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
