package com.example.e3646.videoplayer;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

public class Controller extends MediaController {

    private Context mContext;
    private VideoView mVideoView;
    private Activity mActivity;
    private View mView;

    private SeekBar mSeekBar;
    private ImageButton mPlayButton;


    public Controller(VideoView videoView, Activity activity) {
        super(activity);
        this.mVideoView = videoView;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);
        mView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, null);

        try {
            Field mRoot = MediaController.class.getDeclaredField("mRoot");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        removeAllViews();

    }



}
