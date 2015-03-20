package com.tbe.prolab.Project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tbe.prolab.R;
import com.tbe.prolab.main;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InfoProject extends Fragment {

    TextView name;
    TextView punchline;
    TextView description;


    public static InfoProject newInstance() {
        InfoProject fragment = new InfoProject();
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
        name = (TextView) v.findViewById(R.id.info_project_title);
        description = (TextView) v.findViewById(R.id.info_project_description);
        punchline = (TextView) v.findViewById(R.id.info_project_punchline);
        new WebAccessInfoProject(main.idProject).execute();
        return v;
    }

    public class WebAccessInfoProject extends AsyncTask<String, Void, String> {

        private String idProject;

        public WebAccessInfoProject(String idProject) {
            this.idProject = idProject;
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
            try {
                JSONObject jsonObject = new JSONObject(result);
                name.setText(jsonObject.getString("name"));
                punchline.setText(jsonObject.getString("punchline"));
                description.setText(jsonObject.getString("description"));

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
                return readIt(is, len);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }


        public String readIt(InputStream stream, int len) throws IOException {
            Reader reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
    }
}
