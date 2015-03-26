package com.tbe.prolab.Fonctionnalities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tbe.prolab.DividerItemDecoration;
import com.tbe.prolab.Members.MemberAdapter;
import com.tbe.prolab.PopUp.AddUser;
import com.tbe.prolab.PopUp.RemoveFonctionnality;
import com.tbe.prolab.R;
import com.tbe.prolab.Tools.ReadIt;
import com.tbe.prolab.main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class InfoFonctionnalities extends ActionBarActivity {

    private RecyclerView listmembers;
    private RecyclerView.Adapter listMembersAdapter;
    private RecyclerView.LayoutManager listMembersLayoutManager;


    private ProgressBar progressBar;
    private TextView name;
    private TextView description;
    private TextView date;
    private int idFonctionnality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_fonctionnalities);

        name = (TextView) findViewById(R.id.info_fonctionnality_name);
        description = (TextView) findViewById(R.id.info_fonctionnality_description);
        date = (TextView)findViewById(R.id.info_fonctionnalities_text_date);

        progressBar = (ProgressBar) findViewById(R.id.info_fonctionnality_progress_bar);

        listmembers = (RecyclerView) findViewById(R.id.select_fonctionnalities_list);
        listmembers.setHasFixedSize(true);
        listmembers.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        listMembersLayoutManager = new LinearLayoutManager(this);
        listmembers.setLayoutManager(listMembersLayoutManager);

        listMembersAdapter = new MemberAdapter(new String[0], new int[0], getApplicationContext(), new int[0], this.getSupportFragmentManager());
        listmembers.setAdapter(listMembersAdapter);

        Bundle bundle = this.getIntent().getExtras();
        idFonctionnality = bundle.getInt("idFonctionnality");
        new WebAccessInfoFonctionnality(bundle.getInt("idFonctionnality")).execute();
        new WebAccessFonctionnalityUsers(bundle.getInt("idFonctionnality")).execute();
    }

    public void addMember(View view){
        new AddUser(1,idFonctionnality).show(this.getSupportFragmentManager(), "addFonctionnalityMember");
    }

    public void updateFonctionnality(View view){
        Intent intent = new Intent(this, EditFonctionnality.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", name.getText().toString());
        bundle.putString("description", description.getText().toString());
        bundle.putString("date", date.getText().toString());
        bundle.putInt("progress", progressBar.getProgress());
        bundle.putInt("id", idFonctionnality);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void deleteFonctionnality(View view){
        new RemoveFonctionnality(view.getContext(), name.getText().toString(), idFonctionnality).show(getSupportFragmentManager(), "removeFonctionnality");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info_fonctionnalities, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class WebAccessFonctionnalityUsers extends AsyncTask<String, Void, String> {

        int idFonctionnality;

        public WebAccessFonctionnalityUsers(int idFonctionnality) {
            this.idFonctionnality = idFonctionnality;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return getFonctionnalityUsers(idFonctionnality);
            } catch (IOException e) {
                return "Fail";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                ArrayList<String> members = new ArrayList<>();
                ArrayList<Integer> idMember = new ArrayList<>();
                for (int i = 0;i<jsonArray.length();++i){
                    members.add(jsonArray.getJSONObject(i).getString("username"));
                    idMember.add(jsonArray.getJSONObject(i).getInt("idMember"));
                }
                ((MemberAdapter) listMembersAdapter).setData(members, idMember, idFonctionnality);
            } catch (Exception e){
                callFail(result);
            }
        }

        private String getFonctionnalityUsers(int idFonctionnality) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            try {
                URL url = new URL(main.HOST + "/v1/fonctionnalities/member/" + idFonctionnality);
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

    private void callFail(String result) {
        Toast.makeText(this.getApplicationContext(), "Fail : " + result, Toast.LENGTH_SHORT).show();
    }

    private class WebAccessInfoFonctionnality extends AsyncTask<String, Void, String> {

        private int idFonctionnality;

        public WebAccessInfoFonctionnality(int idFonctionnality) {
            this.idFonctionnality = idFonctionnality;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return getInfoFonctionnality();
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
                 description.setText(jsonObject.getString("description"));
                 progressBar.setProgress(jsonObject.getInt("avancement"));
                 date.setText(jsonObject.getString("deadLine"));
             } catch (JSONException e) {
                 e.printStackTrace();
             }
        }

        public String getInfoFonctionnality() throws IOException {
            try {
                URL url = new URL(main.HOST + "/v1/fonctionnalities/" + idFonctionnality);
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

                return ReadIt.ReadIt(conn.getInputStream());
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
            }
        }
    }
}
