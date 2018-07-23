package com.example.batub.newsurveyapp;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class newdisplaypage extends AppCompatActivity {

        FirebaseDatabase mdatab;
        DatabaseReference myref;






    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newdisplaypage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final TextView buttonhint=(TextView) findViewById(R.id.buttonhint);
        final TextView buttonhint2=(TextView) findViewById(R.id.buttonhint2);



         final FloatingActionButton fab = findViewById(R.id.fab); // open survey / on long hold close survey and display fab2 button
        final FloatingActionButton fab2= findViewById(R.id.fab2); //  on long click show survey back


        fab.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.GONE);
        buttonhint.setVisibility(View.VISIBLE);
        buttonhint2.setVisibility(View.GONE);

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
               // buttonhint.setText("");

                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder1=  new AlertDialog.Builder(newdisplaypage.this);
                View mView= getLayoutInflater().inflate(R.layout.survey_dialog,null);

                TextView mTextView= (TextView) mView.findViewById(R.id.surveyquestiontext);
                Button answer1but= (Button) mView.findViewById(R.id.answer1button);
                Button answer2but= (Button) mView.findViewById(R.id.answer2button);

                answer1but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"you have choosen answer1",Toast.LENGTH_LONG).show();

                    }

                });


                answer2but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getApplicationContext(),"you have choosen answer2",Toast.LENGTH_LONG).show();

                    }
                });

                mBuilder1.setView(mView);
                AlertDialog dialog =mBuilder1.create();
                dialog.show();




            }


        });



    }

}
