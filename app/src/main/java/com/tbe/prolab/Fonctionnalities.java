package com.tbe.prolab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fonctionnalities extends Fragment {

    public static Fonctionnalities newInstance() {
        Fonctionnalities fragment = new Fonctionnalities();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public Fonctionnalities() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fonctionnalities, container, false);
    }

}
