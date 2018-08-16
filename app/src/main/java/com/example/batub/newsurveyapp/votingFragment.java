package com.example.batub.newsurveyapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class votingFragment extends Fragment {


    ProgressBar pb1 ,pb2 ;
    TextView questionText, vote1percenttext,vote2percenttext;
    Button button1,button2;
    TextView vote1text,vote2text;



    public votingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View fragView = inflater.inflate(R.layout.fragment_voting, container, false);

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

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button1.setVisibility(View.GONE); button2.setVisibility(View.GONE);

                pb1.setVisibility(View.VISIBLE);
                pb2.setVisibility(View.VISIBLE);

                vote1text.setVisibility(View.VISIBLE);
                vote2text.setVisibility(View.VISIBLE);

                vote1percenttext.setVisibility(View.VISIBLE);
                vote2percenttext.setVisibility(View.VISIBLE);

            }
        });

        return  fragView;

    }

}
