package com.example.batub.newsurveyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity {

    int increment;
    private Button button1, button2;
    private EditText editText, editText2,editText3,editMinuteText;
    private RecyclerView recyclerView;
    private TextView textView;

   // public Survey passTheSurveyObject;

    //String incrementString;

    private FirebaseDatabase myDatabase;
    private DatabaseReference myDatabaseRef, refLocation;

    Button finale_button;

    Long enterTime ;
    Long minuteRange;





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
        editMinuteText =(EditText) findViewById(R.id.editMinuteText);

        textView = (TextView) findViewById(R.id.readdataview);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String value= editText.getText().toString(); //question
                String value2= editText2.getText().toString(); //answer1
                String value3= editText3.getText().toString(); //answer2

                 minuteRange= Long.valueOf(editMinuteText.getText().toString()); //answer2

                enterTime = Calendar.getInstance().getTimeInMillis();


                //  refLocation=myDatabaseRef;

                refLocation=myDatabase.getReference().child("deneme");


                /*
                Query query=refLocation.orderByKey(); // QUERY

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            String incNumber=ds.getKey();
                            increment =Integer.parseInt(incNumber);

                            Log.d("incnumber",incNumber);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                query.addListenerForSingleValueEvent(valueEventListener);


                incrementString= Integer.toString(increment+1);



                Survey surveyObject= new Survey(value,value2,value3,increment+1);

                refLocation.child(incrementString).setValue(surveyObject);

               // refLocation.child(incrementString).child("id").setValue(incrementString);



                textView7.setText(incrementString);

              //  passTheSurveyObject.changeAttributeSurvey(surveyObject);


                */

                    refLocation.child("soru").setValue(value);
                refLocation.child("cevap1").setValue(value2);
                refLocation.child("cevap2").setValue(value3);

                refLocation.child("minute_range").setValue(minuteRange);
                refLocation.child("enter_time").setValue(enterTime);

               SharedPreferences mypref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = mypref.edit();

               /* SharedPreferences mypref = getApplicationContext().getSharedPreferences("mypref",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mypref.edit();*/

                editor.putBoolean("isAnswered",false);
                editor.putBoolean("answered yes no",false);

                editor.commit();

                boolean isansweredInMain = mypref.getBoolean("isanswered",false);

                Log.e("isansweredInMain",String.valueOf(isansweredInMain));


                editText.getText().clear();
                editText2.getText().clear();
                editText3.getText().clear();
                editMinuteText.getText().clear();


                Toast.makeText(getApplicationContext(),
                        "added to firebase", Toast.LENGTH_LONG).show();

            }

        });

    }

    @Override
    protected void onResume() {



            Toast.makeText(getApplicationContext(),
                    "onresume", Toast.LENGTH_SHORT).show();

            super.onResume();
        }

    public void goToDisplayPage(View view){

        Intent intent = new Intent(this,fragmented_vote_activity.class);
        startActivity(intent);


    }

    public void gotodemo(View view){
        Intent demoIntent = new Intent(this,final_demo.class);

        startActivity(demoIntent);
    }

    @Override
    protected void onStop() {

        Toast.makeText(getApplicationContext(),
                "onstop", Toast.LENGTH_SHORT).show();

        super.onStop();
    }
}
