package com.tbe.prolab.Project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.pu;
import com.tbe.prolab.R;
import com.tbe.prolab.Tools.ReadIt;
import com.tbe.prolab.main;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InfoProject extends Fragment implements View.OnClickListener {

    private TextView name;
    private TextView punchline;
    private TextView description;
    private int projectNumber;
    private String url;

    private ProgressBar progressBar;


    public static InfoProject newInstance() {
        InfoProject fragment = new InfoProject();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume(){
        super.onResume();
        new WebAccessInfoProject(main.idProject).execute();
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

        progressBar = (ProgressBar) v.findViewById(R.id.info_project_progressbar);
        progressBar.setMax(100);

        name = (TextView) v.findViewById(R.id.info_project_title);
        description = (TextView) v.findViewById(R.id.info_project_description);
        punchline = (TextView) v.findViewById(R.id.info_project_punchline);
        new WebAccessInfoProject(main.idProject).execute();
        ImageButton imageButton = (ImageButton) v.findViewById(R.id.info_project_modif_button);
        imageButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.info_project_modif_button:
                Intent intent = new Intent(this.getActivity(), ModifProject.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name.getText().toString());
                bundle.putString("punchline", punchline.getText().toString());
                bundle.putString("description", description.getText().toString());
                bundle.putString("url", url);
                bundle.putInt("projectNumber", projectNumber);
                intent.putExtras(bundle);
                startActivity(intent);
        }
    }

    public class WebAccessInfoProject extends AsyncTask<String, Void, String> {

        private String idProject;

        public WebAccessInfoProject(String idProject) {
            this.idProject = idProject;
        }

        @Override
        protected String doInBackground(String... urls) {
            progressBar.setIndeterminate(true);
            try {
                return getProject();
            } catch (Exception e) {
                return "fail";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            progressBar.setIndeterminate(false);
            try {
                JSONObject jsonObject = new JSONObject(result);
                name.setText(jsonObject.getString("name"));
                punchline.setText(jsonObject.getString("punchline"));
                description.setText(jsonObject.getString("description"));
                projectNumber=jsonObject.getInt("id");
                url=jsonObject.getString("url");
            } catch (JSONException e) {
            }
        }

        public String getProject() throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(main.HOST + "/v1/projects/" + idProject);
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
}
