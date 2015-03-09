package com.tbe.prolab;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;


public class createProject extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void valid(View view){
        new WebAccess(((TextView) findViewById(R.id.create_project_name)).getText().toString()
        , ((TextView) findViewById(R.id.create_project_punchline)).getText().toString()
        , ((TextView) findViewById(R.id.create_project_description)).getText().toString()
        , ((TextView) findViewById(R.id.create_project_url)).getText().toString()).execute();
    }

    protected void callFail(){
        Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
    }


    protected void callMain(){
        this.finish();
    }

    private class WebAccess extends AsyncTask<String, Void, String> {

        String projectName;
        String projectPunshLine;
        String projectDescription;
        String projectURL;


        public WebAccess(String projectName, String projectPunshLine, String projectDescription, String projectURL) {
            this.projectDescription = projectDescription;
            this.projectName = projectName;
            this.projectPunshLine = projectPunshLine;
            this.projectURL = projectURL;
        }

        @Override
        protected String doInBackground(String... urls)  {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(main.HOST + "/v1/projects");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write("name="+ projectName+"&description="+projectDescription+"&url="+projectURL+"&punchline="+projectPunshLine);
                writer.flush();

                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("Connection ", "The response is: " + response);
                is = conn.getInputStream();
                // Convert the InputStream into a string
                return readIt(is, len);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch (Exception e){
                return "fail";
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("fail")) {
                callFail();
            } else {
                callMain();
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
