package com.tbe.prolab.Fonctionnalities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbe.prolab.R;

public class Fonctionnalities extends Fragment {
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

        return v;
    }
}