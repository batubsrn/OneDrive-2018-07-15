package com.example.batub.newsurveyapp;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.upstream.DataSourceException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;



public class displaypage extends AppCompatActivity {

    Survey newsurvey2;
    Integer question_no;
    String incNumber;

    //TextClock clock;

    Button button7;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaypage);

        TextClock tClock = (TextClock) findViewById(R.id.textClock);

        button7= findViewById(R.id.displaytryout);
        /*
        button7.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Snackbar.make(view, "Get lost Survey", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                return true;
            }
        });

       */

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2= getIntent();
                String entry_number = intent2.getStringExtra("latestEntry");

                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference dialogreference;


                dialogreference=database.getReference().child("surveys").child("1");


                dialogreference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        newsurvey2.question= (String)dataSnapshot.child("question").getValue();
                        newsurvey2.answer1= (String)dataSnapshot.child("answer1").getValue();
                        newsurvey2.answer2= (String)dataSnapshot.child("answer2").getValue();

                        Log.d("question",newsurvey2.question);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                AlertDialog.Builder mBuilder1=  new AlertDialog.Builder(displaypage.this);
                View mView= getLayoutInflater().inflate(R.layout.survey_dialog,null);

                TextView mTextView= mView.findViewById(R.id.surveyquestiontext);
                Button answer1but= mView.findViewById(R.id.answer1button);
                Button answer2but= mView.findViewById(R.id.answer2button);

                 mTextView.setText(newsurvey2.getQuestion());
                 answer1but.setText(newsurvey2.getAnswer1());
                 answer2but.setText(newsurvey2.getAnswer2());


                answer1but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getApplicationContext(),"you have choosen answer1",Toast.LENGTH_SHORT).show();

                    }

                });

                answer2but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getApplicationContext(),"you have chosen answer2",Toast.LENGTH_SHORT).show();

                    }
                });

                mBuilder1.setView(mView);
                AlertDialog dialog =mBuilder1.create();


                dialog.show();

            }

        });

    }

    public void gotovideomethod(View view) {

        Intent govideointent = new Intent(this, video_player_activity.class);
        startActivity(govideointent);

    }

    public void gotosurveylist(View view)
    {
       // Intent intent5= new Intent(this,listactivity.class);
        // startActivity(intent5);
    }

    public void gonewdisplay(View view)
    {
        Intent intent4 = new Intent(this, newdisplaypage.class);
        startActivity(intent4);
    }


}










