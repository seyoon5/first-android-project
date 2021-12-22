package com.example.test.YouTube;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.test.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class UtubePlay extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView ytpv;
    private YouTubePlayer ytp;
    final String serverKey="AIzaSyD0HhKqtuUPkcJjViED8bs53FfjXKSxYRU"; // YouTube API 키

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utube_play);
        ytpv = findViewById(R.id.playerView);
        ytpv.initialize(serverKey, this);
        // 화면에서 동영상 시작하라는 명령
    }



    @Override
    public void onInitializationFailure(YouTubePlayer.Provider arg0,
                                        YouTubeInitializationResult arg1) {
        Toast.makeText(this, "Initialization Fail", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasrestored) {
        ytp = player;
        //영상 시작
        Intent gt = getIntent();
        ytp.loadVideo(gt.getStringExtra("id"));
    }

}
