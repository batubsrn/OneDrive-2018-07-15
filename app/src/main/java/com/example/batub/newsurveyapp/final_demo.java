package com.example.batub.newsurveyapp;

import android.app.Activity;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class final_demo extends Activity {


    private SimpleExoPlayer player;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;

    //////////////////////////////////////////

    FloatingActionButton voteButton;

    TextView boolTestView;

    TextView questionView,vote1View,vote2View;

    FirebaseDatabase voteDatabase;
    DatabaseReference questionReference , voteCountReference ;

    String question, option1,option2 ;

    Boolean onTime;


    public void calculateMinute(Long phoneTime) {



    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_demo_layout);

        boolTestView= (TextView) findViewById(R.id.booltesttext) ;
        voteButton = (FloatingActionButton) findViewById(R.id.fab5);

        /////////////////// EXOPLAYER
        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        Timeline.Window window = new Timeline.Window();
        ImageView ivHideControllerButton = (ImageView) findViewById(R.id.exo_controller);

        ////////////////// DATABASE REFERENCES

        voteDatabase=FirebaseDatabase.getInstance();
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

        ///////////////////////// VOTING ALERT DIALOG

        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                questionReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        question = dataSnapshot.child("soru").getValue(String.class);
                        option1 = dataSnapshot.child("cevap1").getValue(String.class);
                        option2 = dataSnapshot.child("cevap2").getValue(String.class);

                        Log.e("question",question);
                        Log.e("option1",option1);
                        Log.e("option1",option2);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(final_demo.this);
                View myView = getLayoutInflater().inflate(R.layout.vote_dialog,null);

                questionView = (TextView) findViewById(R.id.questionText);
                vote1View = (TextView) findViewById(R.id.op1votebutton);
                vote2View = (TextView) findViewById(R.id.op2votebutton);


                myBuilder.setView(myView);
                final AlertDialog dialog =myBuilder.create();


                questionView.setText(question);
                vote1View.setText(option1);
                vote2View.setText(option2);


                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                dialog.show();

                dialog.getWindow().setLayout( 2000,1000 );



            }
        });



        ///////////////////////////////////////////



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
