package com.example.nit_project;

public class Config {
    public static final String DB_URL="http://noobieuser.000webhostapp.com/";
    public static final String REGISTER_URL=DB_URL+"register.php";
    public static final String LOGIN_URL=DB_URL+"login.php";
    public static final String Edit_url=DB_URL+"AndroidFileUpload/edit_case.php";

    public static final String KEY_USERID="userid";

    //public static final String KEY_PASSWORD="password";
    public static final String KEY_FIRSTNAE="firstname";
    public static final String KEY_MIDDLENAME="middlename";
    public static final String KEY_LASTNAME="lastname";
    public static final String KEY_CONTACTNUMBER="contactnumber";
    public static final String KEY_BATCHNUMBER="batchnumber";
    public static final String KEY_ADDRESS="address";
    public static final String KEY_EMAIL="email";
    public static final String KEY_RANK="rank";
    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = "http://noobieuser.000webhostapp.com/AndroidFileUpload/fileUpload.php";
    public static final String VIDEO_UPLOAD_URL = "http://noobieuser.000webhostapp.com/AndroidFileUpload/videoUpload.php";
    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "AndroidFileUpload";
    public static final String urlUpload = "http://noobieuser.000webhostapp.com/AndroidFileUpload/multiple.php";
    public static final int REQCODE = 100;
    public static final String imageList = "imageList";
    public static final String imageName = "gaurav";
    // Camera activity request codes
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int FILE_IMAGE_REQUEST_CODE= 300;
    public static final int FILE_VIDEO_REQUEST_CODE= 400;
    public static final int ACCESS_LOCATION_CODE= 500;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
}
