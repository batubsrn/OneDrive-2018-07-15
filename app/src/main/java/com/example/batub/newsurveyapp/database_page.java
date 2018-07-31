package com.example.batub.newsurveyapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class database_page extends AppCompatActivity {

    FirebaseDatabase  myDatabase;
    DatabaseReference voteRef;
    Button vote1button,vote2button;
    TextView vote1text,vote2text, vote1percent,vote2percent;

    Integer vote1Int,vote2Int;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_page_layout);

        vote1text= (TextView) findViewById(R.id.vote1text);
        vote2text= (TextView) findViewById(R.id.vote2text);



        myDatabase=FirebaseDatabase.getInstance();
        voteRef=myDatabase.getReference().child("votecount").child("1");

        Query vote1query=voteRef.orderByKey().limitToFirst(1);
        Query vote2query=voteRef.orderByKey().limitToLast(1);

        /*

        vote1query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String vote1number =dataSnapshot.getValue(String.class);
                vote1text.setText(vote1number);
                vote1Int=Integer.parseInt(vote1number);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        vote2query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String vote2number =dataSnapshot.getValue(String.class);
                vote2text.setText(vote2number);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        */


    }


}
