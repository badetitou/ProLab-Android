package com.tbe.prolab.Fonctionnalities;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tbe.prolab.DividerItemDecoration;
import com.tbe.prolab.Project.ProjectAdapter;
import com.tbe.prolab.R;
import com.tbe.prolab.RecyclerItemClickListener;
import com.tbe.prolab.Tools.ReadIt;
import com.tbe.prolab.main;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class Fonctionnalities extends Fragment implements View.OnClickListener {

    private RecyclerView listFonctionnality;
    private RecyclerView.Adapter listFonctionnalityAdapter;
    private RecyclerView.LayoutManager listFonctionnalityLayoutManager;

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
    public void onResume(){
        super.onResume();
        new WebAccessProjectFonctionnalities(main.idProject).execute();
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
        ImageButton button = (ImageButton) v.findViewById(R.id.fonctionnalities_add_fonctionnalities);
        button.setOnClickListener(this);


        listFonctionnality = (RecyclerView) v.findViewById(R.id.select_fonctionnalities_list);
        listFonctionnality.setHasFixedSize(true);
        listFonctionnality.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        listFonctionnalityLayoutManager = new LinearLayoutManager(getActivity());
        listFonctionnality.setLayoutManager(listFonctionnalityLayoutManager);

        listFonctionnalityAdapter = new FonctionnalityAdapter(new String[0], new String[0], new int[0]);
        listFonctionnality.setAdapter(listFonctionnalityAdapter);

        listFonctionnality.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), InfoFonctionnalities.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", main.username);
                        bundle.putString("idProject", main.idProject);
                        bundle.putString("name" ,((FonctionnalityAdapter)listFonctionnalityAdapter).getFonctionnalityName(position));
                        bundle.putString("description" ,((FonctionnalityAdapter)listFonctionnalityAdapter).getFonctionnalityDescription(position));
                        bundle.putInt("idFonctionnality" ,((FonctionnalityAdapter)listFonctionnalityAdapter).getFonctionnalityNumber(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                })
        );
        new WebAccessProjectFonctionnalities(main.idProject).execute();

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


    public class WebAccessProjectFonctionnalities extends AsyncTask<String, Void, String> {

        private String project;

        public WebAccessProjectFonctionnalities(String project) {
            this.project = project;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return getProject();
            } catch (Exception e) {
                return "fail";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("fail") || result.equals("")){
                callFail();
            }else {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    List<String> names = new ArrayList<>();
                    List<String> descriptions = new ArrayList<>();
                    List<Integer> fonctionnalityNumber = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        names.add(jsonArray.getJSONObject(i).getString("name"));
                        descriptions.add(jsonArray.getJSONObject(i).getString("description"));
                        fonctionnalityNumber.add(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")));
                    }
                    ((FonctionnalityAdapter) listFonctionnalityAdapter).setData(names, descriptions, fonctionnalityNumber);

                } catch (JSONException e) {
                    callFail();
                }
            }
        }

        public String getProject() throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(main.HOST + "/v1/task/allfonctionnalities/"+ main.idMember);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("Connection ", "The response is: " + response);
                if (response == 204) {
                    return "fail";
                }

                is = conn.getInputStream();
                // Convert the InputStream into a string
                return ReadIt.ReadIt(is);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    private void callFail() {
        Toast.makeText(this.getActivity(), "fail",Toast.LENGTH_SHORT).show();
    }

}