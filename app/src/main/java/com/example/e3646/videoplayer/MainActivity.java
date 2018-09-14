package com.example.e3646.videoplayer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private VideoView mVideoView;
    private ViewGroup.LayoutParams mVideoViewLayoutParams;
    private RelativeLayout mVideoLayout;

    private MediaController mMediaController;
    private ConstraintLayout mController;

    private SeekBar mSeekBar;

    private ToggleButton mPlayButton;
    private ImageButton mForwardButton;
    private ImageButton mRewindButton;

    private ToggleButton mVoluneButton;
    private ToggleButton mFullScreenButton;

    private TextView mProgressTime;
    private TextView mTotalTime;

    private float mVideoWidth;
    private float mVideoHeight;

    private Timer mTimer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimer = new Timer();

        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoLayout = (RelativeLayout)findViewById(R.id.video_layout);

//        mController = new Controller(mVideoView, this);
//        mVideoView.setMediaController(mController);

//        mMediaController = new MediaController(this);
//        mVideoView.setMediaController(mMediaController);

        mVideoView.setVideoURI(Uri.parse("https://s3-ap-northeast-1.amazonaws.com/mid-exam/Video/taeyeon.mp4"));
        mVideoView.start();

        ////////////////////////////////////////////////////////////

        mPlayButton = (ToggleButton) findViewById(R.id.button_play);
        mPlayButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean play) {
                int currentPosition = mVideoView.getCurrentPosition();
                if (!play) {

                    mVideoView.pause();
                } else {
                    mVideoView.seekTo(currentPosition + 1000);
                    mVideoView.start();
                }
                mPlayButton.setButtonDrawable(play ? R.drawable.ic_play_arrow : R.drawable.ic_pause);
            }
        });

        ////////////////////////////////////////////////////////////

        mForwardButton = (ImageButton) findViewById(R.id.button_fast_forward);
        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPostition = mVideoView.getCurrentPosition();
                mVideoView.seekTo(currentPostition + 25000);
            }
        });

        mRewindButton = (ImageButton) findViewById(R.id.button_fast_rewind);
        mRewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPostition = mVideoView.getCurrentPosition();
                mVideoView.seekTo(currentPostition - 25000);
            }
        });

        ////////////////////////////////////////////////////////////

        mVoluneButton = (ToggleButton) findViewById(R.id.button_volume);
        mVoluneButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {

                mVoluneButton.setButtonDrawable(b ? R.drawable.ic_volume_on : R.drawable.ic_volume_off);
            }
        });

        ////////////////////////////////////////////////////////////

        mProgressTime = (TextView) findViewById(R.id.progress_time);
        mTotalTime = (TextView) findViewById(R.id.total_time);
        mSeekBar = (SeekBar) findViewById(R.id.progress_bar);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

                        mSeekBar.setMax(mVideoView.getDuration());
                        mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                        Log.d("duration", ":" + mVideoView.getDuration());
                        Log.d("current", ":" + mVideoView.getCurrentPosition());

                        String progressTime = tranformTime(mVideoView.getCurrentPosition());
                        String totalTime = tranformTime(mVideoView.getDuration());

                        mProgressTime.setText(progressTime);
                        mTotalTime.setText(totalTime);
                    }
                });

//                mVideoHeight = mediaPlayer.getVideoWidth();
//                mVideoHeight = mediaPlayer.getVideoHeight();
//
//                int w = mVideoView.getWidth();
//                int h = mVideoView.getHeight();

            }
        });

        ////////////////////////////////////////////////////////////

        mController = (ConstraintLayout)findViewById(R.id.controller);
        mFullScreenButton = (ToggleButton)findViewById(R.id.button_fullscreen);
        mFullScreenButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                mFullScreenButton.setButtonDrawable(b ? R.drawable.ic_fullscreen_exit : R.drawable.ic_fullscreen);
            }
        });

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        Log.d("rotate", "rotate");
        mVideoViewLayoutParams = mVideoLayout.getLayoutParams();

        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mVideoLayout.setLayoutParams(layoutParams);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            mVideoLayout.setLayoutParams(mVideoViewLayoutParams);
        }
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {


        switch (requestedOrientation) {
            case(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE):
                break;
            case(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT):
                break;
        }
        super.setRequestedOrientation(requestedOrientation);
    }



    private String tranformTime(int time) {
        time /= 1000;
        int min = (time % 3600) / 60;
        int sec = time % 60;

        String mm = String.valueOf(min);
        if (min < 10) {
            mm = "0" + mm;
        }
        String ss = String.valueOf(sec);
        if (sec < 10) {
            ss = "0" + ss;
        }
        Log.d("tranfrom time", min + "_" + sec);
        String timeFormat = mm + ":" + ss;
        return timeFormat;
    }

    public void setmTimerTAsk() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mController.setVisibility(View.INVISIBLE);

            }
        },3000);
    }

}

