package com.example.batub.newsurveyapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;




public class final_demo extends Activity {



    private SimpleExoPlayer player;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;

    //////////////////////////////////////////

    FloatingActionButton voteButton;





    FirebaseDatabase voteDatabase;
    DatabaseReference questionReference , voteCountReference, progressBarReference ;

     String question, option1,option2 ;

    Long vote1Count,vote2Count ;

    TextView questionView , timeText ;
    Button vote1Button, vote2Button;

    Boolean onTime;
    boolean answered;
    boolean timerFirstTime;
    SharedPreferences preferences;

    static Double d3;
    static Double d4;

    long timeLeft;

    ProgressBar percent1Progress,percent2Progress;

    TextView vote1BarPercent, vote2BarPercent ;
    TextView vote1ResultText ,vote2ResultText;



    AlertDialog dialog ,dialog2 ;


    public void openResultDialog() {

        AlertDialog.Builder myBuilder2 = new AlertDialog.Builder(final_demo.this);
        View myView2 = getLayoutInflater().inflate(R.layout.vote_result_layout,null);

        vote1ResultText = (TextView)  myView2.findViewById(R.id.vote1ResultText);
        vote2ResultText = (TextView)  myView2.findViewById(R.id.vote2ResultText);

        percent1Progress = (ProgressBar) myView2.findViewById(R.id.vote1progressbar);
        percent2Progress = (ProgressBar) myView2.findViewById(R.id.vote2progressbar);

        vote1BarPercent = (TextView) myView2.findViewById(R.id.vote1BarPercentText);
        vote2BarPercent = (TextView) myView2.findViewById(R.id.vote2BarPercentText);

        progressBarReference = voteDatabase.getReference().child("votecount").child("1");

        progressBarReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                vote1Count = dataSnapshot.child("vote1").getValue(Long.class);
                vote2Count = dataSnapshot.child("vote2").getValue(Long.class);

                String vote1CountStr = String.valueOf(vote1Count);
                String vote2CountStr=String.valueOf(vote2Count);

               // vote1percent.setText(vote1CountStr);
               // vote2percent.setText( vote2CountStr );

                d3=vote1Count.doubleValue();        //  vote 1 real count convert to double for decimal
                d4=vote2Count.doubleValue();         // vote 2 real count    "      "   "   "       "

                Double d5 = ( (d3)/(d3+d4)) * 100 ;  // vote 1 percentage with decimal
                Double d6 = ( (d4)/(d3+d4)) * 100 ;  // vote 2 percentage   "     "

                int int5 = d5.intValue(); // test & backup
                int int6 = d6.intValue(); // test & backup

                System.out.println(d3); //Track variables
                System.out.println(d4);
                System.out.println(d5);
                System.out.println(d6);

                percent1Progress.setProgress(int5);
                percent2Progress.setProgress(int6);

                vote1BarPercent.setText("%"+String.format("%.2f", d5)  );    // percentage display
                vote2BarPercent.setText("%"+String.format("%.2f", d6)  );    //     "         "

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        questionReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String option1text = dataSnapshot.child("cevap1").getValue(String.class);
                String option2text = dataSnapshot.child("cevap2").getValue(String.class);
                vote1ResultText.setText(option1text);
                vote2ResultText.setText(option2text);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        myBuilder2.setView(myView2);
        dialog2 =myBuilder2.create();

        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog2.show();

        dialog2.getWindow().setLayout( 2000,1000);



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_demo_layout);

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


                            voteButton.setVisibility(View.VISIBLE);

                        } else if ( votingRangeMills < timeDiff ){
                            Log.e("ontime","false");
                            onTime=false;

                            voteButton.setVisibility(View.GONE);
                        }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;

        ///////////////////////// VOTING ALERT DIALOG
        timerFirstTime =true;

            voteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    answered = preferences.getBoolean("isAnswered",false);



                    if(answered){
                        openResultDialog();
                    }

                    /// Get questions and display them

                    if (answered==false){
                        final AlertDialog.Builder myBuilder = new AlertDialog.Builder(final_demo.this);
                        View myView = getLayoutInflater().inflate(R.layout.vote_dialog,null);

                        questionView = (TextView) myView.findViewById(R.id.questionText);
                        vote1Button = (Button) myView.findViewById(R.id.op1votebutton);
                        vote2Button = (Button) myView.findViewById(R.id.op2votebutton);
                        timeText = (TextView) myView.findViewById(R.id.timeText); // count down timer

                        questionReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                 question = dataSnapshot.child("soru").getValue(String.class);
                                option1 = dataSnapshot.child("cevap1").getValue(String.class);
                                 option2 = dataSnapshot.child("cevap2").getValue(String.class);

                                 Long entryTime = dataSnapshot.child("enter_time").getValue(Long.class);
                                 Long minRange = dataSnapshot.child("minute_range").getValue(Long.class);

                                 Long time = Calendar.getInstance().getTimeInMillis();
                                 final long countdownMills = ( minRange *60000 ) -   (time-entryTime) ;

                               Log.e("question",question);
                                Log.e("option1",option1);
                                Log.e("option2",option2);

                               questionView.setText(question);
                                vote1Button.setText(option1);
                                vote2Button.setText(option2);

                                  timeLeft = countdownMills ;

                                if (timerFirstTime) {
                                    new CountDownTimer(timeLeft, 1000){
                                        public void onTick(long millisUntilFinished){
                                            timeText.setText(String.valueOf ( timeLeft/1000 ) );
                                            timeLeft = timeLeft -1000;
                                        }
                                        public  void onFinish(){
                                            dialog.dismiss();
                                            dialog2.dismiss();
                                            voteButton.setVisibility(View.GONE);


                                        }
                                    }.start();

                                    timerFirstTime =false;
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                        myBuilder.setView(myView);
                         dialog =myBuilder.create();

                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        dialog.show();

                        dialog.getWindow().setLayout( 2000,1000);

                        voteCountReference= voteDatabase.getReference().child("votecount").child("1");

                        voteCountReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                vote1Count = dataSnapshot.child("vote1").getValue(Long.class);
                                vote2Count = dataSnapshot.child("vote2").getValue(Long.class);
                                Log.e("vote1", String.valueOf(vote1Count) );
                                Log.e("vote2", String.valueOf(vote2Count) );
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {


                            }
                        });

                        vote1Button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                vote1Count= vote1Count+1;
                                voteCountReference.child("vote1").setValue(vote1Count);

                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putBoolean("isAnswered",true);
                                editor.commit();

                                //answered = true;

                                Toast.makeText(getApplicationContext(),"Answer submitted successfully",Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                                openResultDialog();
                            }
                        });

                        vote2Button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                vote2Count= vote2Count+1;
                                voteCountReference.child("vote2").setValue(vote2Count);

                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putBoolean("isAnswered",true);
                                editor.commit();


                                //answered = true;

                                Toast.makeText(getApplicationContext(),"Answer submitted successfully",Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                                openResultDialog();

                            }
                        });
                    }


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
