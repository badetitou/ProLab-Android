package com.tbe.prolab.Fonctionnalities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tbe.prolab.R;

import java.util.zip.Inflater;

public class Fonctionnalities extends Fragment implements View.OnClickListener {
    public Fonctionnalities() {
        // Required empty public constructor
    }

    public static Fonctionnalities newInstance() {
        Fonctionnalities fragment = new Fonctionnalities();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fonctionnalities, container, false);
        Button button = (Button) v.findViewById(R.id.fonctionnalities_add_fonctionnalities);
        button.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fonctionnalities_add_fonctionnalities:
                createFonctionnality();
        }
    }

    public void createFonctionnality(){
        Intent intent = new Intent(getActivity(), CreateFonctionnality.class);
        startActivity(intent);
    }
}