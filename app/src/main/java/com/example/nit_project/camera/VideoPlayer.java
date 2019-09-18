package com.example.nit_project.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.nit_project.R;

public class VideoPlayer extends AppCompatActivity {
    private VideoView mVideoView;
    String filepath=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        mVideoView=findViewById(R.id.videoPlayer);
        Intent intent=getIntent();
        filepath=intent.getStringExtra("filepath");
        MediaController m=new MediaController(this);
        mVideoView.setMediaController(m);
        m.show(0);
        mVideoView.requestFocus();
        mVideoView.setVideoPath(filepath);
        mVideoView.seekTo(100);

    }
}
