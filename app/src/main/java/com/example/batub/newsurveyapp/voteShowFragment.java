package com.example.batub.newsurveyapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class voteShowFragment extends Fragment {


    View fragView;

    ProgressBar pb1 ,pb2 ;
    TextView questionText, vote1percenttext,vote2percenttext;
    Button button1,button2;
    TextView vote1text,vote2text;

    public voteShowFragment() {
        // Required empty public constructor
    }



    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragView = inflater.inflate(R.layout.fragment_vote_show, container, false);
        pb1 = (ProgressBar)fragView.findViewById(R.id.progressBar1frag);
        pb2 = (ProgressBar)fragView.findViewById(R.id.progressBar2frag);

        questionText = (TextView)fragView.findViewById(R.id.questionTextfrag);
        vote1percenttext = (TextView)fragView.findViewById(R.id.vote1percenttext);
        vote2percenttext = (TextView)fragView.findViewById(R.id.vote2percenttext);

        button1 = (Button)fragView.findViewById(R.id.button1frag);
        button2 = (Button)fragView.findViewById(R.id.button2frag);

        vote1text = (TextView) fragView.findViewById(R.id.vote1textfrag);
        vote2text = (TextView) fragView.findViewById(R.id.vote2textfrag);

        return fragView;
    }




}
