package com.tbe.prolab.Members;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tbe.prolab.R;

public class SelectMember extends Fragment implements View.OnClickListener {

    private RecyclerView listFonctionnality;
    private RecyclerView.Adapter listFonctionnalityAdapter;
    private RecyclerView.LayoutManager listFonctionnalityLayoutManager;

    public SelectMember() {
        // Required empty public constructor
    }

    public static SelectMember newInstance() {
        SelectMember fragment = new SelectMember();
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

    @Override
    public void onClick(View v) {

    }
}