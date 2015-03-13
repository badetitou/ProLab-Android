package com.tbe.prolab.Project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.tbe.prolab.R;

public class Project extends Fragment implements View.OnClickListener {
    public Project() {
        // Required empty public constructor
    }

    public static Project newInstance() {
        Project fragment = new Project();
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
        View v = inflater.inflate(R.layout.fragment_project, container, false);
        ImageButton imageButton = (ImageButton) v.findViewById(R.id.create_project_button);
        imageButton.setOnClickListener(this);
        return v;
    }

    public void createProject(View view) {
        Intent intent = new Intent(this.getActivity(), Project.createProject.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_project_button:
                createProject(v);
        }

    }
}
