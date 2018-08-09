package com.example.batub.newsurveyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.Calendar;

public class final_activity extends AppCompatActivity {


    String op1count;
    String op2count ;

    private SimpleExoPlayer player;

    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;

    TextView vote1text,vote2text,vote1bartext,vote2bartext;



    FirebaseDatabase myDb;
    DatabaseReference myRef2;
    DatabaseReference myRef3;
    DatabaseReference myRef4;
    DatabaseReference onRangeRef;
    DatabaseReference increment;

    DatabaseReference voteCountRef;

    FloatingActionButton fab;

    Long voteMin , voteEnterTime ,phoneTime, timeDiff;

    static Double d3;
    static Double d4;

    ProgressBar percent1Progress,percent2Progress;

    Long vote1Int,vote2Int;


    boolean onMinRange, isAnswered ;

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_activity_layout);

        sp =getSharedPreferences("answered", Context.MODE_PRIVATE);

        isAnswered = sp.getBoolean("answered_or_not",false);

        // onMinRange=false;



        myDb = FirebaseDatabase.getInstance();
        onRangeRef= myDb.getReference().child("deneme");

        onRangeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                voteMin=dataSnapshot.child("minute_range").getValue(Long.class);
                voteEnterTime=dataSnapshot.child("enter_time").getValue(Long.class);

                phoneTime= Calendar.getInstance().getTimeInMillis();

                timeDiff = phoneTime - voteEnterTime ;

                if ( (voteMin * 60000) > timeDiff ){
                 onMinRange=true;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab= findViewById(R.id.fab3);
        fab.setVisibility(View.VISIBLE);


        final TextView buttonhint=(TextView) findViewById(R.id.buttonhint);



        fab = findViewById(R.id.fab3); // open survey / on long hold close survey and display fab2 button


        /*if(onMinRange)
        {
            fab.setVisibility(View.VISIBLE);
        }
        else {
            fab.setVisibility(View.GONE);
        }*/



        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                myDb=FirebaseDatabase.getInstance();
                myRef2=myDb.getReference().child("deneme").child("soru");
                myRef3=myDb.getReference().child("deneme").child("cevap1");
                myRef4=myDb.getReference().child("deneme").child("cevap2");


                AlertDialog.Builder mBuilder1=  new AlertDialog.Builder(final_activity.this);
                View mView= getLayoutInflater().inflate(R.layout.final_dialog,null);

                final TextView mTextView= (TextView) mView.findViewById(R.id.surveyquestiontext);
                final Button answer1but= (Button) mView.findViewById(R.id.answer1button);
                final Button answer2but= (Button) mView.findViewById(R.id.answer2button);

                vote1text= (TextView) findViewById(R.id.vote1text2);
                vote2text= (TextView) findViewById(R.id.vote2text2);

                percent1Progress = (ProgressBar) findViewById(R.id.percent1Bar2) ;
                percent2Progress = (ProgressBar) findViewById(R.id.percent2Bar2) ;

                vote1bartext= (TextView) findViewById(R.id.vote1bartext2);
                vote2bartext= (TextView) findViewById(R.id.vote2bartext2);

               /* countdownText = (TextView) findViewById(R.id.countdownText);
                new CountDownTimer(30 * 1000,1000){
                    @Override
                    public void onTick(long l) {
                        countdownText.setText((int) (l /1000));
                    }

                    @Override
                    public void onFinish() {
                        countdownText.setText("done");
                    }
                };
*/
                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String myQuestion= dataSnapshot.getValue(String.class);
                        mTextView.setText(myQuestion);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                myRef3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String myAnswer1= dataSnapshot.getValue(String.class);
                        answer1but.setText(myAnswer1);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                myRef4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String myAnswer2= dataSnapshot.getValue(String.class);
                        answer2but.setText(myAnswer2);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                mBuilder1.setView(mView);
                final AlertDialog dialog =mBuilder1.create();

                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

                dialog.show();
                dialog.getWindow().setLayout( 800,500 );




                answer1but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        vote1Int= vote1Int+1;
                        increment.child("vote1").setValue(vote1Int);

                        Toast.makeText(getApplicationContext(),"Answer submitted successfully",Toast.LENGTH_SHORT).show();

                        // isAnswered= true;

                        editor.putBoolean("answered_or_not",true );
                        editor.apply();

                        answer1but.setEnabled(false);
                        answer2but.setEnabled(false);




                        // dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"you have choosen answer1",Toast.LENGTH_LONG).show();
                    }

                });


                answer2but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        vote2Int = vote2Int + 1;
                        increment.child("vote2").setValue(vote2Int);

                        Toast.makeText(getApplicationContext(),"Answer submitted successfully",Toast.LENGTH_SHORT).show();

                        //isAnswered=true;

                        editor.putBoolean("answered_or_not",true);
                        editor.apply();

                        answer1but.setEnabled(false);
                        answer2but.setEnabled(false);

                        // dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"you have choosen answer2",Toast.LENGTH_LONG).show();

                    }
                });

                // TextView op1text,op2text;

            }


        });


         voteCountRef= myDb.getReference().child("votecount").child("1");
        voteCountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vote1Int = dataSnapshot.child("vote1").getValue(Long.class);
                vote2Int = dataSnapshot.child("vote2").getValue(Long.class);

                String vote1CountStr = String.valueOf(vote1Int);
                String vote2CountStr=String.valueOf(vote2Int);

                vote1text.setText(vote1CountStr);
                vote2text.setText( vote2CountStr );

                d3=vote1Int.doubleValue(); //  vote 1 real count
                d4=vote2Int.doubleValue();         // vote 2 real count

                Double d5 = ( (d3)/(d3+d4)) * 100 ;  // vote 1 percentage
                Double d6 = ( (d4)/(d3+d4)) * 100 ;  // vote 2 percentage

                int int5 = d5.intValue(); // test & backup
                int int6 = d6.intValue(); // test & backup


                System.out.println(d3);
                System.out.println(d4);
                System.out.println(d5);
                System.out.println(d6);

                percent1Progress.setProgress(int5);
                percent2Progress.setProgress(int6);

                vote1bartext.setText("%" + Double.toString(d5));    // percentage display
                vote2bartext.setText("%" + Double.toString(d6));    //     "         "

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
