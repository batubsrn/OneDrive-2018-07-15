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
import com.google.firebase.database.ValueEventListener;

public class database_page extends AppCompatActivity {

    FirebaseDatabase  myDatabase;
    DatabaseReference myRef3;
    Button dialogbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_page_layout);
        dialogbutton= (Button) findViewById(R.id.questiontext);

        dialogbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder1=  new AlertDialog.Builder(database_page.this);
                View mView= getLayoutInflater().inflate(R.layout.survey_dialog,null);

                TextView mTextView= mView.findViewById(R.id.surveyquestiontext);
                Button answer1but= mView.findViewById(R.id.answer1button);
                Button answer2but= mView.findViewById(R.id.answer2button);


                mBuilder1.setView(mView);
                AlertDialog dialog =mBuilder1.create();


                dialog.show();



            }
        });


        myDatabase=FirebaseDatabase.getInstance();
        myRef3=myDatabase.getReference().child("deneme").child("soru");

        myRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String myChildquestion= dataSnapshot.getValue(String.class);
                dialogbutton.setText(myChildquestion);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}
