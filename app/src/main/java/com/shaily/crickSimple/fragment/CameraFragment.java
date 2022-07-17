package com.shaily.crickSimple.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.shaily.crickSimple.R;
import com.shaily.crickSimple.databinding.FragmentCameraBinding;
import com.shaily.crickSimple.widget.AutoFitTextureView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends CameraVideoFragment {
FragmentCameraBinding binding;
    private String mOutputFilePath;
    AutoFitTextureView mTextureView;
    ImageView mRecordVideo;
    VideoView mVideoView;
    ImageView mPlayVideo;

    public CameraFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false);
        // mTextureView = binding.mTextureView;
         mRecordVideo = binding.mRecordVideo;
         mVideoView = binding.mVideoView;
         mPlayVideo = binding.mPlayVideo;

         binding.mRecordVideo.setOnClickListener(view -> {
             /**
              * If media is not recoding then start recording else stop recording
              */
             if (mIsRecordingVideo) {
                 try {
                     stopRecordingVideo();
                     prepareViews();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             } else {
                 startRecordingVideo();
                 mRecordVideo.setImageResource(R.drawable.ic_rec);
                 //Receive out put file here
                 mOutputFilePath = getCurrentFile().getAbsolutePath();
             }
         });

        binding.mPlayVideo.setOnClickListener(view -> {
            mVideoView.start();
            mPlayVideo.setVisibility(View.GONE);
        });

        return binding.getRoot();
    }
    @Override
    public int getTextureResource() {
        return R.id.mTextureView;
    }

 /*   @OnClick({R.id.mRecordVideo, R.id.mPlayVideo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mRecordVideo:
                *//**
                 * If media is not recoding then start recording else stop recording
                 *//*
                if (mIsRecordingVideo) {
                    try {
                        stopRecordingVideo();
                        prepareViews();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    startRecordingVideo();
                    mRecordVideo.setImageResource(R.drawable.ic_rec);
                    //Receive out put file here
                    mOutputFilePath = getCurrentFile().getAbsolutePath();
                }
                break;
            case R.id.mPlayVideo:
                mVideoView.start();
                mPlayVideo.setVisibility(View.GONE);
                break;
        }
    }*/
    private void prepareViews() {
        if (mVideoView.getVisibility() == View.GONE) {
            mVideoView.setVisibility(View.VISIBLE);
            mPlayVideo.setVisibility(View.VISIBLE);
            mTextureView.setVisibility(View.GONE);
            setMediaForRecordVideo();
        }
    }
    private void setMediaForRecordVideo() {
        // Set media controller
        mVideoView.setMediaController(new MediaController(getActivity()));
        mVideoView.requestFocus();
        mVideoView.setVideoPath(mOutputFilePath);
        mVideoView.seekTo(100);
        mVideoView.setOnCompletionListener(mp -> {
            // Reset player
            mVideoView.setVisibility(View.GONE);
            mTextureView.setVisibility(View.VISIBLE);
            mPlayVideo.setVisibility(View.GONE);
            mRecordVideo.setImageResource(R.drawable.recr);
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}