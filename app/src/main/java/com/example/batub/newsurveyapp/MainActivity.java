package com.example.batub.newsurveyapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private Button button1, button2;
    private EditText editText, editText2,editText3;
    private RecyclerView recyclerView;
    private TextView textView;

    private FirebaseDatabase myDatabase;
    private DatabaseReference myDatabaseRef, refLocation;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseApp.initializeApp(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabase= FirebaseDatabase.getInstance();
        myDatabaseRef = myDatabase.getReference();
        final DatabaseReference myref2 = myDatabase.getReference().child("surveys");


        button1= (Button) findViewById(R.id.button1); //add button
        button2= (Button) findViewById(R.id.button2); //to display page button
        editText =(EditText) findViewById(R.id.editText);

        editText2 =(EditText) findViewById(R.id.editText2);
        editText3 =(EditText) findViewById(R.id.editText3);
        textView = (TextView) findViewById(R.id.readdataview);
       final TextView textView7 = (TextView) findViewById(R.id.denemeview);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String value= editText.getText().toString(); //question
                String value2= editText2.getText().toString(); //answer1
                String value3= editText3.getText().toString(); //answer2

                //  refLocation=myDatabaseRef;

                refLocation=myDatabase.getReference().child("surveys");

                Survey surveyobject= new Survey(value,value2,value3);
               // Query incement_query;
               // int last;

                refLocation.child("2").setValue(surveyobject);

               /* myref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       // textView7.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                */


             //   refLocation.child("question").setValue(value);
              //  refLocation.child("answer").setValue(value2);
               // refLocation.child("answer2").setValue(value3);

                editText.getText().clear();
                editText2.getText().clear();
                editText3.getText().clear();

                Toast.makeText(getApplicationContext(),
                        "added to firebase", Toast.LENGTH_LONG).show();

            }

        });

    }

    @Override
    protected void onResume() {
          // Query myquery= refLocation.



            Toast.makeText(getApplicationContext(),
                    "onresume", Toast.LENGTH_LONG).show();

            super.onResume();
        }





    public void goToDisplayPage(View view){

        Intent intent = new Intent(this,displaypage.class);
        startActivity(intent);
    }

}
