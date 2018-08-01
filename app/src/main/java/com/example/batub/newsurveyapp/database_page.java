package com.example.batub.newsurveyapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class database_page extends AppCompatActivity {

    FirebaseDatabase  myDatabase;
    DatabaseReference voteRef,op1ref,op2ref;
    Button vote1button,vote2button;
    TextView vote1text,vote2text, vote1percentview,vote2percentview,vote1bartext,vote2bartext;

    ProgressBar percent1Progress,percent2Progress;

    Long vote1Int,vote2Int;
    Long percent1,percent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_page_layout);

        myDatabase=FirebaseDatabase.getInstance();
        voteRef=myDatabase.getReference().child("votecount").child("1");

        op1ref=myDatabase.getReference().child("votecount").child("1").child("vote1");
        op2ref=myDatabase.getReference().child("votecount").child("1").child("vote2");


        percent1Progress = (ProgressBar) findViewById(R.id.percent1Bar) ;
        percent2Progress = (ProgressBar) findViewById(R.id.percent2Bar) ;

        vote1bartext= (TextView) findViewById(R.id.vote1bartext);
        vote2bartext= (TextView) findViewById(R.id.vote2bartext);

        vote1text= (TextView) findViewById(R.id.vote1text);
        vote2text= (TextView) findViewById(R.id.vote2text);
        vote1percentview= (TextView) findViewById(R.id.vote1percent);
        vote2percentview= (TextView) findViewById(R.id.vote2percent);


        op1ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vote1Int = dataSnapshot.getValue(Long.class);
                String vote1CountStr = String.valueOf(vote1Int);
                vote1text.setText(vote1CountStr);

                int i = vote1Int.intValue();
                percent1Progress.setProgress(i);
                vote1bartext.setText(vote1CountStr);

                /*percent1= (  ( ( vote1Int ) /  (vote1Int+vote2Int)  ) * 100  );
                String percent1Str =String.valueOf(percent1);
                vote1percentview.setText( percent1Str );*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        op2ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vote2Int= dataSnapshot.getValue(Long.class);
                String vote2CountStr=String.valueOf(vote2Int);
                vote2text.setText( vote2CountStr );

                int i2 = vote2Int.intValue();
                percent2Progress.setProgress(i2);
                vote2bartext.setText(vote2CountStr);

                /*percent2= (  ( ( vote2Int ) /  (vote1Int+vote2Int)  ) * 100  );
                String percent2Str =String.valueOf(percent2);
                vote2percentview.setText( percent2Str );*/

                // if (ikisi de gelmiş)
                    hesapla();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*

        int i3 = vote1Int.intValue();

        int i4 = vote2Int.intValue();

        int i5=  ( i3/(i3+i4) )  *100 ;

        int i6 =   ( i4/(i3+i4) )  *100 ;


        percent1Progress.setProgress(i5);

        percent1Progress.setProgress(i6); */


    }


}
