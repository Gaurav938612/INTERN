package com.example.nit_project.camera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.example.nit_project.Config;
import com.example.nit_project.R;
import com.example.nit_project.activities.VideoGallaryActivity;
import java.io.File;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends CameraVideoFragment {

    private static final String TAG = "CameraFragment";
    private static final String VIDEO_DIRECTORY_NAME = "AndroidWave";
    @BindView(R.id.mTextureView)
    AutoFitTextureView mTextureView;
    @BindView(R.id.mRecordVideo)
    ImageView mRecordVideo;
    @BindView(R.id.mVideoView)
    VideoView mVideoView;
    @BindView(R.id.mOk)
    Button mOk;
    @BindView(R.id.mReset)
    Button mReset;

    Unbinder unbinder;
    private String mOutputFilePath;
    private Uri uri=null;


    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */


    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public int getTextureResource() {
        return R.id.mTextureView;
    }

    @Override
    protected void setUp(View view) {

    }

    @OnClick(R.id.mRecordVideo)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mRecordVideo:
                /**
                 * If media is not recoding then start recording else stop recording
                 */
                if (mIsRecordingVideo) {
                    try {
                        stopRecordingVideo();
                        Intent intent1=this.getActivity().getIntent();
                        if(intent1.getBooleanExtra("extra",false)){
                            Log.d(TAG,"we reached here");
                            intent1.putExtra("filepath",mOutputFilePath);
                            Intent intent2=new Intent();
                            intent2.putExtra("filepath",mOutputFilePath);
                            this.getActivity().setResult(1,intent2);
                            this.getActivity().finish();
                        }
                        else {
                            Intent intent=new Intent(getActivity(), VideoGallaryActivity.class);
                            intent.putExtra("video_path",mOutputFilePath);
                            startActivity(intent);
                            this.getActivity().finish();
                        }


                        //prepareViews();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    startRecordingVideo();
                    mRecordVideo.setImageResource(R.drawable.ic_stop_button);
                    mOutputFilePath=give_filepath();
                    Log.d(TAG,"filepath in camera is "+mOutputFilePath);
                }
                break;
        }
    }

    private void prepareViews() {
        mTextureView.setVisibility(View.GONE);
        mVideoView.setVisibility(View.VISIBLE);
        mRecordVideo.setVisibility(View.GONE);
        mOk.setVisibility(View.VISIBLE);
        mReset.setVisibility(View.VISIBLE);
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), VideoGallaryActivity.class);
                intent.putExtra("video_path",mOutputFilePath);
                startActivity(intent);
            }
        });

        try {
            setMediaForRecordVideo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMediaForRecordVideo() throws IOException {
        //mOutputFilePath = parseVideo(mOutputFilePath);
        // Set media controller
        MediaController m=new MediaController(getActivity());
        mVideoView.setMediaController(m);
        m.show(0);
        mVideoView.requestFocus();
        mVideoView.setVideoPath(mOutputFilePath);
        mVideoView.seekTo(100);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}