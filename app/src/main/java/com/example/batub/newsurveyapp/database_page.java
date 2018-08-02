package com.example.batub.newsurveyapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class database_page extends AppCompatActivity {

    FirebaseDatabase  myDatabase;
    DatabaseReference voteRef,op1ref,increment;
    Button vote1button,vote2button;
    TextView vote1text,vote2text, vote1displaytextview,vote2displaytextview,vote1bartext,vote2bartext;

    ProgressBar percent1Progress,percent2Progress;
    // String progbar1textStr,progbar2textStr;

    Long vote1Int,vote2Int;
    //  Long percent1,percent2;

     static   int i3;

     static Double d3;
     static Double d4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_page_layout);

        myDatabase=FirebaseDatabase.getInstance();
        voteRef=myDatabase.getReference().child("votecount").child("1");

        op1ref=myDatabase.getReference().child("votecount").child("1"); // read vote count

        increment=myDatabase.getReference().child("votecount").child("1"); // increment vote count

        vote1button = (Button) findViewById(R.id.vote1increment);
        vote2button = (Button) findViewById(R.id.vote2increment);


        percent1Progress = (ProgressBar) findViewById(R.id.percent1Bar) ;
        percent2Progress = (ProgressBar) findViewById(R.id.percent2Bar) ;

        vote1bartext= (TextView) findViewById(R.id.vote1bartext);
        vote2bartext= (TextView) findViewById(R.id.vote2bartext);

        vote1text= (TextView) findViewById(R.id.vote1text);
        vote2text= (TextView) findViewById(R.id.vote2text);

        vote1displaytextview = (TextView) findViewById(R.id.vote1displaytext);
        vote2displaytextview= (TextView) findViewById(R.id.vote2displaytext);


        op1ref.addValueEventListener(new ValueEventListener() {
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

        vote1button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vote1Int= vote1Int+1;
                increment.child("vote1").setValue(vote1Int);
            }
        });


        vote2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vote2Int= vote2Int+1;
                increment.child("vote2").setValue(vote2Int);
            }
        });


    }


 /*   public void savenumber(Long long1,int i,Double doub){

        i=long1.intValue();
        doub=long1.doubleValue();


    }*/



}
