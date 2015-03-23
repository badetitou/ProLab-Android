package com.tbe.prolab.Users;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tbe.prolab.Project.ProjectAdapter;
import com.tbe.prolab.R;
import com.tbe.prolab.main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InfoUser extends ActionBarActivity {

    TextView username;
    TextView email;
    TextView firstname;
    TextView lastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        Bundle bundle = this.getIntent().getExtras();
        username = (TextView) findViewById(R.id.info_user_username);
        email = (TextView) findViewById(R.id.info_user_email);
        firstname = (TextView) findViewById(R.id.info_user_firstname);
        lastname = (TextView) findViewById(R.id.info_user_lastname);
        new WebAccessUser(bundle.getString("username")).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class WebAccessUser extends AsyncTask<String, Void, String> {

        private String userName;

        public WebAccessUser(String userName) {
            this.userName = userName;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return getUser();
            } catch (Exception e) {
                return "fail";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                username.setText(jsonObject.getString("username"));
                email.setText(jsonObject.getString("email"));
                firstname.setText(jsonObject.getString("firstname"));
                lastname.setText(jsonObject.getString("lastname"));
            } catch (JSONException e) {
            }
        }

        public String getUser() throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(main.HOST + "/v1/users/" + username);
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
