package com.example.batub.newsurveyapp;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class fragmented_vote_activity extends AppCompatActivity {


    private SimpleExoPlayer player;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;

    /////////////////////////////////////////////////////////////

    FloatingActionButton voteButton;

    TextView boolTestView;

    TextView questionView , timeText ;
    Button vote1Button, vote2Button;

    FirebaseDatabase voteDatabase;
    DatabaseReference questionReference , voteCountReference, progressBarReference ;

    String question, option1,option2 ;

    Long vote1Count,vote2Count ;

    Boolean onTime;
    boolean answered;
    boolean timerFirstTime;

    static Double d3;
    static Double d4;

    long timeLeft;

    ProgressBar percent1Progress,percent2Progress;

    TextView vote1BarPercent, vote2BarPercent ;
    TextView vote1ResultText ,vote2ResultText;

    SharedPreferences preferences;

    AlertDialog dialog ,dialog2 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmented_vote_activity_layout);

        /////////////////// EXOPLAYER
        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        Timeline.Window window = new Timeline.Window();
        ImageView ivHideControllerButton = (ImageView) findViewById(R.id.exo_controller);


        boolTestView= (TextView) findViewById(R.id.booltesttext) ;
        voteButton = (FloatingActionButton) findViewById(R.id.fab5);


        ////////////////// DATABASE REFERENCES

        voteDatabase= FirebaseDatabase.getInstance();
        questionReference = voteDatabase.getReference().child("deneme");
        voteCountReference = voteDatabase.getReference().child("votecount");

        /////////// ON TIME CALCULATION
        questionReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Long enterTime = dataSnapshot.child("enter_time").getValue(Long.class);
                Long votingRangeMin = dataSnapshot.child("minute_range").getValue(Long.class);

                long votingRangeMills = ( votingRangeMin * 60000 );

                long phoneTime = Calendar.getInstance().getTimeInMillis();

                long timeDiff =  (phoneTime - enterTime);

                if (votingRangeMills > timeDiff) {
                    onTime = true;
                    Log.e("ontime","true");

                    boolTestView.setVisibility(View.VISIBLE);
                    voteButton.setVisibility(View.VISIBLE);

                } else if ( votingRangeMills < timeDiff ){
                    Log.e("ontime","false");
                    onTime=false;
                    boolTestView.setVisibility(View.GONE);
                    voteButton.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;


    }


    private void initializePlayer() {

        SimpleExoPlayerView simpleExoPlayerView = findViewById(R.id.player_view);
        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);
/*        MediaSource mediaSource = new HlsMediaSource(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),
                mediaDataSourceFactory, mainHandler, null);*/

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"),
                mediaDataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource);


    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


}


