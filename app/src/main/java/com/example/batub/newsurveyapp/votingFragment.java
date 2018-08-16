package com.example.batub.newsurveyapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class votingFragment extends Fragment {

    View fragView;


    FirebaseDatabase voteDatabase;
    DatabaseReference questionReference , voteCountReference, progressBarReference ;

    String question, option1,option2 ;

    Long vote1Count,vote2Count ;



    ProgressBar pb1 ,pb2 ;
    TextView questionText, vote1percenttext,vote2percenttext;
    Button button1,button2;
    TextView vote1text,vote2text;

    TextView timerText;

    Boolean onTime;
    boolean answered;
    boolean timerFirstTime;

    SharedPreferences preferences;

    static Double d3;
    static Double d4;

    long timeLeft;

    ////////////////////////////////////////////////////
    public votingFragment() {
        // Required empty public constructor
    }

    /////////////////////////////////////////////////////////

    public void openResults() {


        vote1text = (TextView)  fragView.findViewById(R.id.vote1textfrag);
        vote2text = (TextView)  fragView.findViewById(R.id.vote2textfrag);

        pb1 = (ProgressBar) fragView.findViewById(R.id.progressBar1frag);
        pb2 = (ProgressBar) fragView.findViewById(R.id.progressBar2frag);

        vote1percenttext = (TextView) fragView.findViewById(R.id.vote1percenttext);
        vote2percenttext = (TextView) fragView.findViewById(R.id.vote2percenttext);

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

                System.out.println(d3);
                System.out.println(d4);
                System.out.println(d5);
                System.out.println(d6);

                String d5Str = String.format("%.2f", d5);
                String d6Str = String.format("%.2f", d6);

                pb1.setProgress(int5);
                pb2.setProgress(int6);

                vote1percenttext.setText("%" + d5Str  );    // percentage display
                vote2percenttext.setText("%" + d6Str  );    //     "         "

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

                vote1text.setText(option1text);
                vote2text.setText(option2text);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


    ////////////////////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment


         fragView = inflater.inflate(R.layout.fragment_voting, container, false);

        pb1 = (ProgressBar)fragView.findViewById(R.id.progressBar1frag);
        pb2 = (ProgressBar)fragView.findViewById(R.id.progressBar2frag);

        pb1.setVisibility(View.GONE);
        pb2.setVisibility(View.GONE);

        questionText = (TextView)fragView.findViewById(R.id.questionTextfrag);

        vote1percenttext = (TextView)fragView.findViewById(R.id.vote1percenttext);
        vote2percenttext = (TextView)fragView.findViewById(R.id.vote2percenttext);

        vote1percenttext.setVisibility(View.GONE);
        vote2percenttext.setVisibility(View.GONE);

        button1 = (Button)fragView.findViewById(R.id.button1frag);
        button2 = (Button)fragView.findViewById(R.id.button2frag);

        vote1text = (TextView) fragView.findViewById(R.id.vote1textfrag);
        vote2text = (TextView) fragView.findViewById(R.id.vote2textfrag);

        vote1text.setVisibility(View.GONE);
        vote2text.setVisibility(View.GONE);

        ////////////////// DATABASE REFERENCES

        voteDatabase=FirebaseDatabase.getInstance();
        questionReference = voteDatabase.getReference().child("deneme");
        voteCountReference= voteDatabase.getReference().child("votecount").child("1");


        ////////////////////
        timerFirstTime = true;

        /*preferences = PreferenceManager.getDefaultSharedPreferences(getContext());


        answered = preferences.getBoolean("isAnswered",false);
        Log.e("answered",String.valueOf(answered) );*/

        /*if(answered){
            openResults();
        }*/

        /// Get questions and display them



            questionText = (TextView) fragView.findViewById(R.id.questionTextfrag);
            button1 = (Button) fragView.findViewById(R.id.button1frag);
            button2 = (Button) fragView.findViewById(R.id.button2frag);
            timerText = (TextView) fragView.findViewById(R.id.timerText); // count down timer

            questionReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    question = dataSnapshot.child("soru").getValue(String.class);
                    option1 = dataSnapshot.child("cevap1").getValue(String.class);
                    option2 = dataSnapshot.child("cevap2").getValue(String.class);

                    Long entryTime = dataSnapshot.child("enter_time").getValue(Long.class);
                    Long minRange = dataSnapshot.child("minute_range").getValue(Long.class);

                    Long time = Calendar.getInstance().getTimeInMillis();
                    final long countdownMills = (minRange * 60000) - (time - entryTime);

                    Log.e("question", question);
                    Log.e("option1", option1);
                    Log.e("option2", option2);

                    questionText.setText(question);
                    button1.setText(option1);
                    button2.setText(option2);

                    timeLeft = countdownMills;

                    if (timerFirstTime) {
                        new CountDownTimer(timeLeft, 1000) {
                            public void onTick(long millisUntilFinished) {
                                timerText.setText(String.valueOf(timeLeft / 1000));
                                timeLeft = timeLeft - 1000;
                            }

                            public void onFinish() {

                                //  CLOSE FRAGMENT




                            }
                        }.start();

                        timerFirstTime = false;
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




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



        ///answer1

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vote1Count= vote1Count+1;
                voteCountReference.child("vote1").setValue(vote1Count);

                SharedPreferences.Editor editor = preferences.edit();



                editor.putBoolean("isAnswered",true);

                editor.commit();

                //answered = true;

                Toast.makeText(getActivity(),"Answer submitted successfully",Toast.LENGTH_SHORT).show();

                button1.setVisibility(View.GONE);
                button2.setVisibility(View.GONE);

                pb1.setVisibility(View.VISIBLE); // option 1 bar display
                pb2.setVisibility(View.VISIBLE);

                vote1text.setVisibility(View.VISIBLE); //option 1 display
                vote2text.setVisibility(View.VISIBLE);

                vote1percenttext.setVisibility(View.VISIBLE); //option 1 percent display
                vote2percenttext.setVisibility(View.VISIBLE);

                openResults();


                }

        });

        /// answer2

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vote2Count= vote2Count+1;
                voteCountReference.child("vote2").setValue(vote2Count);

               SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean("isAnswered",true);
                editor.commit();

                //answered = true;

                Toast.makeText(getActivity(),"Answer submitted successfully",Toast.LENGTH_SHORT).show();

                button1.setVisibility(View.GONE);
                button2.setVisibility(View.GONE);

                pb1.setVisibility(View.VISIBLE); // option 1 bar display
                pb2.setVisibility(View.VISIBLE);

                vote1text.setVisibility(View.VISIBLE); //option 1 display
                vote2text.setVisibility(View.VISIBLE);

                vote1percenttext.setVisibility(View.VISIBLE); //option 1 percent display
                vote2percenttext.setVisibility(View.VISIBLE);

                openResults();



            }


        });

        //////OnCreateView



        return  fragView;

    }


}

