package com.example.batub.newsurveyapp;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class video_player_activity extends Activity {

    private SimpleExoPlayer player;

    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;


    FirebaseDatabase myDb;
    DatabaseReference myRef2;
    DatabaseReference myRef3;
    DatabaseReference myRef4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        /*
        String myQuestion;
        String myanswer1;
        String myAnswer2;
        */




        final TextView buttonhint=(TextView) findViewById(R.id.buttonhint);
        final TextView buttonhint2=(TextView) findViewById(R.id.buttonhint2);


        final FloatingActionButton fab = findViewById(R.id.fab); // open survey / on long hold close survey and display fab2 button
        final FloatingActionButton fab2= findViewById(R.id.fab2); //  on long click show survey back


        fab.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.GONE);
        buttonhint.setVisibility(View.VISIBLE);
        buttonhint2.setVisibility(View.GONE);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        fab2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(getApplicationContext(),"Surveys are back",Toast.LENGTH_SHORT).show();
                fab.setVisibility(View.VISIBLE);
                fab2.setVisibility(View.GONE);
                buttonhint.setVisibility(View.VISIBLE);
                buttonhint2.setVisibility(View.GONE);

                return true;

            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Press and hold in order to open surveys",Toast.LENGTH_SHORT).show();

            }
        });


        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getApplicationContext(),"surveys hidden",Toast.LENGTH_SHORT).show();
                fab.setVisibility(View.GONE);
                fab2.setVisibility(View.VISIBLE);
                buttonhint2.setVisibility(View.VISIBLE);
                buttonhint.setVisibility(View.GONE);

                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                 myDb=FirebaseDatabase.getInstance();
                 myRef2=myDb.getReference().child("deneme").child("soru");
                 myRef3=myDb.getReference().child("deneme").child("cevap1");
                 myRef4=myDb.getReference().child("deneme").child("cevap2");



                AlertDialog.Builder mBuilder1=  new AlertDialog.Builder(video_player_activity.this);
                View mView= getLayoutInflater().inflate(R.layout.survey_dialog,null);

                final TextView mTextView= (TextView) mView.findViewById(R.id.surveyquestiontext);
                final Button answer1but= (Button) mView.findViewById(R.id.answer1button);
                final Button answer2but= (Button) mView.findViewById(R.id.answer2button);



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
                dialog.show();

                answer1but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"you have choosen answer1",Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }

                });


                answer2but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getApplicationContext(),"you have choosen answer2",Toast.LENGTH_LONG).show();
                        dialog.dismiss();


                    }
                });


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
