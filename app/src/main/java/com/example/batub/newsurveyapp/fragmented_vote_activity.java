package com.example.batub.newsurveyapp;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
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
    ImageButton closeButton2 ;

    FloatingActionButton voteButton;

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
    //////////////////////////////////////////////////////////7
    Fragment fragment;
    FragmentTransaction transaction;
    FragmentManager manager;

    FrameLayout myContainer , container2;

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

                    // boolTestView.setVisibility(View.VISIBLE);
                    voteButton.setVisibility(View.VISIBLE);

                } else if ( votingRangeMills < timeDiff ){
                    Log.e("ontime","false");
                    onTime=false;
                    // boolTestView.setVisibility(View.GONE);
                    voteButton.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;

        myContainer =findViewById(R.id.container1);

        container2 = findViewById(R.id.container2);
        container2.setVisibility(View.GONE);

        fragment = new votingFragment();

        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(onTime)
                {
                    voteButton.setVisibility(View.GONE);

                    manager = getSupportFragmentManager();

                    transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_left,R.anim.slide_right,R.anim.slide_right,R.anim.slide_right);

                    transaction.add(R.id.container1, fragment,"myFrag");
                    transaction.addToBackStack(null);
                    transaction.commit();

                    container2.setVisibility(View.VISIBLE);
                }
            }
        });

        View simpleExoPlayerView=findViewById(R.id.player_view);

        simpleExoPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("on touch","video player touched?");

            }
        });

    }

    public void closeFragment(View view){


        View containerview2 = findViewById(R.id.container2);
        containerview2.setVisibility(View.GONE);
        onBackPressed();
      //  voteButton.setVisibility(View.VISIBLE);

        /*View containerview = findViewById(R.id.container1);
        FragmentTransaction transaction2=manager.beginTransaction();
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.slide_right);
        containerview.startAnimation(animation);
        containerview.setVisibility(View.GONE);
        transaction2.remove(fragment);
        manager.popBackStack();
        transaction2.commit();
        voteButton.setVisibility(View.VISIBLE);
            //////////////////////////////////////
        voteButton.setVisibility(View.GONE);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_left,R.anim.slide_right,R.anim.slide_right,R.anim.slide_right);
        transaction.remove(fragment);
        manager.popBackStack();
        container2.setVisibility(View.GONE);    */
    }

    private void initializePlayer() {

        SimpleExoPlayerView simpleExoPlayerView = findViewById(R.id.player_view);
        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        simpleExoPlayerView.setPlayer(player);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL); //fill video full screen

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        voteButton.setVisibility(View.VISIBLE);

        Log.e("backpressed","OnBackPressed");
    }
}


/*if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }*/