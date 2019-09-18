package com.example.nit_project.activities;

import android.util.Log;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class VideoInfo {
    private String title;
    private String filepath;
    public VideoInfo(String title, String filepath){
        this.title=title;
        this.filepath=filepath;

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }



}
