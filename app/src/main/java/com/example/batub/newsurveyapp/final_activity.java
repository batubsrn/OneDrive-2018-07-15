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

public class final_activity extends Activity {


    private SimpleExoPlayer player;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;

     TextView votequestionView;
     Button answer1Button ;
     Button answer2Button ;


    FloatingActionButton openQuestionFab;

    TextView progress1Bar,progress2Bar ;
    ProgressBar answer1Bar,answer2Bar;

    FirebaseDatabase myDatabase;

    DatabaseReference getQuestionsRef, getVoteCountRef, getMinuteRef;

    boolean allowedMinute;

    Long enterTime ,minuteRange ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_activity_layout);

        myDatabase=FirebaseDatabase.getInstance();

        getMinuteRef = myDatabase.getReference().child("deneme");
        getQuestionsRef= myDatabase.getReference().child("deneme");
        getVoteCountRef = myDatabase.getReference().child("votecount").child("1");

        getMinuteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                enterTime=dataSnapshot.child("enter_time").getValue(Long.class);
                minuteRange=dataSnapshot.child("minute_range").getValue(Long.class);

                System.out.print(enterTime);
                System.out.print(minuteRange);

                Long currentTime = Calendar.getInstance().getTimeInMillis();

                System.out.print(currentTime);

                long timeDiff = currentTime - enterTime ;

                long rangeInMillisec = minuteRange * 60000;

                System.out.print(enterTime);

                System.out.print(minuteRange);

                System.out.print(rangeInMillisec);

                System.out.print(timeDiff);

                System.out.print(currentTime);

                if(rangeInMillisec > timeDiff ) {
                    allowedMinute = true;

                }else if (rangeInMillisec < timeDiff){
                    allowedMinute=false;
                }


                System.out.print(allowedMinute);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*openQuestionFab = findViewById(R.id.openQuestionFab);

        votequestionView = (TextView) findViewById(R.id.votequestion);
         answer1Button = (Button) findViewById(R.id.vote1button);
         answer2Button = (Button) findViewById(R.id.vote2button);


        openQuestionFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                AlertDialog.Builder mBuilder1=  new AlertDialog.Builder(final_activity.this);
                View mView= getLayoutInflater().inflate(R.layout.vote_dialog,null);


                getQuestionsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       String questionText =dataSnapshot.child("soru").getValue(String.class);
                       String answer1Text= dataSnapshot.child("cevap1").getValue(String.class);
                       String answer2Text= dataSnapshot.child("cevap2").getValue(String.class);

                       Log.e("soru",questionText);
                       Log.e("cevap1",answer1Text);
                       Log.e("cevap2",answer2Text);




                     //  votequestionView.setText(questionText);
                        answer1Button.setText( answer1Text);
                        answer2Button.setText(answer2Text);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                mBuilder1.setView(mView);
                final AlertDialog dialog =mBuilder1.create();

                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

                dialog.show();
                dialog.getWindow().setLayout( 2000,500 );



            }



        });*/



        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        Timeline.Window window = new Timeline.Window();
        ImageView ivHideControllerButton = (ImageView) findViewById(R.id.exo_controller);

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