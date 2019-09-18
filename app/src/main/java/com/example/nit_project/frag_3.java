package com.example.nit_project;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.nit_project.activities.ContentActivity;
import com.example.nit_project.activities.GallaryActivity;
import com.example.nit_project.activities.VideoGallaryActivity;
import com.example.nit_project.camera.VideoActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.nit_project.Config.CAMERA_CAPTURE_IMAGE_REQUEST_CODE;
import static com.example.nit_project.Config.CAMERA_CAPTURE_VIDEO_REQUEST_CODE;

public class frag_3 extends Activity {

    // LogCat tag
    private static final String TAG = frag_3.class.getSimpleName();

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri; // file url to store image/video

    private TextInputLayout caseIdLayout;
    String case_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_3);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); //uri error solve
        StrictMode.setVmPolicy(builder.build());


        // Changing action bar background color
        // These two lines are not needed
        // getActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.action_bar)));

        //  getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.action_bar))));

        // Checking camera availability
        caseIdLayout=findViewById(R.id.caseIdLayout);
    }

    /**
     * Checking device has camera hardware or not
     * */
    private boolean validatecasenumber()
    {
        String string=caseIdLayout.getEditText().getText().toString().trim();
        case_number=string;
        if(string.isEmpty())
        {
            caseIdLayout.setError("Field can't be empty");
            return false;
        }
        else if(string.length()>10)
        {
            caseIdLayout.setError("Case Number too long");
            return false;
        }
        else {
            caseIdLayout.setError(null);
            return true;
        }
    }


    public void GoToContent(View view) {
        if(validatecasenumber()){
            Intent i=new Intent(getApplicationContext(),ContentActivity.class);
            i.putExtra("case_number",case_number);
            startActivity(i);
        }
    }
}
