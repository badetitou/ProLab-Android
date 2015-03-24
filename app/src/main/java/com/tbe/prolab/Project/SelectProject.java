package com.tbe.prolab.Project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tbe.prolab.DividerItemDecoration;
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


public class SelectProject extends ActionBarActivity {

    private RecyclerView listProject;
    private RecyclerView.Adapter listProjectAdapter;
    private RecyclerView.LayoutManager listProjectLayoutManager;

    private String username;

    @Override
    protected void onResume() {
        super.onResume();
        try {
            new WebAccessUserProject(username).execute();
        } catch (Exception e) {
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        username = bundle.getString("username");

        setContentView(R.layout.activity_select_project);
        listProject = (RecyclerView) findViewById(R.id.select_project_list);
        listProject.setHasFixedSize(true);
        listProject.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        listProjectLayoutManager = new LinearLayoutManager(this);
        listProject.setLayoutManager(listProjectLayoutManager);

        listProjectAdapter = new ProjectAdapter(new String[0], new String[0], new int[0]);
        listProject.setAdapter(listProjectAdapter);

        listProject.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), main.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putString("idProject", ((ProjectAdapter) listProjectAdapter).getProjectNumber(position) + "");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                })
        );


        new WebAccessUserProject(username).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_select_project, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onCreateProject(View view){
        Intent intent = new Intent(this, CreateProject.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class WebAccessUserProject extends AsyncTask<String, Void, String> {

        private String username;

        public WebAccessUserProject(String username) {
            this.username = username;
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
                JSONArray jsonArray = new JSONArray(result);
                List<String> titles = new ArrayList<>();
                List<String> punchlines = new ArrayList<>();
                List<Integer> projectNumber = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); ++i) {
                    titles.add(jsonArray.getJSONObject(i).getString("name"));
                    punchlines.add(jsonArray.getJSONObject(i).getString("punchline"));
                    projectNumber.add(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")));
                }
                ((ProjectAdapter) listProjectAdapter).setData(titles, punchlines, projectNumber);

            } catch (JSONException e) {
            }
        }

        public String getProject() throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(main.HOST + "/v1/members/" + username);
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
